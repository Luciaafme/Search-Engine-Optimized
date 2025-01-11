package control;

public class ClientMain {
    public static void main(String[] args) {
        // URL del balanceador de carga
        String loadBalancerUrl = "http://localhost:80";
        String wordsPath = "hola";
        String queriesPath = "hola";

        // Crear instancia del módulo de clientes
        ClientModule clientModule = new ClientModule(loadBalancerUrl, wordsPath, queriesPath);

        // Endpoint para probar
        String endpoint = "/test";

        // Simulación de clientes enviando solicitudes GET
        System.out.println("Simulating multiple clients...");
        clientModule.simulateClients(10, endpoint);
    }
}
