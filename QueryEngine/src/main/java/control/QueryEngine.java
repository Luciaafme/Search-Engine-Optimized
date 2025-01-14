package control;

import com.hazelcast.core.IMap;
import model.Metadata;
import model.WordOccurrence;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
		Map<String, Object> response = new HashMap<>();

		List<String> wordList = List.of(words.split(" "));
		Set<String> matchingBookIds = calculateIntersection(wordList);
		List<Metadata> matchMetadata = filterMetadata(matchingBookIds, author, startYear, endYear);

		Map<String, Object> results = new HashMap<>();

		for (Metadata metadata : matchMetadata) {
			Map<String, Object> auxiliar = getPreviewLines(metadata, wordList);
			results.put(metadata.getBookID(), auxiliar);
		}
		response.put(words, results);
		return response;
	}

	private Set<String> calculateIntersection(List<String> wordsList) {

		if (wordsList == null || wordsList.isEmpty()) {
			return new HashSet<>();
		}

		Set<String> intersection = wordDatamartMap.getOrDefault(wordsList.get(0), new ArrayList<>())
				.stream()
				.map(WordOccurrence::getBookID)
				.collect(Collectors.toSet());

		for (String word : wordsList.subList(1, wordsList.size())) {
			Set<String> wordBookIds = wordDatamartMap.getOrDefault(word, new ArrayList<>())
					.stream()
					.map(WordOccurrence::getBookID)
					.collect(Collectors.toSet());

			intersection.retainAll(wordBookIds);
		}

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
		List<Integer> wordsLineNumberList = new ArrayList<>();

		String bookContent = datalakeMap.get(metadata.getBookID());

		for (String word : wordList) {
			List<WordOccurrence> occurrences = wordDatamartMap.get(word);
			if (occurrences != null) {
				Optional<WordOccurrence> bookOccurrence = occurrences.stream()
						.filter(occurrence -> occurrence.getBookID().equals(metadata.getBookID()))
						.findFirst();

				if (bookOccurrence.isPresent()) {
					List<Integer> lineNumbers = bookOccurrence.get().getLineNumbers();
					if (!lineNumbers.isEmpty()) {
						wordsLineNumberList.add(lineNumbers.get(0));
					}
				}
			}
		}



		previewLines.put("metadata", metadata);
		previewLines.put("lines", getWordLines(bookContent, wordsLineNumberList));

		return previewLines;
	}

	public List<String>  getWordLines(String bookContent, List<Integer> wordLineNumberList) {

		String regex = "(?i)\\*\\*\\* START OF THE PROJECT GUTENBERG EBOOK .*? \\*\\*\\*(.*?)\\*\\*\\* END OF THE PROJECT GUTENBERG EBOOK .*? \\*\\*\\*";
		Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
		Matcher matcher = pattern.matcher(bookContent);

		List<String> selectedLines = new ArrayList<>();

		if (matcher.find()) {
			String extractedContent = matcher.group(1).trim();
			String[] bookLinesList = extractedContent.split("\n");

			for(int wordLineNumber: wordLineNumberList){
				selectedLines.add(bookLinesList[wordLineNumber]);
			}

			return selectedLines;

		} else {
			return new ArrayList<>();
		}
	}
}