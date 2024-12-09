package control.word;


import control.interfaces.WordCleanerManager;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class WordCleaner implements WordCleanerManager {

	private final Set<String> stopwords = new HashSet<>(Arrays.asList(
			"el", "la", "los", "las", "un", "una", "unos", "unas", "de", "del", "a", "al", "con", "sin", "por", "para",
			"en", "entre", "sobre", "hasta", "ante", "bajo", "desde", "hacia", "durante", "mediante", "tras", "excepto",
			"y", "o", "pero", "porque", "cuando", "donde", "qué", "quién", "cómo", "cuál", "quiénes", "que", "este",
			"esta", "estos", "estas", "ese", "esos", "esa", "esas", "mi", "tu", "su", "nos", "vosotros", "ellos", "ellas",
			"yo", "tú", "usted", "nosotros", "ustedes", "the", "a", "an", "of", "for", "on", "in", "with", "by", "at", "to",
			"from", "up", "down", "through", "under", "between", "among", "until", "over", "about", "before", "after",
			"across", "behind", "below", "above", "around", "and", "or", "but", "because", "when", "where", "what",
			"who", "how", "which", "this", "that", "these", "those", "my", "your", "his", "her", "our", "their", "you",
			"they", "it", "we", "he", "she", "me", "us", "him", "her", "them", "le", "la", "les", "un", "une", "des",
			"du", "de", "à", "au", "aux", "avec", "sans", "pour", "par", "dans", "sur", "entre", "jusqu’à", "avant",
			"après", "derrière", "devant", "chez", "sous", "vers", "pendant", "parmi", "sauf", "et", "ou", "mais",
			"parce que", "quand", "où", "quoi", "qui", "comment", "quel", "quelle", "ce", "cette", "ces", "mon", "ton",
			"son", "notre", "votre", "leur", "ils", "elles", "nous", "vous", "je", "tu", "il", "elle", "on", "nous",
			"vous", "ils", "elles", "ll"
	));

	public WordCleaner() {
	}

	@Override
	public String execute(String word) {
		word = word.toLowerCase();

		word = word.replaceAll("https?://\\S+|www\\.[^\\s]+", "");

		word = word.replaceAll("\\b[a-zA-Z]*'([a-zA-Z]+)\\b", "$1");

		word = word.replaceAll("[^\\p{L}]", "");

		if (!stopwords.contains(word) && word.length() > 1) {
			return word;
		} else {
			return null;
		}
	}


}
