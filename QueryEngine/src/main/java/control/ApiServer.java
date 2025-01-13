package control;

import com.google.gson.Gson;

import java.time.Instant;
import java.util.*;

import static spark.Spark.*;


public class ApiServer {
    private static final Gson gson = new Gson();
    private final QueryEngine queryEngine;
    private static final List<Integer> latencies = new ArrayList<>();
    private static int exceptions;

    public ApiServer(QueryEngine queryEngine) {
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
