package control.interfaces;

import model.Word;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface WordExtractorManager {
	Map<String, List<Integer>> getWords(String content, String identifier) throws IOException;
}
