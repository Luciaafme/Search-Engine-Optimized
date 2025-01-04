package control;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import model.Metadata;
import model.WordOccurrence;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static spark.Spark.port;

public class QueryEngineMain {
	public static void main(String[] args) {

		// Hazelcast setup
		HazelcastClientManager client = new HazelcastClientManager();
		HazelcastInstance clientInstance = client.getHazelcastInstance();

		IMap<String, String> datalakeMap = clientInstance.getMap("datalakeMap");
		IMap<String, Metadata> metadataDatamartMap = clientInstance.getMap("metadataMap");
		IMap<String, List<WordOccurrence>> wordDatamartMap = clientInstance.getMap("wordDatamartMap");

		QueryEngine queryEngine = new QueryEngine(datalakeMap, metadataDatamartMap, wordDatamartMap);

		String baseUrl = "http://localhost:8080";

		ApiServer2 api = new ApiServer2(queryEngine, baseUrl);
		port(8080);
		api.enableCORS("*", "GET,POST,OPTIONS", "Content-Type,Authorization");
		api.configureRoutes();


		api.execute();


		/*
		String query = "printing";
		Map<String,Object> results = queryEngine.executeQuery(query,null,null,null);
		System.out.println("Ejecución con éxito");

		List<String> linesList = Optional.ofNullable(results)
				.map(m -> (Map<String, Object>) m.get("response"))
				.map(m -> (Map<String, Object>) m.get("50350"))
				.map(m -> (List<String>) m.get("lines"))
				.orElse(Collections.emptyList());
		// check results
		List<String> stop = List.of(query.split(" "));
		for(int i=0; i<stop.size(); i++){
			System.out.println(stop.get(i) + " -> " + linesList.get(i));
		}


		 */

	}
}
