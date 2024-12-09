package control;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;

public class HazelcastTest {
    public static void main(String[] args) {
        // Conectar a la instancia de Hazelcast
        HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();

        // Obtener el mapa distribuido
        IMap<Integer, String> booksMap = hazelcastInstance.getMap("booksMap");

        // Verificar si hay libros en el mapa
        if (booksMap.isEmpty()) {
            System.out.println("No hay libros almacenados en Hazelcast.");
        } else {
            System.out.println("Libros almacenados en Hazelcast:");
            booksMap.forEach((bookId, content) -> {
                System.out.println("Libro ID: " + bookId);
                System.out.println("Contenido: " + content.substring(0, Math.min(content.length(), 100)) + "..."); // Mostrar los primeros 100 caracteres
            });
        }

        // Cerrar Hazelcast (opcional)
        hazelcastInstance.shutdown();
    }
}
