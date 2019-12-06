package monsterCrawler.bl.crawlers.articles;

import static monsterCrawler.utils.CrawelersUtils.encodeUrl;
import static monsterCrawler.utils.CrawelersUtils.getFirstElementByClass;

import javax.inject.Named;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;

import monsterCrawler.entities.Group;

@Named
public class CyberSecurityReviewArticleCrawler extends AbstractArticlesCrawler<Group> implements GroupArticlesCrawler {
	private static final String SEARCH_QUERY = "?s=";
	private static final String PAGE = "page/";

	@Value("${CYBER_SECURITY_REVIEW_URL}")
	private String cyberDefenceMagazineUrl;

	@Override
	public String buildUrl(String name) {
		return cyberDefenceMagazineUrl + SEARCH_QUERY + '"' + encodeUrl(name) + '"';
	}

	@Override
	public String buildSearchUrl(String name, int currentPage) {
		return cyberDefenceMagazineUrl + PAGE + currentPage + "/" + SEARCH_QUERY + '"' + encodeUrl(name) + '"';
	}

	@Override
	public String extractTitle(Element article) {
		return getFirstElementByClass(article, "entry-title").text();
	}

	@Override
	public Elements extractArticlesElements(Document doc) {
		return doc.select("article");
	}

	@Override
	public String extractArticleDate(Element article) {
		return article.selectFirst("time").text();
	}

	@Override
	public String getDateFormatPattern() {
		return "MMMM d, yyyy";
	}

	@Override
	public int getFirstSearchPageIndex() {
		return 2;
	}
}
