package control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ClientModule {

    private final String loadBalancerUrl;
    private static List<String> words;
    private static final Random random = new Random();

    public ClientModule(String loadBalancerUrl, String wordsPath) {
        this.loadBalancerUrl = loadBalancerUrl;
        this.words = loadContent(wordsPath);
    }

    private static List<String> loadContent(String filePath) {
        try {
            return Files.readAllLines(Paths.get(filePath));
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public void sendGetRequest(String endpoint, String query) {
        try {
            String urlWithParam = loadBalancerUrl + endpoint + "?phrase=" + query;
            URL url = new URL(urlWithParam);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");

            int responseCode = connection.getResponseCode();
            System.out.println("GET Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line);
                    }
                    System.out.println("Response: " + response);
                }
            } else {
                System.out.println("Error: " + connection.getResponseMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String generatePhrase() {
        int wordCount = random.nextInt(2) + 1;
        StringBuilder phrase = new StringBuilder(words.get(random.nextInt(words.size())));
        for (int i = 1; i < wordCount; i++) {
            phrase.append("%20").append(words.get(random.nextInt(words.size())));
        }
        return phrase.toString();
    }

    public void simulateClients(int threads, String endpoint) {
        ExecutorService executor = Executors.newFixedThreadPool(threads);

        try {
            while (true) {
                for (int i = 0; i < threads; i++) {
                    String phrase = generatePhrase();
                    int thread_id = i;
                    executor.submit(() -> {
                        System.out.println("Client " + thread_id + " sending request...");
                        sendGetRequest(endpoint, phrase);
                    });
                }

                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            System.err.println("Simulation interrupted: " + e.getMessage());
        } finally {
            executor.shutdown();
            try {

                if (!executor.awaitTermination(60, java.util.concurrent.TimeUnit.SECONDS)) {
                    System.out.println("Timeout occurred before completing all tasks.");
                } else {
                    System.out.println("All tasks completed.");
                }
            } catch (InterruptedException e) {
                System.err.println("Error while waiting for ExecutorService termination: " + e.getMessage());
            }
        }
    }

}
