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

		ApiServer api = new ApiServer(queryEngine);
		port(8080);
		ApiServer.enableCORS("*", "GET,POST,OPTIONS", "Content-Type,Authorization");
		api.configureRoutes();

	}
}
