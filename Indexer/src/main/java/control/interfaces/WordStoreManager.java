package control.interfaces;

import model.Word;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface WordStoreManager {

	void update(String bookID, Map<String, List<Integer>> items) throws IOException;
	void printMap();

}
