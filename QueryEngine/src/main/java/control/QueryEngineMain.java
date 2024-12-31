package control;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import model.Metadata;
import model.WordOccurrence;

import java.util.List;
import java.util.Map;

import java.util.*;

import static spark.Spark.port;

public class QueryEngineMain {
	public static void main(String[] args) {

		//port(8080);
		//enableCORS("*", "GET,POST,OPTIONS", "Content-Type,Authorization");


		// Hazelcast setup
		HazelcastClientManager client = new HazelcastClientManager();
		HazelcastInstance clientInstance = client.getHazelcastInstance();

		IMap<String, String> datalakeMap = clientInstance.getMap("datalakeMap");
		IMap<String, Metadata> metadataDatamartMap = clientInstance.getMap("metadataMap");
		IMap<String, List<WordOccurrence>> wordDatamartMap = clientInstance.getMap("wordDatamartMap");

		QueryEngine queryEngine= new QueryEngine(datalakeMap, metadataDatamartMap, wordDatamartMap);

		//configureRoutes(queryEngine);

		// test del query
		Map<String, Object> results = queryEngine.executeQuery("two famous", null, null, null);

		// print line where the wrod appear
		List<String> linesList = Optional.ofNullable(results)
				.map(m -> (Map<String, Object>) m.get("response"))
				.map(m -> (Map<String, Object>) m.get("10088"))
				.map(m -> (List<String>) m.get("lines"))
				.orElse(Collections.emptyList());


		System.out.println(linesList.get(0));
		System.out.println(linesList.get(1));


	}
}
