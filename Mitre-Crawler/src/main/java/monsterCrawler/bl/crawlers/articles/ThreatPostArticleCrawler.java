package monsterCrawler.bl.crawlers.articles;

import static monsterCrawler.utils.CrawelersUtils.createConnection;
import static monsterCrawler.utils.CrawelersUtils.encodeUrl;
import static monsterCrawler.utils.CrawelersUtils.getFirstElementByClass;

import java.io.IOException;

import javax.inject.Named;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;

import monsterCrawler.entities.Group;

//@Named
public class ThreatPostArticleCrawler extends AbstractArticlesCrawler<Group> {
	private static final String SEARCH = "?s=";
	private static final String API_URL = "wp-admin/admin-ajax.php";

	@Value("${THREAT_POST_URL}")
	private String ThreatPostUrl;

	@Override
	public String buildUrl(Group entity) {
		return ThreatPostUrl + SEARCH + '"' + encodeUrl(entity.getName()) + '"';
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
		return article.select("time").attr("datetime").split("T")[0];
	}

	@Override
	public String getDateFormatPattern() {
		return "yyyy-MM-dd";
	}

	@Override
	public Document getSearchPage(Group entity, int pageIndex) throws IOException {
		return createConnection(buildSearchUrl(entity, pageIndex)).data("action", "loadmore")
				.data("query", encodeUrl("{\"s\":\"" + entity.getName() + "\",\"order\":\"DESC\"}"))
				.data("page", Integer.toString(pageIndex)).post();
	}

	@Override
	public int getFirstSearchPageIndex() {
		return 1;
	}
}
