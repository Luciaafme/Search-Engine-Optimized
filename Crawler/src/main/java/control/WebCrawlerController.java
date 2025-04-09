package control;


import com.hazelcast.core.IMap;
import control.interfaces.CrawlerController;
import control.interfaces.Downloader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.stream.Stream;

public class WebCrawlerController implements CrawlerController {
	private final Downloader downloader;
	private final IMap<String, String> datalakeMap;
	private final Path datalakePath;
	private final Integer numBooks;

	public WebCrawlerController(Downloader downloader, IMap<String, String> datalakeMap, String datalakePath, Integer numBooks) {
		this.downloader = downloader;
		this.datalakePath = Path.of(datalakePath);
		this.numBooks = numBooks;
		this.datalakeMap = datalakeMap;
	}

	@Override
	public void execute() throws IOException, InterruptedException {
		Random random = new Random();
		int booksDownloaded = 0;
		while (booksDownloaded < numBooks) {
			int bookId = random.nextInt(99999);
			String urlString = "https://www.gutenberg.org/cache/epub/" + bookId + "/pg" + bookId + ".txt";

			boolean success = downloader.downloadAndUploadToHazelcast(bookId, urlString, datalakePath, datalakeMap);
			if (success) {
				booksDownloaded++;
				System.out.println("Books downloaded: " + booksDownloaded + "/" + numBooks);
			}
		}
	}


	public void uploadBookToMap(String datalakeDirectory) throws IOException {
		Path datalakePath = Paths.get(datalakeDirectory);

		try (Stream<Path> files = Files.list(datalakePath)) {
			files.forEach(file -> {
				try {
					String content = Files.readString(file, StandardCharsets.UTF_8);

					String bookId = file.getFileName().toString();

					datalakeMap.put(bookId, content);

					System.out.println("Book " + bookId + " uploaded to Hazelcast.");
				} catch (IOException | NumberFormatException e) {
					System.err.println("Error al procesar el archivo: " + file + ", Error: " + e.getMessage());
				}
			});
		}
	}
}
