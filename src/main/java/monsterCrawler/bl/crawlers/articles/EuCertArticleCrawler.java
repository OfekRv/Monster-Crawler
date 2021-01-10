package monsterCrawler.bl.crawlers.articles;

import static monsterCrawler.utils.CrawelersUtils.EMPTY;
import static monsterCrawler.utils.CrawelersUtils.encodeUrl;
import static monsterCrawler.utils.CrawelersUtils.getElementsByClass;
import static monsterCrawler.utils.CrawelersUtils.getFirstElementByClass;

import javax.inject.Named;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;

import monsterCrawler.entities.Group;

// Disabled - Lots of fake news & duplications

//@Named
public class EuCertArticleCrawler extends AbstractArticlesCrawler<Group> implements GroupArticlesCrawler {
	private static final String SEARCH_QUERY = "&edition=searcharticles&option=&atLeast=";
	private static final String LANGUAGE = "dynamic?language=en";

	@Value("${EU_CERT_URL}")
	private String euCertUrl;

	@Override
	public String buildUrl(String name) {
		return euCertUrl + LANGUAGE + SEARCH_QUERY + '\'' + encodeUrl(name) + '\'';
	}

	@Override
	public String buildSearchUrl(String name, int currentPage) {
		return EMPTY;
	}

	@Override
	public String extractTitle(Element article) {
		return getFirstElementByClass(article, "center_story center_headline_top").text();
	}

	@Override
	public Elements extractArticlesElements(Document doc) {
		return getElementsByClass(doc, "articlebox_big");
	}

	@Override
	public String extractArticleDate(Element article) {
		String[] splitted = getFirstElementByClass(article, "center_headline_source").text().split(" ");
		return splitted[2] + splitted[3] + splitted[4];
	}

	@Override
	public String getDateFormatPattern() {
		return "MMMMD,yyyy";
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
