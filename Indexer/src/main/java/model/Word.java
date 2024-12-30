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

	// Constructor por defecto requerido por Hazelcast
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
	public void writeData(ObjectDataOutput out) throws IOException {
		out.writeUTF(text);
		out.writeInt(occurrences.size());
		for (WordOccurrence occurrence : occurrences) {
			out.writeObject(occurrence);
		}
	}

	@Override
	public void readData(ObjectDataInput in) throws IOException {
		text = in.readUTF();
		int size = in.readInt();
		occurrences = new HashSet<>();
		for (int i = 0; i < size; i++) {
			occurrences.add(in.readObject());
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Word word = (Word) o;
		return Objects.equals(text, word.text);
	}

	@Override
	public int hashCode() {
		return Objects.hash(text);
	}

	@Override
	public String toString() {
		return "Word{" +
				"text='" + text + '\'' +
				", occurrences=" + occurrences +
				'}';
	}
}
