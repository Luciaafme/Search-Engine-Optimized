package control;


import control.interfaces.MetadataExtractorManager;
import control.interfaces.MetadataStoreManager;
import control.interfaces.WordExtractorManager;
import control.interfaces.WordStoreManager;
import model.Metadata;
import model.Word;

import java.util.Set;

public class Indexer {

	private final WordStoreManager wordStoreManager;
	private final MetadataStoreManager metadataStoreManager;
	private final MetadataExtractorManager metadataExtractor;
	private final WordExtractorManager wordExtractor;

	public Indexer(WordStoreManager wordStoreManager, MetadataStoreManager metadataStoreManager,
				   MetadataExtractorManager metadataExtractor, WordExtractorManager wordExtractor) {
		this.wordStoreManager = wordStoreManager;
		this.metadataStoreManager = metadataStoreManager;
		this.metadataExtractor = metadataExtractor;
		this.wordExtractor = wordExtractor;
	}

	public void execute(String bookId, String  bookContent) {
		try {

			Metadata metadata = metadataExtractor.getMetadata(bookContent, String.valueOf(bookId));
			metadataStoreManager.update(metadata);

			Set<Word> wordSet = wordExtractor.getWords(bookContent, metadata.getBookID());
			wordStoreManager.update(wordSet);
			System.out.println("Finalizado el indexado del libro con ID: " + bookId);
			wordStoreManager.printMap();


		} catch (Exception e) {
			System.err.println("Error general procesando los libros.");
			e.printStackTrace();
		}


	}

}






