package control;


import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import model.Metadata;

import java.util.List;
import java.util.stream.Collectors;

public class QueryEngine {
	private final HazelcastInstance hazelcastInstance;
	private final IMap<String, Metadata> metadataMap;

	public QueryEngine(HazelcastManager hazelcastManager) {
		this.hazelcastInstance = hazelcastManager.getHazelcastInstance();
		this.metadataMap = hazelcastInstance.getMap("metadataMap");

	}

	public List<Metadata> filterMetadata(String language, String author, Integer startYear, Integer endYear) {
		return metadataMap.values().stream()
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
