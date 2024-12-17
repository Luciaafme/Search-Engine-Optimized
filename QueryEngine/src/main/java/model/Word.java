package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Word {
	private String text;  // Text of the word
	private List<WordOccurrence> occurrences;  // List of WordOccurrence objects

	public Word(String text, WordOccurrence[] occurrences) {
		this.text = text;
		this.occurrences = new ArrayList<>(Arrays.asList(occurrences));
	}

	public String getText() {
		return text;
	}

	public WordOccurrence[] getOccurrences() {
		return occurrences.toArray(new WordOccurrence[0]);
	}

	public void addOccurrence(WordOccurrence newOccurrence) {
		occurrences.add(newOccurrence);
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("Word: " + text + "\nOccurrences:\n");
		for (WordOccurrence occurrence : occurrences) {
			sb.append(occurrence.toString()).append("\n");
		}
		return sb.toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Word word = (Word) obj;
		return Objects.equals(text, word.text);
	}

	@Override
	public int hashCode() {
		return Objects.hash(text);
	}

	public static class WordOccurrence implements Serializable {
		private final String book_id;
		private final List<Integer> lineOccurrences;
		private final int frequency;

		public WordOccurrence(String book_id, List<Integer> lineOccurrences) {
			this.book_id = book_id;
			this.lineOccurrences = lineOccurrences;
			this.frequency = lineOccurrences.size();
		}

		public String getBook_id() {
			return book_id;
		}

		public List<Integer> getLineOccurrences() {
			return lineOccurrences;
		}

		public int getFrequency() {
			return frequency;
		}

		@Override
		public String toString() {
			return "Book ID: " + book_id + "\n" +
					"Lines: " + lineOccurrences + "\n" +
					"Frequency: " + frequency + "\n";
		}
	}
}
