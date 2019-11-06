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
public class ThreatPostArticleCrawler implements ArticlesCrawler<Group> {
	private static final String SEARCH = "?s=";

	@Value("${THREAT_POST_URL}")
	private String ThreatPostUrl;

	@Inject
	private ArticleRepository articlesRepository;

	@Override
	public Elements extractArticlesElements(Document doc) {
		Elements linksContainer = doc.getElementsByClass("c-border-layout");
		if (!linksContainer.isEmpty()) {
			return doc.getElementsByClass("c-border-layout").first().getElementsByClass("o-row");
		}

		return new Elements();
	}

	@Override
	public String extractTitle(Element article) {
		return article.getElementsByClass("c-card__title").first().text();
	}

	@Override
	public String getArticleContent(String url) throws IOException {
		return Jsoup.connect(url).get().text();
	}

	@Override
	public LocalDate extractArticleDate(Element article) {
		return LocalDate.parse(article.select("time").first().attr("datetime").split("T")[0],
				DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.US));

	}

	@Override
	public String buildUrl(Group entity) {
		return ThreatPostUrl + SEARCH + '"' + entity.getName().replace(" ", "%20") + '"';
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
