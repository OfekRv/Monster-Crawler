package monsterCrawler.bl.crawlers.articles;

import static monsterCrawler.utils.CrawelersUtils.EMPTY;
import static monsterCrawler.utils.CrawelersUtils.encodeUrl;
import static monsterCrawler.utils.CrawelersUtils.extractUrl;
import static monsterCrawler.utils.CrawelersUtils.getFirstElementByClass;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.inject.Named;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;

import monsterCrawler.entities.Article;
import monsterCrawler.entities.Group;

@Named
public class FireeyeArticleCrawler extends AbstractArticlesCrawler<Group> implements GroupArticlesCrawler {
	private static final String SEARCH = "search.html?q=";
	private static final String RESULTS_PER_PAGE = "&numResultsPerPage=500";

	@Value("${FIREEYE_URL}")
	private String fireeyeUrl;

	@Override
	public boolean isArticleToCrawl(Element articleElement) {
		return true;
	}

	@Override
	public void CrawlArticle(Group entityToCrawl, String name, Element articleElement) {
		String url = extractUrl(articleElement);
		super.CrawlArticle(entityToCrawl, name, articleElement);
		Article crawledArticle = getArticlesRepository().findByUrl(url);
		if (crawledArticle != null && crawledArticle.getDate() == null) {
			crawledArticle.setDate(extractArticleDateFromInsideTheArticle(
					Jsoup.parse(getArticlesContentRepository().findById(crawledArticle.getId()).get().getContent())));
			getArticlesRepository().saveAndFlush(crawledArticle);
		}
	}

	@Override
	public String buildUrl(String name) {
		return fireeyeUrl + SEARCH + '"' + encodeUrl(name) + '"' + RESULTS_PER_PAGE;
	}

	@Override
	public String buildSearchUrl(String name, int currentPage) {
		return EMPTY;
	}

	@Override
	public String extractTitle(Element article) {
		return getFirstElementByClass(article, "a03_link").text();
	}

	@Override
	public Elements extractArticlesElements(Document doc) {
		return doc.getElementsByClass("a03 a03v0");
	}

	public LocalDate extractArticleDateFromInsideTheArticle(Document article) {
		Elements time = article.select("time");
		if (time.size() == 0) {
			return null;
		}
		return LocalDate.parse(article.select("time").text(),
				DateTimeFormatter.ofPattern(getDateFormatPattern(), Locale.US));
	}

	@Override
	public String extractArticleDate(Element article) {
		// problem with fireeye date
		return null;
	}

	@Override
	public String getDateFormatPattern() {
		return "MMMM d, yyyy";
	}

	@Override
	public LocalDate getArticleDate(Element article) {
		// problem with fireeye date
		return null;
	}

	@Override
	public void crawlNextPagesArticles(Group entity, String name) {
		// dont need because of RESULTS_PER_PAGE
	}

	@Override
	public int getFirstSearchPageIndex() {
		// dont need because of RESULTS_PER_PAGE
		return 0;
	}

}
