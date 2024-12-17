package control;


import model.Metadata;

public class Prueba {
    public static void main(String[] args) {
        HazelcastManager hazelcastManager = new HazelcastManager();
        QueryEngine storeMap = new QueryEngine(hazelcastManager);
/*
        // Insertar algunos metadatos de prueba
        storeMap.update(new Metadata("12345", "Book Title 1", "John Doe", "2023", "English", "http://example.com/1"));
        storeMap.update(new Metadata("67890", "Book Title 2", "John Doe", "2022", "English", "http://example.com/2"));
        storeMap.update(new Metadata("54321", "Book Title 3", "John Doe", "2021", "English", "http://example.com/3"));
        storeMap.update(new Metadata("1", "Book Title 5", "Lucia", "2020", "Spanish", "http://example.com/1"));


 */
        // Filtrar por lenguaje, autor y rango de años
        System.out.println("Filtrar por lenguaje 'English', autor 'John Doe' y años 2021-2023:");
        storeMap.filterMetadata("English", "John Doe", 2021, 2023).forEach(System.out::println);

        // Filtrar solo por rango de años
        System.out.println("Filtrar por años 2022-2023:");
        storeMap.filterMetadata(null, null, 2022, 2023).forEach(System.out::println);

        System.out.println("Filtrar por año de inicio 2021");
        storeMap.filterMetadata(null, null, 2021, null).forEach(System.out::println);

        System.out.println("Filtrar hasta año 2022");
        storeMap.filterMetadata(null, null, null, 2022).forEach(System.out::println);

        // Cerrar Hazelcast
        hazelcastManager.shutdown();
    }
}
