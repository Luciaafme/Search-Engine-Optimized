package control;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import model.Metadata;
import model.WordOccurrence;

import java.util.List;

import static spark.Spark.port;

public class QueryEngineMain {
	public static void main(String[] args) {

		HazelcastServer server = new HazelcastServer();
		HazelcastInstance hazelcastInstance = server.getHazelcastInstance();

		IMap<String, Metadata> metadataDatamartMap = hazelcastInstance.getMap("metadataDatamartMap");
		IMap<String, List<WordOccurrence>> wordDatamartMap = hazelcastInstance.getMap("wordDatamartMap");
		IMap<String, String> datalakeMap = hazelcastInstance.getMap("datalakeMap");


		QueryEngine queryEngine = new QueryEngine(datalakeMap, metadataDatamartMap, wordDatamartMap);

		String baseUrl = "http://localhost:8080";

		ApiServer2 api = new ApiServer2(queryEngine, baseUrl, args[0], args[1]);
		port(8080);
		ApiServer2.enableCORS("*", "GET,POST,OPTIONS", "Content-Type,Authorization");
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
