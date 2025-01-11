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

public class ApiServer3 {
    private static final Gson gson = new Gson();
    private QueryEngine queryEngine;

    public ApiServer3(QueryEngine queryEngine) {
        this.queryEngine = queryEngine;
    }

    public void configureRoutes() {
        get("/search", (req, res) -> {

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

                res.status(500);
                return gson.toJson(Map.of("error", "Intern error of the server", "details", e.getMessage()));
            }
        });

        get("/test", (req, res) -> {
            try {
                String phrase = req.queryParams("phrase");

                if (phrase == null || phrase.isEmpty()) {
                    res.status(400);
                    return gson.toJson(Map.of("error", "The search parameter 'phrase' is required"));
                }

                Map<String, Object> response = queryEngine.executeQuery(phrase, null, null, null);
                res.type("application/json");
                return gson.toJson(response);

            } catch (Exception e) {
                res.status(500);
                return gson.toJson(Map.of("error", "Intern error of the server", "details", e.getMessage()));
            }
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
