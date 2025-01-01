package control.interfaces;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public interface WordStoreManager {

	void update(String bookID, Map<String, List<Integer>> items) throws IOException;

	void printMap();

}
