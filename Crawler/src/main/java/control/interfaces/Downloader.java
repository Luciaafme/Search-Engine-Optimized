package control.interfaces;

import com.hazelcast.map.IMap;

import java.nio.file.Path;

public interface Downloader {

	boolean downloadAndUploadToHazelcast(int bookId, String urlString, Path datalakePath,IMap<String, String> booksMap);


}
