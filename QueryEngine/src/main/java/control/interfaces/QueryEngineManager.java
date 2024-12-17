package control.interfaces;

import java.util.Map;

public interface QueryEngineManager {

	public Map<String, Object> printResultsAsMap(String wordsDatamartPath, String datalakePath, String metadataFilePath, String word);

}



