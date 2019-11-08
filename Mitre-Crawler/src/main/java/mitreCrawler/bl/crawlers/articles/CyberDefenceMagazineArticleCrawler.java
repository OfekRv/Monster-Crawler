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
public class CyberDefenceMagazineArticleCrawler implements ArticlesCrawler<Group> {
	private static final String SEARCH = "?s=";

	@Value("${CYBER_DEFENCE_MAGAZINE_URL}")
	private String cyberDefenceMagazineUrl;

	@Inject
	private ArticleRepository articlesRepository;

	@Override
	public Elements extractArticlesElements(Document doc) {
		return doc.getElementsByClass("item-details");
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
		return LocalDate.parse(article.select("time").first().text(),
				DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.US));

	}

	@Override
	public String buildUrl(Group entity) {
		return cyberDefenceMagazineUrl + SEARCH + '"' + entity.getName().replace(" ", "%20") + '"';
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
