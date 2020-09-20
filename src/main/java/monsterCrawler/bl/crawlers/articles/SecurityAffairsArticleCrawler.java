package monsterCrawler.bl.crawlers.articles;

import static monsterCrawler.utils.CrawelersUtils.encodeUrl;
import static monsterCrawler.utils.CrawelersUtils.getElementsByClass;
import static monsterCrawler.utils.CrawelersUtils.getFirstElementByClass;

import java.util.Optional;

import javax.inject.Named;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;

import monsterCrawler.entities.Group;

@Named
public class SecurityAffairsArticleCrawler extends AbstractArticlesCrawler<Group> implements GroupArticlesCrawler {
	private static final String SEARCH_QUERY = "?s=";
	private static final String PAGE = "page/";

	@Value("${SECURITY_AFFAIRS_URL}")
	private String SecurityAffairsUrl;

	@Override
	public String buildUrl(String name) {
		return SecurityAffairsUrl + SEARCH_QUERY + '"' + encodeUrl(name) + '"';
	}

	@Override
	public String buildSearchUrl(String name, int currentPage) {
		return SecurityAffairsUrl + PAGE + currentPage + SEARCH_QUERY + '"' + encodeUrl(name) + '"';
	}

	@Override
	public String extractTitle(Element article) {
		return getFirstElementByClass(article, "post_header half").text();
	}

	@Override
	public Elements extractArticlesElements(Document doc) {
		Optional<Element> articleContainer = Optional.ofNullable(getFirstElementByClass(doc, "sidebar_content"));
		if (!articleContainer.isPresent()) {
			return new Elements();
		}
		return getElementsByClass(articleContainer.get(), "search_content");
	}

	@Override
	public String extractArticleDate(Element article) {
		return getFirstElementByClass(article, "post_detail large_space").text().split(" By")[0];
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
