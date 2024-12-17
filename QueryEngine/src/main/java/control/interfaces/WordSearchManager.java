package control.interfaces;

import model.Word;

import java.util.List;

public interface WordSearchManager {
	Word searchBook(String wordsDatamartPath, String word);

	public List<String> getPreviewLines(String datalakePath, String idBook, List<Integer> lines);

}
