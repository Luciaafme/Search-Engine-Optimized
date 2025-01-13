package control;

public class ClientMain {
    public static void main(String[] args) {
        String loadBalancerUrl = "http://localhost:8080";
        String wordsPath = "C:\\Users\\lucia\\IdeaProjects\\Search-Engine-Optimized-\\QueryEngine\\src\\main\\resources\\words\\words.txt";
        String queriesPath = "C:\\Users\\lucia\\IdeaProjects\\Search-Engine-Optimized-\\QueryEngine\\src\\main\\resources\\words\\words.txt";

        ClientModule clientModule = new ClientModule(loadBalancerUrl, wordsPath, queriesPath);

        String endpoint = "/test";

        System.out.println("Simulating multiple clients...");
        clientModule.simulateClients(10, endpoint);
    }
}
