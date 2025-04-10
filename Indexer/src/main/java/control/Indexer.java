package control;


import control.interfaces.MetadataExtractorManager;
import control.interfaces.MetadataStoreManager;
import control.interfaces.WordExtractorManager;
import control.interfaces.WordStoreManager;
import model.Metadata;

import java.util.List;
import java.util.Map;


public class Indexer {

	private final WordStoreManager wordStoreManager;
	private final MetadataStoreManager metadataStoreManager;
	private final MetadataExtractorManager metadataExtractor;
	private final WordExtractorManager wordExtractor;

	public Indexer(WordStoreManager wordStoreManager,
				   MetadataStoreManager metadataStoreManager,
				   MetadataExtractorManager metadataExtractor,
				   WordExtractorManager wordExtractor) {
		this.wordStoreManager = wordStoreManager;
		this.metadataStoreManager = metadataStoreManager;
		this.metadataExtractor = metadataExtractor;
		this.wordExtractor = wordExtractor;
	}

	public void execute(String bookID, String bookContent) {
		try {

			Metadata metadata = metadataExtractor.getMetadata(bookContent, String.valueOf(bookID));
			metadataStoreManager.update(metadata);
			Map<String, List<Integer>> indexedWordMap = wordExtractor.getWords(bookContent, metadata.getBookID());
			wordStoreManager.update(bookID, indexedWordMap);
			System.out.println("Finished indexing book with id: " + bookID);


		} catch (Exception e) {
			System.err.println("Error processing books.");
			e.printStackTrace();
		}


	}

}






