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
				.map(Word.WordOccurrence::getBook_id)
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


	public Map<String, List<String>> getPreviewLines(List<Metadata> metadataList) {
		Map<String, List<String>> previewLines = new HashMap<>();

		for (Metadata metadata : metadataList) {
			String bookID = metadata.getBookID();

			if (datalakeMap.containsKey(bookID)) {
				List<String> lines = Arrays.asList(datalakeMap.get(bookID).split("\n"));
				previewLines.put(bookID, lines); // se añaden todas las lineas del libro
				// TODO: añadir al diccionario solo las lineas donde aparecen las palabras
			}
		}

		return previewLines;
	}



}
