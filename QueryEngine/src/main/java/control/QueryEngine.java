package control;


import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import model.Metadata;
import model.Word;

import java.util.*;
import java.util.stream.Collectors;

public class QueryEngine {
	private final HazelcastInstance hazelcastInstance;
	private final IMap<String, Metadata> metadataDatamartMap;
	private final IMap<String, Set<Word.WordOccurrence>> wordDatamartMap;

	public QueryEngine(HazelcastManager hazelcastManager) {
		this.hazelcastInstance = hazelcastManager.getHazelcastInstance();
		this.metadataDatamartMap = hazelcastInstance.getMap("metadataMap");
		this.wordDatamartMap = hazelcastInstance.getMap("wordDatamartMap");

	}

	public Map<String, Object> executeQuery(String words, String author, String startYear, String endYear) {
		Map<String, Object> response = new HashMap<>();

		//response.put("results", wordResults);
		return response;
	}


	public static Set<Word.WordOccurrence> calculateIntersection(List<String> words) {
		if (words == null || words.isEmpty()) {
			return new HashSet<>(); // Si la lista está vacía, devolvemos un conjunto vacío.
		}

		// Recuperar las ocurrencias del primer conjunto
		Set<Word.WordOccurrence> intersection = this.wordDatamartMap.getOrDefault(words.get(0), new HashSet<>());

		// Iterar sobre las palabras restantes y calcular la intersección
		for (int i = 1; i < words.size(); i++) {
			String word = words.get(i);
			Set<Word.WordOccurrence> occurrences = this.wordDatamartMap.getOrDefault(word, new HashSet<>());
			intersection.retainAll(occurrences); // Retenemos solo los elementos comunes
		}

		return intersection; // Conjunto resultante con las ocurrencias comunes.
	}




	public List<Metadata> filterMetadata(String language, String author, Integer startYear, Integer endYear) {
		return metadataDatamartMap.values().stream()
				// Filtro por lenguaje si no es nulo
				.filter(metadata -> (language == null || metadata.getLanguage().equalsIgnoreCase(language)))
				// Filtro por autor si no es nulo
				.filter(metadata -> (author == null || metadata.getAuthor().equalsIgnoreCase(author)))
				// Filtro por rango de años si se especifican
				.filter(metadata -> {
					try {
						int year = Integer.parseInt(metadata.getYear());
						// Verificar si el año está dentro del rango
						boolean afterStartYear = (startYear == null || year >= startYear);
						boolean beforeEndYear = (endYear == null || year <= endYear);
						return afterStartYear && beforeEndYear;
					} catch (NumberFormatException e) {
						return false; // Ignorar entradas con años no válidos
					}
				})
				.collect(Collectors.toList());
	}

}
