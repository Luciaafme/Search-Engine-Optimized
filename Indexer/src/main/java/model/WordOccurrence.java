package model;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class WordOccurrence implements DataSerializable {

	private String bookID;
	private Set<Integer> lineOccurrences;

	// Constructor por defecto requerido por Hazelcast
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
	public void writeData(ObjectDataOutput out) throws IOException {
		out.writeUTF(bookID);
		out.writeInt(lineOccurrences.size());
		for (Integer line : lineOccurrences) {
			out.writeInt(line);
		}
	}

	@Override
	public void readData(ObjectDataInput in) throws IOException {
		bookID = in.readUTF();
		int size = in.readInt();
		lineOccurrences = new HashSet<>();
		for (int i = 0; i < size; i++) {
			lineOccurrences.add(in.readInt());
		}
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
				", lineOccurrences=" + lineOccurrences +
				'}';
	}
}
