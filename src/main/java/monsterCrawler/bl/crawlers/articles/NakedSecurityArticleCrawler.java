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
public class NakedSecurityArticleCrawler extends AbstractArticlesCrawler<Group> implements GroupArticlesCrawler {
	private static final String SEARCH_QUERY = "?s=";
	private static final String PAGE = "page/";

	@Value("${NAKED_SECURITY_URL}")
	private String nakedSecurityUrl;

	@Override
	public String buildUrl(String name) {
		return nakedSecurityUrl + SEARCH_QUERY + '"' + encodeUrl(name) + '"';
	}

	@Override
	public String buildSearchUrl(String name, int currentPage) {
		return nakedSecurityUrl + PAGE + currentPage + "/" + SEARCH_QUERY + '"' + encodeUrl(name) + '"';
	}

	@Override
	public String extractTitle(Element article) {
		return article.selectFirst("h1").text();
	}

	@Override
	public Elements extractArticlesElements(Document doc) {
		return doc.select("main").select("article:not(.error-404)");
	}

	@Override
	public String extractArticleDate(Element article) {
		return getFirstElementByClass(article, "result-meta").text().substring(0, 11);
	}

	@Override
	public String getDateFormatPattern() {
		return "MMM dd yyyy";
	}

	@Override
	public int getFirstSearchPageIndex() {
		return 2;
	}
}
