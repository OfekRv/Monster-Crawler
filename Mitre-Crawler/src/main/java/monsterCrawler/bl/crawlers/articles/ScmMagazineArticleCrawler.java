package monsterCrawler.bl.crawlers.articles;

import static monsterCrawler.utils.CrawelersUtils.encodeUrl;
import static monsterCrawler.utils.CrawelersUtils.getFirstElementByClass;

import java.util.Optional;

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

	@Value("${SCM_URL}")
	private String scmUrl;

	@Override
	public String buildUrl(Group entity) {
		return scmUrl + SEARCH_QUERY + '"' + encodeUrl(entity.getName()) + '"';
	}

	@Override
	public String buildSearchUrl(Group entity, int currentPage) {
		return scmUrl + PAGE + currentPage + "/" + SEARCH_QUERY + '"' + encodeUrl(entity.getName()) + '"';
	}

	@Override
	public String extractTitle(Element article) {
		return article.selectFirst("h3").text();
	}

	@Override
	public Elements extractArticlesElements(Document doc) {
		Optional<Element> articleContainer = Optional
				.ofNullable(getFirstElementByClass(doc, "hm-container -no-sidebar"));
		if (!articleContainer.isPresent()) {
			return new Elements();
		}
		return articleContainer.get().select("article");
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
