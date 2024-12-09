package control;

import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import control.interfaces.*;
import control.metadata.MetadataExtractor;
import control.metadata.MetadataStoreSqlite;
import control.word.CustomEntryListener;
import control.word.WordCleaner;
import control.word.WordExtractor;
import control.word.WordStoreMap;

import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {



		// Hazelcast setup
		Config config = new XmlConfigBuilder("Indexer/src/main/resources/hazelcast.xml").build();
		HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);
		IMap<String, String> datalakeMap = hazelcastInstance.getMap("datalakeMap");


		// Components
		WordCleanerManager wordCleaner = new WordCleaner();
		MetadataExtractorManager metadataExtractor = new MetadataExtractor();
		MetadataStoreManager metadataStoreSqlite = new MetadataStoreSqlite("DATAMART PATH");
		WordExtractorManager wordExtractor = new WordExtractor(wordCleaner);
		WordStoreManager wordStoreMap = new WordStoreMap();
		Indexer indexer = new Indexer(wordStoreMap, metadataStoreSqlite, metadataExtractor, wordExtractor);
		datalakeMap.addEntryListener(new CustomEntryListener(indexer),true);


	}
}




