package control;

import static control.ApiServer.configureRoutes;
import static control.ApiServer.enableCORS;
import static spark.Spark.port;

public class Main {
	public static void main(String[] args) {

		port(8080);
		enableCORS("*", "GET,POST,OPTIONS", "Content-Type,Authorization");

		// Hazelcast setup
		HazelcastManager hazelcastManager = new HazelcastManager();

		QueryEngine queryEngine= new QueryEngine(hazelcastManager);

		configureRoutes(queryEngine);
	}
}
