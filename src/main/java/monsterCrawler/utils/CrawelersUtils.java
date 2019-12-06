package monsterCrawler.utils;

import java.io.IOException;
import java.net.URLEncoder;

import javax.inject.Named;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Named
public class CrawelersUtils {
	public static String EMPTY = "";

	public static Document getRequest(String url) throws IOException {
		Connection con = Jsoup.connect(url);
		return con.get();
	}

	public static Document getRequestIgnoringBadStatusCode(String url) throws IOException {
		return Jsoup.connect(url).ignoreHttpErrors(true).get();
	}

	public static Element getFirstElementByClass(Element e, String className) {
		return e.getElementsByClass(className).first();
	}

	public static String downloadAsCleanHtml(String url) {
		try {
			Document doc = getRequest(url);
			return doc.html();
		} catch (IOException e) {
			log.warn("[ARTICLE] Could not download \"" + url + "\" " + "mayble not exist or server error");
			return EMPTY;
		}
	}

	public static Connection createConnection(String url) {
		return Jsoup.connect(url);
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
