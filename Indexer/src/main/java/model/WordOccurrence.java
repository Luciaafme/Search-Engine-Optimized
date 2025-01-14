package model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class WordOccurrence implements Serializable {
	private static final long serialVersionUID = 1L;

	private final String bookID;
	private final List<Integer> lineNumbers;

	public WordOccurrence(String bookID, List<Integer> lineNumbers) {
		this.bookID = bookID;
		this.lineNumbers = lineNumbers;
	}

	public String getBookID() {
		return bookID;
	}

	public List<Integer> getLineNumbers() {
		return lineNumbers;
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

	@Override
	public String toString() {
		return "WordOccurrence{" +
				"bookID='" + bookID + '\'' +
				", lineNumbers=" + lineNumbers +
				'}';
	}
}