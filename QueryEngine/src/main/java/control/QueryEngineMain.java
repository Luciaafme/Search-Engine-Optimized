package control;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import model.Metadata;
import model.WordOccurrence;

import java.util.List;

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
	}
}
