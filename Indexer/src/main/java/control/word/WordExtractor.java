package control.word;

import control.interfaces.WordCleanerManager;
import control.interfaces.WordExtractorManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordExtractor implements WordExtractorManager {

	private final WordCleanerManager wordCleaner;

	public WordExtractor(WordCleanerManager wordCleaner) {
		this.wordCleaner = wordCleaner;
	}

	@Override
	public Map<String, List<Integer>> getWords(String bookContent, String bookID) {

		Map<String, List<Integer>> indexedWordMap = new HashMap<>();

		String regex = "(?i)\\*\\*\\* START OF THE PROJECT GUTENBERG EBOOK .*? \\*\\*\\*(.*?)\\*\\*\\* END OF THE PROJECT GUTENBERG EBOOK .*? \\*\\*\\*";
		Pattern pattern = Pattern.compile(regex, Pattern.DOTALL);
		Matcher matcher = pattern.matcher(bookContent);

		if (matcher.find()) {
			String extractedContent = matcher.group(1).trim();
			String[] lines = extractedContent.split("\n");
			int lineNumber = 0;

			for (String line : lines) {
				processLine(line, lineNumber, indexedWordMap);
				lineNumber++;
			}
		} else {
			System.out.println("WARNING: Delimiters not found for book ID: " + bookID);
		}

		return indexedWordMap;
	}

	private void processLine(String line, int lineNumber, Map<String, List<Integer>> indexedWordMap) {
		String[] words = line.replaceAll("[^a-zA-Z0-9\s]", "").toLowerCase().split("\s+");
		for (String word : words) {
			String cleanWord = wordCleaner.execute(word);
			if (cleanWord == null || cleanWord.isEmpty()) continue;

			update(cleanWord, lineNumber, indexedWordMap);
		}
	}

	private void update(String cleanWord, int lineNumber, Map<String, List<Integer>> indexedWordMap) {
		indexedWordMap.computeIfAbsent(cleanWord, k -> new ArrayList<>()).add(lineNumber);
	}
}


