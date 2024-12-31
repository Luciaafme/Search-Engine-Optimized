package control;

import com.hazelcast.core.IMap;
import model.Metadata;
import model.WordOccurrence;

import java.util.*;
import java.util.stream.Collectors;

public class QueryEngine {
	private final IMap<String, Metadata> metadataDatamartMap;
	private final IMap<String, List<WordOccurrence>> wordDatamartMap;
	private final IMap<String, String> datalakeMap;

	public QueryEngine(IMap<String, String> datalakeMap,
					   IMap<String, Metadata> metadataDatamartMap,
					   IMap<String, List<WordOccurrence>> wordDatamartMap) {
		this.datalakeMap = datalakeMap;
		this.metadataDatamartMap = metadataDatamartMap;
		this.wordDatamartMap = wordDatamartMap;
	}

	public Map<String, Object> executeQuery(String words, String author, String startYear, String endYear) {
		// 1º mapa: solo tiene una clave -> response
		Map<String, Object> response = new HashMap<>();

		List<String> wordList = List.of(words.split(" "));
		Set<String> matchingBookIds = calculateIntersection(wordList);
		//System.out.println(matchingBookIds);
		List<Metadata> matchMetadata = filterMetadata(matchingBookIds, author, startYear, endYear);
		//System.out.println(matchMetadata);

		// 2º mapa -> claves: bookID donde aparecen todas las palabras de la query -> valores: otro mapa interno
		Map<String, Object> results = new HashMap<>();

		for (Metadata metadata : matchMetadata) {
			Map<String, Object> auxiliar = getPreviewLines(metadata, wordList);
			results.put(metadata.getBookID(), auxiliar);
		}
		response.put("response", results);
		return response;
	}

	private Set<String> calculateIntersection(List<String> wordsList) {

		//System.out.println("palabra a buscar:" + wordsList.toString());

		if (wordsList == null || wordsList.isEmpty()) {
			return new HashSet<>();
		}

		// Get the book IDs for the first word
		Set<String> intersection = wordDatamartMap.getOrDefault(wordsList.get(0), new ArrayList<>())
				.stream()
				.map(WordOccurrence::getBookID)
				.collect(Collectors.toSet());

		// Intersect with book IDs of remaining words
		for (String word : wordsList.subList(1, wordsList.size())) {
			Set<String> wordBookIds = wordDatamartMap.getOrDefault(word, new ArrayList<>())
					.stream()
					.map(WordOccurrence::getBookID)
					.collect(Collectors.toSet());

			intersection.retainAll(wordBookIds);
		}

		//System.out.println("interseccion" + intersection);
		return intersection;
	}

	private List<Metadata> filterMetadata(Set<String> bookIDs, String author, String startYear, String endYear) {
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

	public Map<String, Object> getPreviewLines(Metadata metadata, List<String> wordList) {
		Map<String, Object> previewLines = new HashMap<>();
		List<String> lines = new ArrayList<>();

		String bookContent = datalakeMap.get(metadata.getBookID());
		String[] bookLines = bookContent.split("\n");

		for (String word : wordList) {
			List<WordOccurrence> occurrences = wordDatamartMap.get(word);
			if (occurrences != null) {
				Optional<WordOccurrence> bookOccurrence = occurrences.stream()
						.filter(occurrence -> occurrence.getBookID().equals(metadata.getBookID()))
						.findFirst();

				if (bookOccurrence.isPresent()) {
					List<Integer> lineNumbers = bookOccurrence.get().getLineNumbers();
					if (!lineNumbers.isEmpty()) {
						int wordLineNumber = lineNumbers.get(0);  // Get first occurrence
						int actualLineIndex = wordLineNumber + metadata.getBookStartLine() + 3; // se le suma 3 porque es el margen de error
						//System.out.println("number" + actualLineIndex);

						if (actualLineIndex >= 0 && actualLineIndex < bookLines.length) {
							lines.add(bookLines[actualLineIndex]);
							// test
							//System.out.println(bookLines[actualLineIndex]);
						} else {
							throw new IllegalArgumentException(
									"Line number " + wordLineNumber + " is out of range for book content.");
						}
					}
				}
			}
		}

		previewLines.put("metadata", metadata);
		previewLines.put("lines", lines);

		return previewLines;
	}
}