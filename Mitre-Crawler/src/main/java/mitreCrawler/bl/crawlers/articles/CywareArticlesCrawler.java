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
public class CywareArticlesCrawler implements ArticlesCrawler<Group> {
	private static final String SEARCH = "search/";

	private String cywareUrl = "https://cyware.com/";

	@Inject
	private ArticleRepository articlesRepository;

	@Override
	public void crawl(Group entityToCrawl) {
		try {
			Document doc = Jsoup.connect(cywareUrl + SEARCH + entityToCrawl.getName()).get();
			Elements articlesLinks = doc.getElementsByClass("post-content");
			for (Element article : articlesLinks) {
				String articleUrl = article.select("a").first().absUrl("href");
				if (!articlesRepository.existsByUrl(articleUrl)) {
					log.info("[ARTICLE] getting \"" + articleUrl + "\"");
					articlesRepository
							.save(new Article(articleUrl, extractTitle(article), getArticleContent(articleUrl),
									extractArticleDate(article), new HashSet<Group>(Arrays.asList(entityToCrawl))));
				}
			}
			/*
			 * there is a post command which loads more
			 */
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String extractTitle(Element article) {
		return article.select("img").first().attr("title");
	}

	private String getArticleContent(String url) throws IOException {
		return Jsoup.connect(url).get().text();
	}

	private LocalDate extractArticleDate(Element article) {
		return LocalDate.parse(article.getElementsByClass("date").first().text(),
				DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.US));

	}
}
