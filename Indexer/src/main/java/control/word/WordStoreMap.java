package control.word;

import com.hazelcast.core.IMap;
import control.interfaces.WordStoreManager;
import model.WordOccurrence;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WordStoreMap implements WordStoreManager {
	private final IMap<String, List<WordOccurrence>> wordDatamartMap;

	public WordStoreMap(IMap<String, List<WordOccurrence>> wordDatamartMap) {
		this.wordDatamartMap = wordDatamartMap;
	}

	@Override
	public void update(String bookID, Map<String, List<Integer>> newWordsMap) {

		for (Map.Entry<String, List<Integer>> entry : newWordsMap.entrySet()) {
			String word = entry.getKey();
			List<Integer> lineNumberList = entry.getValue();

			WordOccurrence newOccurrence = new WordOccurrence(bookID, lineNumberList);

			List<WordOccurrence> existingOccurrences = wordDatamartMap.getOrDefault(word, new ArrayList<>());

			boolean exists = existingOccurrences.stream()
					.anyMatch(occurrence -> occurrence.getBookID().equals(bookID));

			if (!exists) {
				existingOccurrences.add(newOccurrence);
				wordDatamartMap.put(word, existingOccurrences);
			}
		}
	}

}