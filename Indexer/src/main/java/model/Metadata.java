package model;

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

	public String[] toList() {
		return new String[]{bookID, name, author, year, language, downloadLink};
	}
	@Override
	public String toString() {
		return "Metadata {" +
				"BookID='" + bookID + '\'' +
				", Title='" + name + '\'' +
				", Author='" + author + '\'' +
				", Year='" + year + '\'' +
				", Language='" + language + '\'' +
				", DownloadLink='" + downloadLink + '\'' +
				'}';
	}


}
