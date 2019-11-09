package monsterCrawler.bl.crawlers.articles;

import static monsterCrawler.utils.CrawelersUtils.downloadAsCleanHtml;
import static monsterCrawler.utils.CrawelersUtils.extractUrl;
import static monsterCrawler.utils.CrawelersUtils.getRequest;
import static monsterCrawler.utils.CrawelersUtils.getRequestIgnoringBadStatusCode;
import static monsterCrawler.utils.CrawelersUtils.paddedWithSpaces;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;

import monsterCrawler.entities.Article;
import monsterCrawler.entities.NamedEntity;
import monsterCrawler.repositories.ArticleRepository;

public interface ArticlesCrawler<E extends NamedEntity> {
	public default void crawl(E entityToCrawl) {
		try {
			Document doc = getRequest(buildUrl(entityToCrawl));
			Elements articlesElements = extractArticlesElements(doc);
			articlesElements.addAll(loadAndExtractNextArticles(entityToCrawl));
			for (Element articleElement : articlesElements) {
				CrawlArticle(entityToCrawl, articleElement);
			}
		} catch (IOException e) {
			getLogger().warn("[ARTICLE] Could not search server with crawler: " + this.getClass().getName()
					+ " for entity: " + entityToCrawl.getName());
		}
	}

	public default void CrawlArticle(E entityToCrawl, Element articleElement) {
		String articleUrl = extractUrl(articleElement);
		getLogger().info("[ARTICLE] getting \"" + articleUrl + "\"");
		String content = downloadAsCleanHtml(articleUrl);

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
						getArticleDate(articleElement));
				relateEntityAndSave(entityToCrawl, article);
			}

		}
	}

	public default void relateEntityAndSave(E entityToCrawl, Article article) {
		article.addRelatedEntity(entityToCrawl);
		getRepository().saveAndFlush(article);
	}

	public default Elements loadAndExtractNextArticles(E entity) {
		int currentPage = getFirstSearchPageIndex();
		int lastPage = getPageLimit();
		Elements articlesElements = new Elements();
		Elements currentArticlesElements;
		Document doc;
		do {
			try {
				doc = getSearchPage(entity, currentPage);
			} catch (IOException e) {
				getLogger()
						.warn("[ARTICLE] Could not get next articles, maybe not exists or server problem. stopped before page number \""
								+ currentPage + "\" (" + this.getClass().getName() + ")");
				return articlesElements;
			}

			currentArticlesElements = extractArticlesElements(doc);
			articlesElements.addAll(currentArticlesElements);
			currentPage++;
		} while (!currentArticlesElements.isEmpty() && currentPage <= lastPage);

		return articlesElements;
	}

	public default Document getSearchPage(E entity, int pageIndex) throws IOException {
		return getRequestIgnoringBadStatusCode(buildSearchUrl(entity, pageIndex));
	}

	public default LocalDate getArticleDate(Element article) {
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

	public default int getPageLimit() {
		return 20;
	}

	public ArticleRepository getRepository();

	public Logger getLogger();
}
