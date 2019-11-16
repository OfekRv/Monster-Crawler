package monsterCrawler.bl.crawlers.articles;

import static monsterCrawler.utils.CrawelersUtils.EMPTY;
import static monsterCrawler.utils.CrawelersUtils.getFirstElementByClass;

import javax.inject.Named;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;

import monsterCrawler.entities.Group;

@Named
public class CywareArticlesCrawler extends AbstractArticlesCrawler<Group> {
	private static final String SEARCH = "search/";

	@Value("${CYWARE_URL}")
	private String cywareUrl;

	@Override
	public Elements extractArticlesElements(Document doc) {
		return doc.getElementsByClass("post-content");
	}

	@Override
	public Elements loadAndExtractNextArticles(Group entity) {
		// TODO
		return new Elements();
	}

	@Override
	public String buildSearchUrl(Group entity, int currentPage) {
		// TODO
		return EMPTY;
	}

	@Override
	public int getFirstSearchPageIndex() {
		// TODO
		return 0;
	}

	@Override
	public String extractTitle(Element article) {
		return article.selectFirst("img").attr("title");
	}

	@Override
	public String buildUrl(Group entity) {
		return cywareUrl + SEARCH + entity.getName();
	}

	@Override
	public String extractArticleDate(Element article) {
		return getFirstElementByClass(article, "date").text();
	}

	@Override
	public String getDateFormatPattern() {
		return "MMM d, yyyy";
	}
}
