package mitreCrawler.bl.crawlers.articles;

import javax.inject.Named;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;

import mitreCrawler.entities.Group;

// Not very stable source

@Named
public class CyberDefenceMagazineArticleCrawler extends AbstractArticlesCrawler<Group> {
	private static final String SEARCH_QUERY = "?s=";
	private static final String PAGE = "page/";

	@Value("${CYBER_DEFENCE_MAGAZINE_URL}")
	private String cyberDefenceMagazineUrl;

	@Override
	public String buildUrl(Group entity) {
		return cyberDefenceMagazineUrl + SEARCH_QUERY + '"' + entity.getName().replace(" ", "%20") + '"';
	}

	@Override
	public String buildSearchUrl(Group entity, int currentPage) {
		return cyberDefenceMagazineUrl + PAGE + currentPage++ + "/" + SEARCH_QUERY + '"'
				+ entity.getName().replace(" ", "%20") + '"';
	}

	@Override
	public String extractTitle(Element article) {
		return article.select("h3").first().text();
	}

	@Override
	public Elements extractArticlesElements(Document doc) {
		return doc.getElementsByClass("td_module_16 td_module_wrap td-animation-stack");
	}

	@Override
	public String extractArticleDate(Element article) {
		return article.select("time").first().text();
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
