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
			articlesElements.addAll(loadAndExtractNextArticles(entityToCrawl));
			for (Element articleElement : articlesElements) {
				CrawelArticle(entityToCrawl, articleElement);
			}
			/*
			 * there is a post command which loads more
			 */
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public default void CrawelArticle(E entityToCrawl, Element articleElement) throws IOException {
		String articleUrl = extractUrl(articleElement);
		getLogger().info("[ARTICLE] getting \"" + articleUrl + "\"");
		String content = getArticleContent(articleUrl);
		if (content.contains(paddedWithSpaces(getEntityName(entityToCrawl)))) {
			Article article;
			if (getRepository().existsByUrl(articleUrl)) {
				article = getRepository().findByUrl(articleUrl);
				if (!article.isRelatedEntity(entityToCrawl)) {
					getLogger().info("[ARTICLE] found another related entity in \"" + articleUrl + "\"");
					relateEntityAndSave(entityToCrawl, article);
				}
			} else {
				getLogger().info("[ARTICLE] saving \"" + articleUrl + "\"");
				article = new Article(articleUrl, extractTitle(articleElement), content,
						extractArticleDate(articleElement));
				relateEntityAndSave(entityToCrawl, article);
			}

		}
	}

	public default void relateEntityAndSave(E entityToCrawl, Article article) {
		article.addRelatedEntity(entityToCrawl);
		getRepository().saveAndFlush(article);
	}

	public default String getArticleContent(String url) throws IOException {
		Document doc = Jsoup.connect(url).get();
		doc.select("script,link,footer,img,image,iframe,.hidden,style,path,meta,form").remove();
		return doc.html();
	}

	public Elements extractArticlesElements(Document doc);

	public Elements loadAndExtractNextArticles(E entity) throws IOException;

	public String buildUrl(E entity);

	public String extractTitle(Element article);

	public LocalDate extractArticleDate(Element article);

	public String getEntityName(E entity);

	public ArticleRepository getRepository();

	public Logger getLogger();
}
