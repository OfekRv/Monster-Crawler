package monsterCrawler.bl.crawlers.articles;

import monsterCrawler.entities.Group;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Named;

import static monsterCrawler.utils.CrawelersUtils.encodeUrl;
import static monsterCrawler.utils.CrawelersUtils.getFirstElementByClass;

@Named
public class GbhackersArticleCrawler extends AbstractArticlesCrawler<Group> implements GroupArticlesCrawler {
	private static final String SEARCH_QUERY = "?s=";
	private static final String PAGE = "page/";

	@Value("${GBHACKERS_URL}")
	private String gbhackersUrl;

	@Override
	public String buildUrl(String name) {
		return gbhackersUrl + SEARCH_QUERY + '"' + encodeUrl(name) + '"';
	}

	@Override
	public String buildSearchUrl(String name, int currentPage) {
		return gbhackersUrl + PAGE + currentPage + "/" + SEARCH_QUERY + '"' + encodeUrl(name) + '"';
	}

	@Override
	public String extractTitle(Element article) {
		return article.selectFirst("h3").text();
	}

	@Override
	public Elements extractArticlesElements(Document doc) {
		Element articlesContainer= getFirstElementByClass(doc,"td-ss-main-content");
		if (articlesContainer==null)
		{
			return new Elements();
		}
		return articlesContainer.getElementsByClass("td_module_10 td_module_wrap td-animation-stack");
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
