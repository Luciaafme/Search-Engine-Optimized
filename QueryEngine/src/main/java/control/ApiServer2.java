package control;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.*;
import org.jsoup.Jsoup;

import static spark.Spark.*;

public class ApiServer2 {

	private static final Gson gson = new Gson();

	// hay que poner la ruta completa donde se encuentra el archivo words, sino no lee su contenido
	private static final List<String> words = loadContent("C:\\Users\\alvar\\Desktop\\Universidad\\03 - AÑO3\\Semestre-1\\04-BD\\01-Teoria\\01-Assignments\\02-Individual Assignment\\04 - Stage4\\Search-Engine-Optimized\\QueryEngine\\src\\main\\resources\\words.txt");
	private static final List<String> queries = loadContent("C:\\Users\\alvar\\Desktop\\Universidad\\03 - AÑO3\\Semestre-1\\04-BD\\01-Teoria\\01-Assignments\\02-Individual Assignment\\04 - Stage4\\Search-Engine-Optimized\\QueryEngine\\src\\main\\resources\\queries.txt");
	private static final Random random = new Random();
	private static int threads = 1;
	private static int threadCount = 0;
	private static long sleepTime = 100;
	private static final List<Integer> latencies = new ArrayList<>();
	private static int exceptions;
	private QueryEngine queryEngine;
	private static String baseUrl;

	public ApiServer2(QueryEngine queryEngine, String baseUrl) {
		this.queryEngine = queryEngine;
		this.baseUrl = baseUrl;
	}

	public void configureRoutes() {
		get("/search", (req, res) -> {
			String phrase = req.queryParams("phrase");
			String author = req.queryParams("author");
			String startYear = req.queryParams("startyear");
			String endYear = req.queryParams("endyear");

			if (phrase == null || phrase.isEmpty()) {
				res.status(400);
				return gson.toJson(Map.of("error", "The search parameter 'phrase' is required"));
			}

			try {
				Map<String, Object> response = queryEngine.executeQuery(phrase, author, startYear, endYear);
				res.type("application/json");
				return gson.toJson(response);
			} catch (Exception e) {
				res.status(500);
				return gson.toJson(Map.of("error", "Intern error of the server", "details", e.getMessage()));
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

	private static TimerTask task() {
		return new TimerTask() {
			@Override
			public void run() {
				reportData(new ArrayList<>(latencies));
				resetData();
				increaseSpeed();
			}

			private void reportData(List<Integer> latencies) {
				String sep = " | ";
				String report = String.join(sep,
						Instant.now() + "",
						"Requests: " + latencies.size(),
						"AVG response time: " + latencies.stream().mapToInt(i -> i).average().orElse(0) + " ms",
						"Exceptions: " + exceptions,
						"Threads: " + threadCount,
						"Sleep time: " + sleepTime + " ms");

				System.out.println("------- REPORT -------\n" + report);

				saveReportToFile(report);
			}

			private static void saveReportToFile(String report) {
				String filePath = "C:\\Users\\alvar\\Desktop\\Universidad\\03 - AÑO3\\Semestre-1\\04-BD\\01-Teoria\\01-Assignments\\02-Individual Assignment\\04 - Stage4\\Search-Engine-Optimized\\QueryEngine\\src\\main\\resources\\historialQueries.txt"; // Ruta del archivo donde se guardará el historial
				//String filePath = "C:\\Users\\alvar\\Desktop\\Universidad\\03 - AÑO3\\Semestre-1\\04-BD\\01-Teoria\\01-Assignments\\02-Individual Assignment\\04 - Stage4\\Search-Engine-Optimized\\QueryEngine\\src\\main\\resources\\historialWords.txt"; // Ruta del archivo donde se guardará el historial

				try {
					Files.write(Paths.get(filePath), report.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
				} catch (IOException e) {
					System.err.println("Error al escribir en el archivo: " + e.getMessage());
				}
			}



			private void increaseSpeed() {
				if (threadCount < 8) createThread();
				else if (sleepTime > 2) sleepTime /= 2;
			}

			private void resetData() {
				synchronized (latencies) {
					latencies.clear();
					exceptions = 0;
				}
			}
		};
	}


	private static void createThread() {
		new Thread(() -> {
			threadCount++;
			while (true) {
				try {
					doQuery();
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
		}).start();
	}

	private static void doQuery() {
		try {
			long start = System.currentTimeMillis();
			//Jsoup.connect(baseUrl + "/test?phrase=" + words()).ignoreContentType(true).get(); // To test with only words
			List<String> query = queries();
			Jsoup.connect(baseUrl + "/test?phrase=" + query.get(0) + "?author=" + query.get(1) + "?yearStart=" + query.get(2) + "?yearEnd=" + query.get(3)).ignoreContentType(true).get(); // To test with queries with metadata

			long stop = System.currentTimeMillis();
			synchronized (latencies) {
				latencies.add((int) (stop - start));
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
			exceptions++;
		}
	}

	private static String words() {
		int count = new Random().nextInt(2) + 1;
		StringBuilder result = new StringBuilder(words.get(new Random().nextInt(words.size())));
		for (int i = 1; i < count; i++) result.append("&").append(words.get(new Random().nextInt(words.size())));
		return result.toString();
	}

	private static List<String> queries() {
		List<String> query = List.of(queries.get(random.nextInt(queries.size())).split(","));
		return query;
	}

	public void execute() {
		for (int i = 0; i < threads; i++) createThread();
		new Timer().schedule(task(), 5000, 5000);
	}
}

