package utils;

import java.io.IOException;
import java.net.URLEncoder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class CrawelersUtils {
	public static Document getRequest(String url) throws IOException {
		return Jsoup.connect(url).get();
	}

	public static Document getRequestIgnoringBadStatusCode(String url) throws IOException {
		return Jsoup.connect(url).get();
	}

	public static Element getFirstElementByClass(Element e, String className) {
		return e.getElementsByClass(className).first();
	}

	public static String extractUrl(Element e) {
		return e.select("a").first().absUrl("href");
	}

	public static String encodeUrl(String url) {
		return URLEncoder.encode(url);
	}

	public static String paddedWithSpaces(String text) {
		return " " + text + " ";
	}
}
