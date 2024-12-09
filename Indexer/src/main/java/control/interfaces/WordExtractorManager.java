package control.interfaces;

import model.Word;

import java.io.IOException;
import java.util.Set;

public interface WordExtractorManager {
	Set<Word> getWords(String content, String identifier) throws IOException;
}
