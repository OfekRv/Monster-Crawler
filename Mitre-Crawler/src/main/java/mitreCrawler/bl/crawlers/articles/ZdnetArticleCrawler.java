package mitreCrawler.bl.crawlers.articles;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import lombok.extern.slf4j.Slf4j;
import mitreCrawler.entities.Group;
import mitreCrawler.repositories.ArticleRepository;

@Named
@Slf4j
public class ZdnetArticleCrawler implements ArticlesCrawler<Group> {
	private static final String SEARCH = "search/";
	private static final String SEARCH_QUERY = "?o=1&q=";

	@Value("${ZDNET_URL}")
	private String zdnetUrl;

	@Inject
	private ArticleRepository articlesRepository;

	@Override
	public Elements extractArticlesElements(Document doc) {
		return doc.select("article");
	}

	@Override
	public Elements loadAndExtractNextArticles(Group entity) throws IOException {
		int currentPage = 2;
		Elements articlesElements = new Elements();
		Elements currentArticlesElements;
		Document doc;
		do {
			doc = Jsoup.connect(zdnetUrl + SEARCH + "/" + currentPage++ + "/" + SEARCH_QUERY + '"'
					+ entity.getName().replace(" ", "%20") + '"').ignoreHttpErrors(true).get();

			currentArticlesElements = extractArticlesElements(doc);
			articlesElements.addAll(currentArticlesElements);

		} while (!currentArticlesElements.isEmpty());

		return articlesElements;
	}

	@Override
	public String extractTitle(Element article) {
		return article.select("h3").first().text();
	}

	@Override
	public LocalDate extractArticleDate(Element article) {
		return LocalDate.parse(article.getElementsByClass("meta").first().select("span").first().text(),
				DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.US));

	}

	@Override
	public String buildUrl(Group entity) {
		return zdnetUrl + SEARCH + SEARCH_QUERY + '"' + entity.getName().replace(" ", "%20") + '"';
	}

	@Override
	public String getEntityName(Group entity) {
		return entity.getName();
	}

	@Override
	public ArticleRepository getRepository() {
		return articlesRepository;
	}

	@Override
	public Logger getLogger() {
		return log;
	}
}
