package control;


import com.hazelcast.core.IMap;
import model.Metadata;
import model.Word;

import java.util.*;
import java.util.stream.Collectors;

public class QueryEngine {
	private final IMap<String, Metadata> metadataDatamartMap;
	private final IMap<String, Set<Word.WordOccurrence>> wordDatamartMap;
	private final IMap<String, String> datalakeMap;

	public QueryEngine(IMap<String, String> datalakeMap,
					   IMap<String, Metadata> metadataDatamartMap,
					   IMap<String, Set<Word.WordOccurrence>> wordDatamartMap) {

		this.metadataDatamartMap = metadataDatamartMap;
		this.wordDatamartMap = wordDatamartMap;
		this.datalakeMap = datalakeMap;


	}

	public Map<String, Object> executeQuery(String words, String author, String startYear, String endYear) {
		Map<String, Object> response = new HashMap<>();

		List<String> wordList = List.of(words.split(" "));
		Set<Word.WordOccurrence> matchOcurrences = calculateIntersection(wordList);
		List<Metadata> matchMetadata = filterMetadata(matchOcurrences, author, startYear, endYear);

		Map<String, Map<String, Object>> results = new HashMap<>();

		for(Metadata metadata: matchMetadata){
			Map<String, Object> auxiliar = getPreviewLines(metadata, wordList);
			results.put(metadata.getBookID(), auxiliar);
		}
		response.put("response", results);
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


	public Map<String, Object> getPreviewLines(Metadata metadata, List<String> wordList) {

		Map<String, Object> previewLines = new HashMap<>();
		List<String> lines = new ArrayList<>();

		String bookContent = datalakeMap.get(metadata.getBookID());
		String[] bookLines = bookContent.split("\n");

		for (String word : wordList) {
			int wordLineNumber = wordDatamartMap.get(word).stream()
					.filter(occurrence -> occurrence.getBookID().equals(metadata.getBookID()))
					.findFirst()
					.get()
					.getLineOccurrences()
					.iterator()
					.next();

			int actualLineIndex = wordLineNumber + metadata.getBookStartLine();

			if (actualLineIndex >= 0 && actualLineIndex < bookLines.length) {
				lines.add(bookLines[actualLineIndex]);
			} else {
				throw new IllegalArgumentException("El número de línea " + wordLineNumber + " está fuera del rango del contenido del libro.");
			}
		}

		previewLines.put("metadata", metadata);
		previewLines.put("lines", lines);

		return previewLines;
	}



}

