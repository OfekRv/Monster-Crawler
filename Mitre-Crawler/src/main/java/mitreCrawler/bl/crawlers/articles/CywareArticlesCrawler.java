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
public class CywareArticlesCrawler implements ArticlesCrawler<Group> {
	private static final String SEARCH = "search/";

	@Value("${THREAT_POST_URL}")
	private String cywareUrl;

	@Inject
	private ArticleRepository articlesRepository;

	@Override
	public Elements extractArticlesElements(Document doc) {
		return doc.getElementsByClass("post-content");
	}

	@Override
	public String extractTitle(Element article) {
		return article.select("img").first().attr("title");
	}

	@Override
	public String getArticleContent(String url) throws IOException {
		return Jsoup.connect(url).get().text();
	}

	@Override
	public LocalDate extractArticleDate(Element article) {
		return LocalDate.parse(article.getElementsByClass("date").first().text(),
				DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.US));

	}

	@Override
	public String buildUrl(Group entity) {
		return cywareUrl + SEARCH + entity.getName();
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
