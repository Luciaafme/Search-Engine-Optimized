package control;


import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import model.Metadata;
import model.Word;

import java.util.*;
import java.util.stream.Collectors;

public class QueryEngine {
	private final IMap<String, Metadata> metadataDatamartMap;
	private final IMap<String, Set<Word.WordOccurrence>> wordDatamartMap;
	private final IMap<String, String> datalakeMap;

	public QueryEngine(HazelcastManager hazelcastManager) {
		HazelcastInstance hazelcastInstance = hazelcastManager.getHazelcastInstance();
		this.metadataDatamartMap = hazelcastInstance.getMap("metadataMap");
		this.wordDatamartMap = hazelcastInstance.getMap("wordDatamartMap");
		this.datalakeMap = hazelcastInstance.getMap("datalakeMap");

	}

	public Map<String, Object> executeQuery(String words, String author, String startYear, String endYear) {
		Map<String, Object> response = new HashMap<>();

		List<String> wordsList = List.of(words.split(" "));
		Set<Word.WordOccurrence> matchOcurrences = calculateIntersection(wordsList);
		List<Metadata> matchMetadata = filterMetadata(matchOcurrences, author, startYear, endYear);

		response.put("response", matchMetadata);
		return response;
	}


	private Set<Word.WordOccurrence> calculateIntersection(List<String> wordsList) {
		if (wordsList == null || wordsList.isEmpty()) {
			return new HashSet<>();
		}

		Set<Word.WordOccurrence> intersection = this.wordDatamartMap.getOrDefault(wordsList.get(0), new HashSet<>());

		for (String word: wordsList) {
			Set<Word.WordOccurrence> occurrences = this.wordDatamartMap.getOrDefault(word, new HashSet<>());
			intersection.retainAll(occurrences);
		}

		return intersection;
	}



	private List<Metadata> filterMetadata(Set<Word.WordOccurrence> occurrences, String author, String startYear, String endYear) {
		Set<String> bookIDs = occurrences.stream()
				.map(Word.WordOccurrence::getBookID)
				.collect(Collectors.toSet());

		return metadataDatamartMap.values().stream()
				.filter(metadata -> bookIDs.contains(metadata.getBookID()))
				.filter(metadata -> (author == null || metadata.getAuthor().equalsIgnoreCase(author)))
				.filter(metadata -> {
					try {
						int year = Integer.parseInt(metadata.getYear());
						Integer start = (startYear != null) ? Integer.parseInt(startYear) : null;
						Integer end = (endYear != null) ? Integer.parseInt(endYear) : null;

						boolean afterStartYear = (start == null || year >= start);
						boolean beforeEndYear = (end == null || year <= end);
						return afterStartYear && beforeEndYear;
					} catch (NumberFormatException e) {
						return false;
					}
				})
				.collect(Collectors.toList());
	}


	public Map<String, List<String>> getPreviewLines(List<Metadata> metadataList, List<Word> wordList) {
		Map<String, List<String>> previewLinesByBook = new HashMap<>();

		// 1. Create a map of words to their occurrences by book
		Map<String, Map<String, Set<Integer>>> wordsByBook = new HashMap<>();

		// Organize words and their occurrences by book
		for (Word word : wordList) {
			for (Word.WordOccurrence occurrence : word.getOccurrences()) {
				String bookID = occurrence.getBookID();

				// Initialize the word map for this book if it doesn't exist
				wordsByBook.putIfAbsent(bookID, new HashMap<>());

				// Add the word and its line occurrences
				wordsByBook.get(bookID).putIfAbsent(word.getText(), new HashSet<>());
				wordsByBook.get(bookID).get(word.getText()).addAll(occurrence.getLineOccurrences());
			}
		}

		// 2. Process each book's metadata and content
		for (Metadata metadata : metadataList) {
			String bookID = metadata.getBookID();
			int startLine = metadata.getBookStartLine();

			// Skip if no content or no words for this book
			if (!datalakeMap.containsKey(bookID) || !wordsByBook.containsKey(bookID)) {
				continue;
			}

			// Get book content and split into lines
			String bookContent = datalakeMap.get(bookID);
			List<String> allLines = Arrays.asList(bookContent.split("\n"));

			// 3. Process each word's occurrences
			List<String> bookPreviewLines = new ArrayList<>();
			Map<String, Set<Integer>> wordsInBook = wordsByBook.get(bookID);

			for (Map.Entry<String, Set<Integer>> wordEntry : wordsInBook.entrySet()) {
				// Find the first valid line occurrence for this word
				Optional<Integer> firstValidLine = wordEntry.getValue().stream()
						.filter(lineNumber -> lineNumber >= startLine && lineNumber < allLines.size())
						.findFirst();

				// Add the line if found
				firstValidLine.ifPresent(lineNumber ->
						bookPreviewLines.add(allLines.get(lineNumber)));
			}

			// 4. Add preview lines if any were found
			if (!bookPreviewLines.isEmpty()) {
				previewLinesByBook.put(bookID, bookPreviewLines);
			}
		}

		return previewLinesByBook;
	}

}

