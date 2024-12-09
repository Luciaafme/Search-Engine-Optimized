package control.interfaces;

import model.Word;

import java.io.File;
import java.util.Set;

public interface WordSerializerManager {
	void serialize(File filePath, Set<Word.WordOccurrence> ocurrence);
}
