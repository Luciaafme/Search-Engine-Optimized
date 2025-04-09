package control;

import control.interfaces.Filter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LanguageFilter implements Filter {
	@Override
	public boolean applyFilter(String content) {
		Pattern languagePattern = Pattern.compile("(?i)Language\\s*:\\s*(\\w+)", Pattern.MULTILINE);
		Matcher matcher = languagePattern.matcher(content);

		if (matcher.find()) {
			String language = matcher.group(1).trim();
			//System.out.println("Detected language: " + language);

			return !(language.equalsIgnoreCase("Spanish") || language.equalsIgnoreCase("English") || language.equalsIgnoreCase("French"));
		} else {
			//System.out.println("No language field found.");
		}
		return true;
	}
}
