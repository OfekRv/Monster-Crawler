package utils;

import org.jsoup.nodes.Element;

public class CrawelersUtils {
	public static String extractUrl(Element e) {
		return e.select("a").first().absUrl("href");
	}

	public static String paddedWithSpaces(String text) {
		return " " + text + " ";
	}
}
