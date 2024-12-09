package control.metadata;

import control.interfaces.MetadataExtractorManager;
import model.Metadata;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MetadataExtractor implements MetadataExtractorManager {

	@Override
	public Metadata getMetadata(String inputString, String bookID)  {

		// Patterns to match metadata fields
		Pattern titlePattern = Pattern.compile("(?i)^Title:?\\s*(.+)$", Pattern.MULTILINE);
		Pattern authorPattern = Pattern.compile("(?i)^Author:?\\s*(.+)$", Pattern.MULTILINE);
		Pattern languagePattern = Pattern.compile("(?i)^Language:?\\s*(.+)$", Pattern.MULTILINE);
		Pattern yearPattern = Pattern.compile("Release date:.*?(\\b\\d{4}\\b)");

		// Match metadata fields
		Matcher titleMatcher = titlePattern.matcher(inputString);
		Matcher authorMatcher = authorPattern.matcher(inputString);
		Matcher languageMatcher = languagePattern.matcher(inputString);
		Matcher yearMatcher = yearPattern.matcher(inputString);

		// Extract metadata values
		String title = titleMatcher.find() ? titleMatcher.group(1).trim() : "UNKNOWN";
		String author = authorMatcher.find() ? authorMatcher.group(1).trim() : "UNKNOWN";
		String language = languageMatcher.find() ? languageMatcher.group(1).trim() : "UNKNOWN";
		String year = yearMatcher.find() ? yearMatcher.group(1).trim() : "UNKNOWN";
		String downloadLink = "https://www.gutenberg.org/cache/epub/" + bookID + "/pg" + bookID + ".txt";

		return new Metadata(bookID, title, author, year, language, downloadLink);
	}
}
