package control;

public class ClientMain {
    public static void main(String[] args) {
        String baseURL = "http://localhost:8080";
        String wordsPath = "/words.txt";    // Add the path

        ClientModule clientModule = new ClientModule(baseURL, wordsPath);

        String endpoint = "/test";

        System.out.println("Simulating multiple clients...");
        clientModule.simulateClients(10, endpoint);
    }
}
