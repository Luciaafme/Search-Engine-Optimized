package control.word;

import control.interfaces.WordCleanerManager;
import control.interfaces.WordExtractorManager;
import model.Word;
import model.WordOccurrence;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WordExtractor implements WordExtractorManager {

	private final WordCleanerManager wordCleaner;

	public WordExtractor(WordCleanerManager wordCleaner) {
		this.wordCleaner = wordCleaner;
	}

	@Override
	public Set<Word> getWords(String bookContent, String bookID) throws IOException {
		Set<Word> indexedWordSet = new HashSet<>();

		String regex = "(?i)\\*\\*\\* START OF THE PROJECT GUTENBERG EBOOK .*? \\*\\*\\*(.*?)\\*\\*\\* END OF THE PROJECT GUTENBERG EBOOK .*? \\*\\*\\*";
		Pattern pattern = Pattern.compile(regex, Pattern.DOTALL); // DOTALL permite capturar contenido en múltiples líneas
		Matcher matcher = pattern.matcher(bookContent);

		if (matcher.find()) {
			String extractedContent = matcher.group(1).trim();
			String[] lines = extractedContent.split("\n");
			int lineNumber = 0;

			for (String line : lines) {
				processLine(line, bookID, lineNumber, indexedWordSet);
				lineNumber++;
			}
		} else {
			System.out.println("WARNING: Delimiters not found for book ID: " + bookID);
		}

		return indexedWordSet;
	}


	private void processLine(String line, String bookID, int lineNumber, Set<Word> indexedWordSet) {
		String[] words = line.replaceAll("[^a-zA-Z0-9\\s]", "").toLowerCase().split("\\s+");
		for (String word : words) {
			String cleanWord = wordCleaner.execute(word);
			if (cleanWord == null || cleanWord.isEmpty()) continue;

			update(cleanWord, bookID, lineNumber, indexedWordSet);
		}
	}

	private void update(String cleanWord, String bookID, int lineNumber, Set<Word> indexedWordSet) {
		Word targetWord = indexedWordSet.stream()
				.filter(w -> w.getText().equals(cleanWord))
				.findFirst()
				.orElse(null);

		if (targetWord != null) {
			WordOccurrence targetOccurrence = targetWord.getOccurrences().stream()
					.filter(occurrence -> occurrence.getBookID().equals(bookID))
					.findFirst()
					.orElse(null);

			if (targetOccurrence != null) {
				targetOccurrence.addLineOccurrence(lineNumber);
			} else {
				targetWord.addOccurrence(new WordOccurrence(bookID, lineNumber));
			}
		} else {
			indexedWordSet.add(new Word(cleanWord, new WordOccurrence(bookID, lineNumber)));
		}
	}
}


