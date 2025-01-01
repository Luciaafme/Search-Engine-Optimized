package control.interfaces;


import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface WordExtractorManager {
	Map<String, List<Integer>> getWords(String content, String identifier) throws IOException;
}
