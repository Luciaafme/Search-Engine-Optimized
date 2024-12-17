package control;

import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import control.interfaces.*;
import control.metadata.MetadataExtractor;
import control.metadata.MetadataStoreMap;
import control.word.CustomEntryListener;
import control.word.WordCleaner;
import control.word.WordExtractor;
import control.word.WordStoreMap;

import java.io.IOException;

public class Main {
	public static void main(String[] args) throws IOException {


		// HazelCastSetup
		HazelcastManager hazelcastManager = new HazelcastManager();
		IMap<String, String> datalakeMap = hazelcastManager.getHazelcastInstance().getMap("datalakeMap");

		// Componentes
		WordCleanerManager wordCleaner = new WordCleaner();
		MetadataExtractorManager metadataExtractor = new MetadataExtractor();
		MetadataStoreManager metadataStoreMap = new MetadataStoreMap(hazelcastManager);
		WordExtractorManager wordExtractor = new WordExtractor(wordCleaner);
		WordStoreManager wordStoreMap = new WordStoreMap(hazelcastManager);
		Indexer indexer = new Indexer(wordStoreMap, metadataStoreMap, metadataExtractor, wordExtractor);

		// Agregar listener para procesar entradas nuevas en datalakeMap
		datalakeMap.addEntryListener(new CustomEntryListener(indexer), true);


	}
}




