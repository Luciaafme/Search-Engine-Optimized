package control;


import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import control.interfaces.CrawlerController;
import control.interfaces.Downloader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;
import java.util.stream.Stream;

public class WebCrawlerController implements CrawlerController {
	private final Downloader downloader;
	private final HazelcastInstance hazelcastInstance;
	private final IMap<String, String> booksMap;
	private final Path datalakePath;
	private final Integer numBooks;

	public WebCrawlerController(Downloader downloader, String datalakePath, Integer numBooks) {
		this.downloader = downloader;
		this.datalakePath = Path.of(datalakePath);
		this.numBooks = numBooks;
		try {
			this.hazelcastInstance = Hazelcast.newHazelcastInstance(
					new XmlConfigBuilder(new FileInputStream("Crawler/src/main/resources/hazelcast.xml")).build()
			);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}

		this.booksMap = hazelcastInstance.getMap("datalakeMap");

	}

	@Override
	public void execute() throws IOException, InterruptedException {
		Random random = new Random();
		int booksDownloaded = 0;

		while (booksDownloaded < numBooks) {
			int bookId = random.nextInt(99999);
			String urlString = "https://www.gutenberg.org/cache/epub/" + bookId + "/pg" + bookId + ".txt";

			boolean success = downloader.downloadAndUploadToHazelcast(bookId, urlString, datalakePath ,booksMap);
			if (success) {
				booksDownloaded++;
				System.out.println("Books downloaded: " + booksDownloaded);
			}
		}
	}


	public void upload(String datalakeDirectory) throws IOException {
		Path datalakePath = Paths.get(datalakeDirectory);

		// Leer los archivos del directorio
		try (Stream<Path> files = Files.list(datalakePath)) {
			files.forEach(file -> {
				try {
					// Leer contenido del archivo
					String content = Files.readString(file, StandardCharsets.UTF_8);

					// Extraer ID del libro del nombre del archivo
					String bookId = file.getFileName().toString();

					// Subir a Hazelcast
					booksMap.put(bookId, content);

					System.out.println("Libro " + bookId + " subido a Hazelcast.");
				} catch (IOException | NumberFormatException e) {
					System.err.println("Error al procesar el archivo: " + file + ", Error: " + e.getMessage());
				}
			});
		}
	}
}
