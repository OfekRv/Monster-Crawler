package mitreCrawler.bl.crawlers.articles;

import static utils.CrawelersUtils.encodeUrl;
import static utils.CrawelersUtils.getFirstElementByClass;

import java.io.IOException;

import javax.inject.Named;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;

import mitreCrawler.entities.Group;

@Named
public class ThreatPostArticleCrawler extends AbstractArticlesCrawler<Group> {
	private static final String SEARCH = "?s=";
	private static final String API_URL = "wp-admin/admin-ajax.php";

	@Value("${THREAT_POST_URL}")
	private String ThreatPostUrl;

	@Override
	public String buildUrl(Group entity) {
		return ThreatPostUrl + SEARCH + '"' + entity.getName().replace(" ", "%20") + '"';
	}

	@Override
	public String buildSearchUrl(Group entity, int currentPage) {
		return ThreatPostUrl + API_URL;
	}

	@Override
	public Elements extractArticlesElements(Document doc) {
		return doc.select("article");
	}

	@Override
	public String extractTitle(Element article) {
		return getFirstElementByClass(article, "c-card__title").text();
	}

	@Override
	public String extractArticleDate(Element article) {
		return getFirstElementByClass(article, "time").attr("datetime").split("T")[0];
	}

	@Override
	public String getDateFormatPattern() {
		return "yyyy-MM-dd";
	}

	@Override
	public Elements loadAndExtractNextArticles(Group entity) throws IOException {
		int currentPage = getFirstSearchPageIndex();
		Elements articlesElements = new Elements();
		Document doc;
		do {
			doc = Jsoup.connect(buildSearchUrl(entity, currentPage)).data("action", "loadmore")
					.data("query", encodeUrl("{\"s\":\"" + entity.getName() + "\",\"order\":\"DESC\"}"))
					.data("page", Integer.toString(currentPage++)).post();

			articlesElements.addAll(extractArticlesElements(doc));
		} while (doc.hasText());

		return articlesElements;
	}

	@Override
	public int getFirstSearchPageIndex() {
		return 1;
	}
}
