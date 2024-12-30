package model;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public class Metadata implements DataSerializable {

	private String bookID;
	private String name;
	private String author;
	private String year;
	private String language;
	private String downloadLink;
	private int bookStartLine;

	// Constructor vacío requerido por Hazelcast
	public Metadata() {
	}

	public Metadata(String bookID, String name, String author, String year, String language, String downloadLink, int bookStartLine) {
		this.bookID = bookID;
		this.name = name;
		this.author = author;
		this.year = year;
		this.language = language;
		this.downloadLink = downloadLink;
		this.bookStartLine = bookStartLine;
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
	public int getBookStartLine(){return bookStartLine;}

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

	@Override
	public void writeData(ObjectDataOutput out) throws IOException {
		out.writeUTF(bookID);
		out.writeUTF(name);
		out.writeUTF(author);
		out.writeUTF(year);
		out.writeUTF(language);
		out.writeUTF(downloadLink);
		out.writeInt(bookStartLine);
	}

	@Override
	public void readData(ObjectDataInput in) throws IOException {
		this.bookID = in.readUTF();
		this.name = in.readUTF();
		this.author = in.readUTF();
		this.year = in.readUTF();
		this.language = in.readUTF();
		this.downloadLink = in.readUTF();
		this.bookStartLine = in.readInt();
	}
}
