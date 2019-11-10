package monsterCrawler.bl.crawlers.articles;

import static monsterCrawler.utils.CrawelersUtils.encodeUrl;

import javax.inject.Named;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;

import monsterCrawler.entities.Group;

@Named
public class ScmMagazineArticleCrawler extends AbstractArticlesCrawler<Group> {
	private static final String SEARCH_QUERY = "?s=";
	private static final String PAGE = "page/";

	@Value("${SCM_MAGAZINE_URL}")
	private String scmMagazineUrl;

	@Override
	public String buildUrl(Group entity) {
		return scmMagazineUrl + SEARCH_QUERY + '"' + encodeUrl(entity.getName()) + '"';
	}

	@Override
	public String buildSearchUrl(Group entity, int currentPage) {
		return scmMagazineUrl + PAGE + currentPage + "/" + SEARCH_QUERY + '"' + encodeUrl(entity.getName()) + '"';
	}

	@Override
	public String extractTitle(Element article) {
		return article.selectFirst("h3").text();
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
