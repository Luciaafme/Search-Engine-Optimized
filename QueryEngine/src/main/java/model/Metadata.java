package model;

import java.util.Objects;

public class Metadata {

	private final String bookID;
	private final String name;
	private final String author;
	private final String year;
	private final String language;
	private final String downloadLink;

	public Metadata(String bookID, String name, String author, String year, String language, String downloadLink) {
		this.bookID = bookID;
		this.name = name;
		this.author = author;
		this.year = year;
		this.language = language;
		this.downloadLink = downloadLink;
	}

	public String getBookID() {
		return bookID;
	}

	public String getName() {
		return name;
	}

	public String getAuthor() {
		return author;
	}

	public String getYear() {
		return year;
	}

	public String getLanguage() {
		return language;
	}

	public String getDownloadLink() {
		return downloadLink;
	}

	@Override
	public String toString() {
		return bookID + "\n" +
				"    " + name + "\n" +
				"    " + author + "\n" +
				"   " + year + "\n" +
				"   " + language + "\n" +
				"   " + downloadLink + "\n";
	}

	@Override
	public int hashCode() {
		return Objects.hash(bookID);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null || getClass() != obj.getClass()) return false;
		Metadata other = (Metadata) obj;
		return Objects.equals(bookID, other.bookID);
	}

}
