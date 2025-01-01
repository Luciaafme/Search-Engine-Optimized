package control;

import control.interfaces.QueryEngineManager;

import java.util.HashMap;
import java.util.Map;

public class BookController {

	private String WORDS_DATAMART_PATH;
	private String DATALAKE_PATH;
	private String METADATA_FILE_PATH;

	private QueryEngineManager queryEngine;

	public BookController(String WORDS_DATAMART_PATH, String DATALAKE_PATH, String METADATA_FILE_PATH, QueryEngineManager queryEngine) {
		this.WORDS_DATAMART_PATH = WORDS_DATAMART_PATH;
		this.DATALAKE_PATH = DATALAKE_PATH;
		this.METADATA_FILE_PATH = METADATA_FILE_PATH;
		this.queryEngine = queryEngine;
	}

	public Map<String, Object> searchWords(String phrase) {
		Map<String, Object> response = new HashMap<>();


		//response.put("results", wordResults);
		return response;
	}
}
