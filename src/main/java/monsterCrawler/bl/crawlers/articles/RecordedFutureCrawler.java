package monsterCrawler.bl.crawlers.articles;

import static monsterCrawler.utils.CrawelersUtils.encodeUrl;
import static monsterCrawler.utils.CrawelersUtils.extractUrl;
import static monsterCrawler.utils.CrawelersUtils.getFirstElementByClass;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;

import monsterCrawler.entities.Article;
import monsterCrawler.entities.Group;

import javax.inject.Named;

@Named
public class RecordedFutureCrawler extends AbstractArticlesCrawler<Group> implements GroupArticlesCrawler {
	private static final String SEARCH = "?s=";
	private static final String PAGE = "page/";

	@Value("${RECORDED_FUTURE_URL}")
	private String recordedFutureUrl;

	@Override
	public void crawlArticle(Group entityToCrawl, String name, Element articleElement) {
		super.crawlArticle(entityToCrawl, name, articleElement);
		String url = extractUrl(articleElement);
		if (getArticlesRepository().existsByUrl(url)) {
			Article crawledArticle = getArticlesRepository().findByUrl(url);
			if (crawledArticle.getDate() == null) {
				crawledArticle.setDate(extractArticleDateFromInsideTheArticle(Jsoup
						.parse(getArticlesContentRepository().findById(crawledArticle.getId()).get().getContent())));
				getArticlesRepository().saveAndFlush(crawledArticle);
			}
		}
	}

	@Override
	public String buildUrl(String name) {
		return recordedFutureUrl + SEARCH + '"' + encodeUrl(name) + '"';
	}

	@Override
	public String buildSearchUrl(String name, int currentPage) {
		return recordedFutureUrl + PAGE + currentPage + SEARCH + '"' + encodeUrl(name) + '"';
	}

	@Override
	public String extractTitle(Element article) {
		return getFirstElementByClass(article, "entry-title").text();
	}

	@Override
	public Elements extractArticlesElements(Document doc) {
		return doc.select("article");
	}

	public LocalDate extractArticleDateFromInsideTheArticle(Document article) {
		Optional<Element> time = Optional.ofNullable(getFirstElementByClass(article, "name-date"));
		if (time.isPresent()) {
			String rawTime = getFirstElementByClass(article, "name-date").text().split(" •")[0];
			return LocalDate.parse(rawTime, DateTimeFormatter.ofPattern(getDateFormatPattern(), Locale.US));
		}

		return null;
	}

	@Override
	public String extractArticleDate(Element article) {
		// problem with recorded future date
		return null;
	}

	@Override
	public String getDateFormatPattern() {
		return "MMMM d, yyyy";
	}

	@Override
	public LocalDate getArticleDate(Element article) {
		// problem with recorded future date
		return null;
	}

	@Override
	public int getFirstSearchPageIndex() {
		return 2;
	}

}
