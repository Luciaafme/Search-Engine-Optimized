package control;

import control.interfaces.Downloader;
import control.interfaces.Filter;

import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		String datalakePath = "";

		int numBooks = 1;
		Filter filter = new LanguageFilter();
		Downloader downloader = new BookDownloader(filter);
		WebCrawlerController controller = new WebCrawlerController(downloader, datalakePath, numBooks);

		try {
			controller.upload(datalakePath);
			controller.execute();
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}
}


