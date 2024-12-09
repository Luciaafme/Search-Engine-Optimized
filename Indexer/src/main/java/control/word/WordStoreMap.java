package control.word;

import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import control.interfaces.WordStoreManager;
import model.Word;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

public class WordStoreMap implements WordStoreManager {
    private final HazelcastInstance hazelcastInstance;
    private final IMap<String, Set<Word.WordOccurrence>> wordDatamartMap;

    public WordStoreMap() {
        try {
            this.hazelcastInstance = Hazelcast.newHazelcastInstance(
                    new XmlConfigBuilder(new FileInputStream("Indexer/src/main/resources/hazelcast.xml")).build()
            );
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Hazelcast configuration file not found.", e);
        }

        this.wordDatamartMap = hazelcastInstance.getMap("wordDatamartMap");
    }

    @Override
    public void update(Set<Word> newWordSet) {
        System.out.println("Inicia update...");

        for (Word newWord : newWordSet) {
            wordDatamartMap.compute(newWord.getText(), (existingWord, existingOccurrences) -> {
                if (existingOccurrences != null) {
                    // Fusionar las nuevas ocurrencias con las existentes
                    for (Word.WordOccurrence newOccurrence : newWord.getOccurrences()) {
                        existingOccurrences.add(newOccurrence);
                    }
                    return existingOccurrences;
                } else {
                    // Crear un nuevo conjunto de ocurrencias si la palabra no existe
                    return new HashSet<>(newWord.getOccurrences());
                }
            });
        }
    }

    @Override
    public void printMap() {
        // Imprimir el contenido del mapa para verificar
        wordDatamartMap.entrySet().stream()
                .forEach(entry -> {
                    String word = entry.getKey();
                    Set<Word.WordOccurrence> occurrences = entry.getValue();
                    System.out.println("Palabra: " + word);
                    occurrences.forEach(occurrence -> {
                        System.out.println("  - Libro: " + occurrence.getBookID() +
                                ", Líneas: " + occurrence.getLineOccurrences());
                    });
                });
    }
}
