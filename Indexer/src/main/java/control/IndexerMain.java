package control;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import control.interfaces.*;
import control.metadata.MetadataExtractor;
import control.metadata.MetadataStoreMap;
import control.word.CustomEntryListener;
import control.word.WordCleaner;
import control.word.WordExtractor;
import control.word.WordStoreMap;
import model.Metadata;
import model.WordOccurrence;

import java.util.List;

public class IndexerMain {
	public static void main(String[] args) {

		HazelcastClientManager client = new HazelcastClientManager();
		HazelcastInstance clientInstance = client.getHazelcastInstance();

		IMap<String, String> datalakeMap = clientInstance.getMap("datalakeMap");
		IMap<String, Metadata> metadataDatamartMap = clientInstance.getMap("metadataDatamartMap");
		IMap<String, List<WordOccurrence>> wordDatamartMap = clientInstance.getMap("wordDatamartMap");

		WordCleanerManager wordCleaner = new WordCleaner();
		MetadataExtractorManager metadataExtractor = new MetadataExtractor();
		MetadataStoreManager metadataStoreMap = new MetadataStoreMap(metadataDatamartMap);
		WordExtractorManager wordExtractor = new WordExtractor(wordCleaner);
		WordStoreManager wordStoreMap = new WordStoreMap(wordDatamartMap);
		Indexer indexer = new Indexer(wordStoreMap, metadataStoreMap, metadataExtractor, wordExtractor);

		datalakeMap.addEntryListener(new CustomEntryListener<String, String>(indexer), true);


	}
}




