package control.word;

import com.hazelcast.core.IMap;
import control.interfaces.WordStoreManager;
import model.Word;
import model.WordOccurrence;

import java.util.HashSet;
import java.util.Set;

public class WordStoreMap implements WordStoreManager {
    private final IMap<String, Set<WordOccurrence>> wordDatamartMap;

    public WordStoreMap(IMap<String, Set<WordOccurrence>> wordDatamartMap) {
        this.wordDatamartMap = wordDatamartMap;
    }

    @Override
    public void update(Set<Word> newWordSet) {
        System.out.println("Inicia update...");

        for (Word newWord : newWordSet) {
            wordDatamartMap.compute(newWord.getText(), (existingWord, existingOccurrences) -> {
                if (existingOccurrences != null) {
                    // Fusionar las nuevas ocurrencias con las existentes
                    for (WordOccurrence newOccurrence : newWord.getOccurrences()) {
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
                    Set<WordOccurrence> occurrences = entry.getValue();
                    System.out.println("Palabra: " + word);
                    occurrences.forEach(occurrence -> {
                        System.out.println("  - Libro: " + occurrence.getBookID() +
                                ", Líneas: " + occurrence.getLineOccurrences());
                    });
                });
    }
}
