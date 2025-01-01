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

import java.io.IOException;
import java.util.List;

public class IndexerMain {
	public static void main(String[] args) throws IOException {

		HazelcastServer server = new HazelcastServer();
		HazelcastInstance hazelcastInstance = server.getHazelcastInstance();

		IMap<String, Metadata> metadataMap = hazelcastInstance.getMap("metadataMap");
		IMap<String, List<WordOccurrence>> wordMap = hazelcastInstance.getMap("wordDatamartMap");
		IMap<String, String> datalakeMap = hazelcastInstance.getMap("datalakeMap");

		//server.shutdown();

		// Componentes
		WordCleanerManager wordCleaner = new WordCleaner();
		MetadataExtractorManager metadataExtractor = new MetadataExtractor();
		MetadataStoreManager metadataStoreMap = new MetadataStoreMap(metadataMap);
		WordExtractorManager wordExtractor = new WordExtractor(wordCleaner);
		WordStoreManager wordStoreMap = new WordStoreMap(wordMap);
		Indexer indexer = new Indexer(wordStoreMap, metadataStoreMap, metadataExtractor, wordExtractor);

		// Agregar listener para procesar entradas nuevas en datalakeMap
		datalakeMap.addEntryListener(new CustomEntryListener<String, String>(indexer), true);


	}
}




