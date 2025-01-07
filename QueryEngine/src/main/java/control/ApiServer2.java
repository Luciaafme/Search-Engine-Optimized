package control;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.*;

import static spark.Spark.*;

public class ApiServer2 {

	private static final Gson gson = new Gson();

	// hay que poner la ruta completa donde se encuentra el archivo words, sino no lee su contenido
	private static final List<String> words = loadContent("C:\\Users\\alvar\\Desktop\\Universidad\\03 - AÑO3\\Semestre-1\\04-BD\\01-Teoria\\01-Assignments\\02-Individual Assignment\\04 - Stage4\\Search-Engine-Optimized\\QueryEngine\\src\\main\\resources\\words.txt");
	private static final List<String> queries = loadContent("C:\\Users\\alvar\\Desktop\\Universidad\\03 - AÑO3\\Semestre-1\\04-BD\\01-Teoria\\01-Assignments\\02-Individual Assignment\\04 - Stage4\\Search-Engine-Optimized\\QueryEngine\\src\\main\\resources\\queries.txt");
	private static final Random random = new Random();
	private static final List<Integer> latencies = new ArrayList<>();
	private static int exceptions;
	private QueryEngine queryEngine;

	public ApiServer2(QueryEngine queryEngine, String baseUrl) {
		this.queryEngine = queryEngine;
	}

	public void configureRoutes() {

		get("/search", (req, res) -> {

			long start = System.currentTimeMillis();
			try {

				String phrase = req.queryParams("phrase");
				String author = req.queryParams("author");
				String startYear = req.queryParams("startyear");
				String endYear = req.queryParams("endyear");


				if (phrase == null || phrase.isEmpty()) {
					res.status(400);
					return gson.toJson(Map.of("error", "The search parameter 'phrase' is required"));
				}


				Map<String, Object> response = queryEngine.executeQuery(phrase, author, startYear, endYear);
				res.type("application/json");
				return gson.toJson(response);

			} catch (Exception e) {

				exceptions++;
				res.status(500);
				return gson.toJson(Map.of("error", "Intern error of the server", "details", e.getMessage()));
			} finally {

				long stop = System.currentTimeMillis();

				synchronized (latencies) {
					latencies.add((int) (stop - start));
				}
			}
		});

		get("/test", (req, res) -> {
			List<Map<String, Object>> results = new ArrayList<>();
			for (int i = 0; i < 10; i++) { // Ejemplo: 10 pruebas
				String phrase = generatePhrase();
				try {
					Map<String, Object> response = queryEngine.executeQuery(phrase, null, null, null);
					results.add(Map.of("phrase", phrase, "response", response));
				} catch (Exception e) {
					results.add(Map.of("phrase", phrase, "error", e.getMessage()));
				}
			}
			res.type("application/json");
			return gson.toJson(results);
		});

		notFound((req, res) -> {
			res.type("application/json");
			return gson.toJson(Map.of("status", 404, "message", "Path not found"));
		});

		get("/testMetadata", (req, res) -> {
			List<Map<String, Object>> results = new ArrayList<>();
			for (int i = 0; i < 10; i++) { // Ejemplo: 10 pruebas
				List<String> query = generatePhraseWithMetadata();
				try {
					Map<String, Object> response = queryEngine.executeQuery(query.get(0), query.get(1), query.get(2), query.get(3));
					results.add(Map.of("phrase", query.get(0), "author", query.get(1), "yearStart", query.get(2), "yearEnd", query.get(3), "response", response)); // MIRAR LA PARTE DE QUERY
				} catch (Exception e) {
					results.add(Map.of("phrase", query.get(0), "author", query.get(1), "yearStart", query.get(2), "yearEnd", query.get(3), "error", e.getMessage())); // MIRAR LA PARTE DE QUERY
				}
			}
			res.type("application/json");
			return gson.toJson(results);
		});

		notFound((req, res) -> {
			res.type("application/json");
			return gson.toJson(Map.of("status", 404, "message", "Path not found"));
		});

		get("/report", (req, res) -> {

			List<Integer> snapshotLatencies;
			synchronized (latencies) {
				snapshotLatencies = new ArrayList<>(latencies);
			}

			double avgResponseTime = snapshotLatencies.stream()
					.mapToDouble(Integer::doubleValue)
					.average()
					.orElse(0);

			int minLatency = snapshotLatencies.stream()
					.min(Integer::compare)
					.orElse(0);

			int maxLatency = snapshotLatencies.stream()
					.max(Integer::compare)
					.orElse(0);

			Map<String, Object> reportData = new HashMap<>();
			reportData.put("timestamp", Instant.now().toString());
			reportData.put("requests", snapshotLatencies.size());
			reportData.put("avgResponseTime", avgResponseTime + " ms");
			reportData.put("minLatency", minLatency + " ms");
			reportData.put("maxLatency", maxLatency + " ms");
			reportData.put("exceptions", exceptions);

			res.type("application/json");
			return gson.toJson(reportData);
		});


		notFound((req, res) -> {
			res.type("application/json");
			return gson.toJson(Map.of("status", 404, "message", "Path not found"));
		});
	}

	private static List<String> generatePhraseWithMetadata() {
		String query = queries.get(random.nextInt(queries.size()));
		return Arrays.asList(query.split(","));
	}

	private static List<String> loadContent(String filePath) {
		try {
			return Files.readAllLines(Paths.get(filePath));
		} catch (IOException e) {
			System.err.println("Error leyendo el archivo: " + e.getMessage());
			return Collections.emptyList();
		}
	}


	private static String generatePhrase() {
		int wordCount = random.nextInt(2) + 1;
		StringBuilder phrase = new StringBuilder(words.get(random.nextInt(words.size())));
		for (int i = 1; i < wordCount; i++) {
			phrase.append(" ").append(words.get(random.nextInt(words.size())));
		}
		return phrase.toString();
	}

	public static void enableCORS(final String origin, final String methods, final String headers) {
		before((request, response) -> {
			response.header("Access-Control-Allow-Origin", origin);
			response.header("Access-Control-Allow-Methods", methods);
			response.header("Access-Control-Allow-Headers", headers);
		});

		options("/*", (request, response) -> {
			String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
			if (accessControlRequestHeaders != null) {
				response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
			}

			String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
			if (accessControlRequestMethod != null) {
				response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
			}

			return "OK";
		});
	}


}

