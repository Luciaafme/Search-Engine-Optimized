package control;

import com.google.gson.Gson;

import java.util.Map;

import static spark.Spark.*;

public class ApiServer {

	private static final Gson gson = new Gson();

	public static void configureRoutes(BookController bookController) {
		get("/search", (req, res) -> {
			String phrase = req.queryParams("phrase");
			String author=req.queryParams("author");
			String startyear = req.queryParams("startyear");
			String endyear = req.queryParams("endyear");

			if (phrase == null || phrase.isEmpty()) {
				res.status(400);
				return gson.toJson(Map.of("error", "The search parameter 'phrase' is required"));
			}

			try {
				Map<String, Object> response = bookController.searchWords(phrase);

				res.type("application/json");
				return gson.toJson(response);
			} catch (Exception e) {
				res.status(500);
				return gson.toJson(Map.of("error", "Intern error of the server", "details", e.getMessage()));
			}
		});

		notFound((req, res) -> {
			res.type("application/json");
			return gson.toJson(Map.of("status", 404, "message", "Path not found"));
		});
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