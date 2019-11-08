package mitreCrawler.bl.crawlers.articles;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;

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
	private static final String SEARCH = "search/?o=1&q=";
	private static final String API_URL = "wp-admin/admin-ajax.php";

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
		// TODO: stub
		return new Elements();
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
		return zdnetUrl + SEARCH + '"' + entity.getName().replace(" ", "%20") + '"';
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
