package control;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import model.Metadata;
import model.Word;

import java.util.Map;
import java.util.Set;

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
		IMap<String, Set<Word.WordOccurrence>> wordDatamartMap = clientInstance.getMap("wordDatamartMap");

		QueryEngine queryEngine= new QueryEngine(datalakeMap, metadataDatamartMap, wordDatamartMap);

		//configureRoutes(queryEngine);

		// test del query
		Map<String, Object> result = queryEngine.executeQuery("car hello moto", null, null, null);


		// Imprimir resultado
		System.out.println("Resultado de la consulta:");
		result.forEach((key, value) -> {
			System.out.println(key + ": " + value);
		});
	}
}
