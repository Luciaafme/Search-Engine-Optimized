package control;

public class ClientMain {
    public static void main(String[] args) {
        // URL del balanceador de carga
        String loadBalancerUrl = "http://localhost:8080";
        String wordsPath = "C:\\Users\\lucia\\IdeaProjects\\Search-Engine-Optimized-\\QueryEngine\\src\\main\\resources\\words\\words.txt";
        String queriesPath = "C:\\Users\\lucia\\IdeaProjects\\Search-Engine-Optimized-\\QueryEngine\\src\\main\\resources\\words\\words.txt";

        // Crear instancia del módulo de clientes
        ClientModule clientModule = new ClientModule(loadBalancerUrl, wordsPath, queriesPath);

        // Endpoint para probar
        String endpoint = "/test";

        // Simulación de clientes enviando solicitudes GET
        System.out.println("Simulating multiple clients...");
        clientModule.simulateClients(10, endpoint);
    }
}
