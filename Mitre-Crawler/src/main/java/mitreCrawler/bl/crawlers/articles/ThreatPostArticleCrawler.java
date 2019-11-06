package mitreCrawler.bl.crawlers.articles;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Named;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import lombok.extern.slf4j.Slf4j;
import mitreCrawler.entities.Article;
import mitreCrawler.entities.Group;
import mitreCrawler.repositories.ArticleRepository;

@Named
@Slf4j
public class ThreatPostArticleCrawler implements ArticlesCrawler<Group> {
	private static final String SEARCH = "?s=";

	private String infoSecurityMagazineUrl = "https://threatpost.com/";

	@Inject
	private ArticleRepository articlesRepository;

	@Override
	public void crawl(Group entityToCrawl) {

		try {
			Document doc = Jsoup.connect(infoSecurityMagazineUrl + SEARCH + '"' + entityToCrawl.getName() + '"').get();
			Elements articlesLinks = extractArticlesLinks(doc);
			for (Element article : articlesLinks) {
				String articleUrl = article.select("a").first().absUrl("href");
				if (!articlesRepository.existsByUrl(articleUrl)) {
					log.info("[ARTICLE] getting \"" + articleUrl + "\"");
					String content = getArticleContent(articleUrl);
					if (content.contains(paddedWithSpaces(entityToCrawl.getName()))) {
						log.info("[ARTICLE] saving \"" + articleUrl + "\"");
						articlesRepository.save(new Article(articleUrl, extractTitle(article), content,
								extractArticleDate(article), new HashSet<Group>(Arrays.asList(entityToCrawl))));
					}
				}
			}
			/*
			 * there is a post command which loads more
			 */
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Elements extractArticlesLinks(Document doc) {
		Elements linksContainer = doc.getElementsByClass("c-border-layout");
		if (!linksContainer.isEmpty()) {
			return doc.getElementsByClass("c-border-layout").first().getElementsByClass("o-row");
		}

		return new Elements();
	}

	private String extractTitle(Element article) {
		return article.getElementsByClass("c-card__title").first().text();
	}

	private String getArticleContent(String url) throws IOException {
		return Jsoup.connect(url).get().text();
	}

	private LocalDate extractArticleDate(Element article) {
		return LocalDate.parse(article.select("time").first().attr("datetime").split("T")[0],
				DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.US));

	}

	private String paddedWithSpaces(String text) {
		return " " + text + "";
	}
}
