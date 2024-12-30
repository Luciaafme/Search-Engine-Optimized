package control;

import com.hazelcast.config.Config;
import com.hazelcast.config.SerializerConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.StreamSerializer;
import control.interfaces.*;
import control.metadata.MetadataExtractor;
import control.metadata.MetadataStoreMap;
import control.word.CustomEntryListener;
import control.word.WordCleaner;
import control.word.WordExtractor;
import control.word.WordStoreMap;
import model.Metadata;
import model.Word;
import model.WordOccurrence;

import java.io.IOException;
import java.util.Set;

public class IndexerMain {
	public static void main(String[] args) throws IOException {

		HazelcastServer server = new HazelcastServer();
		HazelcastInstance hazelcastInstance = server.getHazelcastInstance();

		IMap<String, Metadata> metadataMap = hazelcastInstance.getMap("metadataMap");
		IMap<String, Set<WordOccurrence>> wordMap = hazelcastInstance.getMap("wordDatamartMap");
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
		datalakeMap.addEntryListener(new CustomEntryListener(indexer), true);


	}
}




