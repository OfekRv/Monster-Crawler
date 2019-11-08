package mitreCrawler.bl.crawlers.articles;

import static utils.CrawelersUtils.extractUrl;
import static utils.CrawelersUtils.paddedWithSpaces;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;

import mitreCrawler.entities.Article;
import mitreCrawler.entities.NamedEntity;
import mitreCrawler.repositories.ArticleRepository;

public interface ArticlesCrawler<E extends NamedEntity> {
	public default void crawl(E entityToCrawl) {
		try {
			Document doc = Jsoup.connect(buildUrl(entityToCrawl)).get();
			Elements articlesElements = extractArticlesElements(doc);
			articlesElements.addAll(loadAndExtractNextArticles(entityToCrawl));
			for (Element articleElement : articlesElements) {
				CrawelArticle(entityToCrawl, articleElement);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public default void CrawelArticle(E entityToCrawl, Element articleElement) throws IOException {
		String articleUrl = extractUrl(articleElement);
		getLogger().info("[ARTICLE] getting \"" + articleUrl + "\"");
		String content = getArticleContent(articleUrl);
		if (content.contains(paddedWithSpaces(entityToCrawl.getName()))) {
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
						parseArticleDate(articleElement));
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

	public default Elements loadAndExtractNextArticles(E entity) throws IOException {
		int currentPage = getFirstSearchPageIndex();
		Elements articlesElements = new Elements();
		Elements currentArticlesElements;
		Document doc;
		do {
			doc = Jsoup.connect(buildSearchUrl(entity, currentPage)).ignoreHttpErrors(true).get();

			currentArticlesElements = extractArticlesElements(doc);
			articlesElements.addAll(currentArticlesElements);

		} while (!currentArticlesElements.isEmpty());

		return articlesElements;
	}

	public default LocalDate parseArticleDate(Element article) {
		return LocalDate.parse(extractArticleDate(article),
				DateTimeFormatter.ofPattern(getDateFormatPattern(), Locale.US));
	}

	public String buildUrl(E entity);

	public String buildSearchUrl(E entity, int currentPage);

	public String extractTitle(Element article);

	public Elements extractArticlesElements(Document doc);

	public String extractArticleDate(Element article);

	public String getDateFormatPattern();

	public int getFirstSearchPageIndex();

	public ArticleRepository getRepository();

	public Logger getLogger();
}
