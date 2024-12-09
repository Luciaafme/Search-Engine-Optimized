package control.interfaces;

import model.Word;

import java.io.IOException;
import java.util.Set;

public interface WordStoreManager {

	void update(Set<Word> items) throws IOException;
	void printMap();

}
