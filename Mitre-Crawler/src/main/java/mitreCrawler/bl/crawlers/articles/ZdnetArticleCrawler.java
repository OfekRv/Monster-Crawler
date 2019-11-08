package mitreCrawler.bl.crawlers.articles;

import javax.inject.Named;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;

import mitreCrawler.entities.Group;

@Named
public class ZdnetArticleCrawler extends AbstractArticlesCrawler<Group> {
	private static final String SEARCH = "search/";
	private static final String SEARCH_QUERY = "?o=1&q=";

	@Value("${ZDNET_URL}")
	private String zdnetUrl;

	@Override
	public String buildUrl(Group entity) {
		return zdnetUrl + SEARCH + SEARCH_QUERY + '"' + entity.getName().replace(" ", "%20") + '"';
	}

	@Override
	public String buildSearchUrl(Group entity, int currentPage) {
		return zdnetUrl + SEARCH + "/" + (currentPage++) + "/" + SEARCH_QUERY + '"'
				+ entity.getName().replace(" ", "%20") + '"';
	}

	@Override
	public String extractTitle(Element article) {
		return article.select("h3").first().text();
	}

	@Override
	public Elements extractArticlesElements(Document doc) {
		return doc.select("article");
	}

	@Override
	public String extractArticleDate(Element article) {
		return article.getElementsByClass("meta").first().select("span").first().text();
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
