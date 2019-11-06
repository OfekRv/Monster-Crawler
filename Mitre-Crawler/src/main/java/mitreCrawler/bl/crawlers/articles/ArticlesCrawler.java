package mitreCrawler.bl.crawlers.articles;

import static utils.CrawelersUtils.extractUrl;
import static utils.CrawelersUtils.paddedWithSpaces;

import java.io.IOException;
import java.time.LocalDate;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;

import mitreCrawler.entities.Article;
import mitreCrawler.repositories.ArticleRepository;

public interface ArticlesCrawler<E> {
	public default void crawl(E entityToCrawl) {
		try {
			Document doc = Jsoup.connect(buildUrl(entityToCrawl)).get();
			Elements articlesElements = extractArticlesElements(doc);
			for (Element articleElement : articlesElements) {
				String articleUrl = extractUrl(articleElement);
				if (!getRepository().existsByUrl(articleUrl)) {
					getLogger().info("[ARTICLE] getting \"" + articleUrl + "\"");
					String content = getArticleContent(articleUrl);
					if (content.contains(paddedWithSpaces(getEntityName(entityToCrawl)))) {
						getLogger().info("[ARTICLE] saving \"" + articleUrl + "\"");
						Article article = new Article(articleUrl, extractTitle(articleElement), content,
								extractArticleDate(articleElement));
						article.addRelatedEntity(entityToCrawl);
						getRepository().save(article);
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

	public Elements extractArticlesElements(Document doc);

	public String buildUrl(E entity);

	public String extractTitle(Element article);

	public String getArticleContent(String url) throws IOException;

	public LocalDate extractArticleDate(Element article);

	public String getEntityName(E entity);

	public ArticleRepository getRepository();

	public Logger getLogger();
}
