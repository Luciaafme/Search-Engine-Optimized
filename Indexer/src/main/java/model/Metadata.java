package model;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.DataSerializable;

import java.io.IOException;

public class Metadata implements DataSerializable {

	private String bookID;
	private String title;
	private String author;
	private String year;
	private String language;
	private String downloadLink;

	public Metadata() {
	}

	public Metadata(String bookID, String title, String author, String year, String language, String downloadLink) {
		this.bookID = bookID;
		this.title = title;
		this.author = author;
		this.year = year;
		this.language = language;
		this.downloadLink = downloadLink;
	}

	public String getBookID() {
		return bookID;
	}

	@Override
	public String toString() {
		return "Metadata {" +
				"BookID='" + bookID + '\'' +
				", Title='" + title + '\'' +
				", Author='" + author + '\'' +
				", Year='" + year + '\'' +
				", Language='" + language + '\'' +
				", DownloadLink='" + downloadLink + '\'' +
				'}';
	}

	@Override
	public void writeData(ObjectDataOutput out) throws IOException {
		out.writeUTF(bookID);
		out.writeUTF(title);
		out.writeUTF(author);
		out.writeUTF(year);
		out.writeUTF(language);
		out.writeUTF(downloadLink);
	}

	@Override
	public void readData(ObjectDataInput in) throws IOException {
		this.bookID = in.readUTF();
		this.title = in.readUTF();
		this.author = in.readUTF();
		this.year = in.readUTF();
		this.language = in.readUTF();
		this.downloadLink = in.readUTF();
	}
}
