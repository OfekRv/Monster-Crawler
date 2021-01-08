package monsterCrawler.bl.crawlers.articles;

import static monsterCrawler.utils.CrawelersUtils.EMPTY;
import static monsterCrawler.utils.CrawelersUtils.encodeUrl;
import static monsterCrawler.utils.CrawelersUtils.getFirstElementByClass;

import javax.inject.Named;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;

import monsterCrawler.entities.Group;

@Named
public class DarkReadingArticleCrawler extends AbstractArticlesCrawler<Group> implements GroupArticlesCrawler {
	private static final String SEARCH_QUERY = "search.asp?q=";

	@Value("${DARK_READING_URL}")
	private String darkReadingUrl;

	@Override
	public String buildUrl(String name) {
		return darkReadingUrl + SEARCH_QUERY + '"' + encodeUrl(name) + '"';
	}

	@Override
	public String buildSearchUrl(String name, int currentPage) {
		return EMPTY;
	}

	@Override
	public String extractTitle(Element article) {
		return article.selectFirst("a").text();
	}

	@Override
	public Elements extractArticlesElements(Document doc) {
		return doc.select("dt");
	}

	@Override
	public String extractArticleDate(Element article) {
		return getFirstElementByClass(article, "date").text().split(" ")[0];
	}

	@Override
	public String getDateFormatPattern() {
		return "M/d/yyyy";
	}

	@Override
	public int getFirstSearchPageIndex() {
		return 1;
	}

	@Override
	public void crawlNextPagesArticles(Group entity, String name) {
		// No need
	}
}
