package control;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import control.interfaces.Downloader;
import control.interfaces.Filter;

import java.io.IOException;

public class CrawlerMain {

	public static void main(String[] args) {

		// In your client modules
		HazelcastClientManager client = new HazelcastClientManager();
		HazelcastInstance clientInstance = client.getHazelcastInstance();
		IMap<String, String> datalakeMap = clientInstance.getMap("datalakeMap");

		//client.shutdown();

		System.out.println(args[0]);
		String datalakeFolderPath = args[0];

		int numBooks = 0;
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


