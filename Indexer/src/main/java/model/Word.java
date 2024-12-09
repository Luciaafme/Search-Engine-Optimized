package model;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Word implements DataSerializable {

	private String text;
	private Set<WordOccurrence> occurrences;

	// Default constructor required by Hazelcast
	public Word() {
		this.occurrences = new HashSet<>();
	}

	public Word(String text, WordOccurrence occurrence) {
		this.text = text;
		this.occurrences = new HashSet<>();
		this.occurrences.add(occurrence);
	}

	public String getText() {
		return text;
	}

	public Set<WordOccurrence> getOccurrences() {
		return occurrences;
	}

	public void addOccurrence(WordOccurrence newOccurrence) {
		occurrences.add(newOccurrence);
	}

	@Override
	public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
		objectDataOutput.writeUTF(text);
		objectDataOutput.writeInt(occurrences.size());
		for (WordOccurrence occurrence : occurrences) {
			occurrence.writeData(objectDataOutput);
		}
	}

	@Override
	public void readData(ObjectDataInput objectDataInput) throws IOException {
		text = objectDataInput.readUTF();
		int size = objectDataInput.readInt();
		occurrences = new HashSet<>();
		for (int i = 0; i < size; i++) {
			WordOccurrence occurrence = new WordOccurrence();
			occurrence.readData(objectDataInput);
			occurrences.add(occurrence);
		}
	}

	@Override
	public String toString() {
		return "Word{" +
				"text='" + text + '\'' +
				", occurrences=" + occurrences +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Word word = (Word) o;
		return Objects.equals(this.text, word.text);
	}

	@Override
	public int hashCode() {
		return Objects.hash(this.text);
	}


	public static class WordOccurrence implements DataSerializable {

		private String bookID;
		private Set<Integer> lineOccurrences;

		// Default constructor required by Hazelcast
		public WordOccurrence() {
			this.lineOccurrences = new HashSet<>();
		}

		public WordOccurrence(String bookID, int lineNumber) {
			this.bookID = bookID;
			this.lineOccurrences = new HashSet<>();
			this.lineOccurrences.add(lineNumber);
		}

		public String getBookID() {
			return bookID;
		}

		public Set<Integer> getLineOccurrences() {
			return lineOccurrences;
		}

		public void addLineOccurrence(int lineNumber) {
			this.lineOccurrences.add(lineNumber);
		}

		@Override
		public void writeData(ObjectDataOutput objectDataOutput) throws IOException {
			objectDataOutput.writeUTF(bookID);
			objectDataOutput.writeInt(lineOccurrences.size());
			for (int line : lineOccurrences) {
				objectDataOutput.writeInt(line);
			}
		}

		@Override
		public void readData(ObjectDataInput objectDataInput) throws IOException {
			bookID = objectDataInput.readUTF();
			int size = objectDataInput.readInt();
			lineOccurrences = new HashSet<>();
			for (int i = 0; i < size; i++) {
				lineOccurrences.add(objectDataInput.readInt());
			}
		}

		@Override
		public String toString() {
			return "WordOccurrence{" +
					"bookID='" + bookID + '\'' +
					", lineOccurrences=" + lineOccurrences +
					'}';
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			WordOccurrence that = (WordOccurrence) o;
			return Objects.equals(bookID, that.bookID);
		}

		@Override
		public int hashCode() {
			return Objects.hash(bookID);
		}


	}
}
