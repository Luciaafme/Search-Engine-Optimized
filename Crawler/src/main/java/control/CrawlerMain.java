package control;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import control.interfaces.Downloader;
import control.interfaces.Filter;

import java.io.IOException;

public class CrawlerMain {

	public static void main(String[] args) {

		HazelcastManager client = new HazelcastManager();
		HazelcastInstance clientInstance = client.getHazelcastInstance();
		IMap<String, String> datalakeMap = clientInstance.getMap("datalakeMap");

		String datalakeFolderPath = System.getenv("datalakePath");

		int numBooks = Integer.parseInt(System.getenv("nBooks"));
		Filter filter = new LanguageFilter();
		Downloader downloader = new BookDownloader(filter);
		WebCrawlerController controller = new WebCrawlerController(downloader, datalakeMap, datalakeFolderPath, numBooks);

		try {
			controller.uploadBookToMap(datalakeFolderPath);
			controller.execute();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}


