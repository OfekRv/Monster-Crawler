package monsterCrawler.bl.crawlers.articles;

import static monsterCrawler.utils.CrawelersUtils.downloadAsCleanHtml;
import static monsterCrawler.utils.CrawelersUtils.extractUrl;
import static monsterCrawler.utils.CrawelersUtils.getRequest;
import static monsterCrawler.utils.CrawelersUtils.getRequestIgnoringBadStatusCode;
import static monsterCrawler.utils.CrawelersUtils.paddedWithSpaces;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;

import monsterCrawler.entities.Article;
import monsterCrawler.entities.ArticleContent;
import monsterCrawler.entities.NamedEntity;
import monsterCrawler.repositories.ArticleContentRepository;
import monsterCrawler.repositories.ArticleRepository;

public interface ArticlesCrawler<E extends NamedEntity> {
	public default void crawl(E entityToCrawl) {
		Set<String> entityNames = new HashSet<>();
		entityNames.addAll(getAlternativeEntityNames(entityToCrawl));
		entityNames.add(entityToCrawl.getName());
		String url;
		for (String entityName : entityNames) {
			url = buildUrl(entityName);
			try {
				Document doc = getRequest(url);
				Elements articlesElements = extractArticlesElements(doc);
				articlesElements.addAll(loadAndExtractNextArticles(entityToCrawl, entityName));
				for (Element articleElement : articlesElements) {
					CrawlArticle(entityToCrawl, entityName, articleElement);
				}
			} catch (IOException e) {
				getLogger().warn("[ARTICLE] Could not search server with crawler: " + this.getClass().getName()
						+ " for entity: " + entityToCrawl.getName());
			}
		}
	}

	public default void CrawlArticle(E entityToCrawl, String name, Element articleElement) {
		String articleUrl = extractUrl(articleElement);
		getLogger().info("[ARTICLE] getting \"" + articleUrl + "\"");
		String content = downloadAsCleanHtml(articleUrl);

		if (content.contains(paddedWithSpaces(name))) {
			Article article;
			if (getArticlesRepository().existsByUrl(articleUrl)) {
				article = getArticlesRepository().findByUrl(articleUrl);
				if (!article.isRelatedEntity(entityToCrawl)) {
					getLogger().info("[ARTICLE] found another related entity in \"" + articleUrl + "\"");
					relateEntityAndSave(entityToCrawl, article);
				}
			} else {
				getLogger().info("[ARTICLE] saving \"" + articleUrl + "\"");
				article = new Article(articleUrl, extractTitle(articleElement), getArticleDate(articleElement));
				article = relateEntityAndSave(entityToCrawl, article);
				getArticlesContentRepository().saveAndFlush(new ArticleContent(article.getId(), content));
			}
		}
	}

	public default Article relateEntityAndSave(E entityToCrawl, Article article) {
		article.addRelatedEntity(entityToCrawl);
		return getArticlesRepository().saveAndFlush(article);
	}

	public default Elements loadAndExtractNextArticles(E entity, String name) {
		int currentPage = getFirstSearchPageIndex();
		int lastPage = getPageLimit();
		Elements articlesElements = new Elements();
		Elements currentArticlesElements = new Elements();
		Document doc;
		do {
			try {
				doc = getPage(name, currentPage);
			} catch (IOException e) {
				getLogger()
						.warn("[ARTICLE] Could not get next articles, maybe not exists or server problem. stopped before page number "
								+ currentPage + " (" + this.getClass().getName() + ")");
				return articlesElements;
			}

			currentArticlesElements = extractArticlesElements(doc);
			currentPage++;
		} while (!currentArticlesElements.isEmpty() && currentPage <= lastPage);

		return articlesElements;
	}

	public default LocalDate getArticleDate(Element article) {
		try {
			return LocalDate.parse(extractArticleDate(article),
					DateTimeFormatter.ofPattern(getDateFormatPattern(), Locale.US));
		} catch (Exception e) {
			getLogger().warn("[DATE] Could not parse date");
			return null;
		}
	}

	public default Document getPage(String name, int currentPage) throws IOException {
		return getRequestIgnoringBadStatusCode(buildSearchUrl(name, currentPage));
	}

	public Collection<String> buildUrls(E entity);

	public Collection<String> buildSearchUrls(E entity, int currentPage);

	public Collection<String> getAlternativeEntityNames(E entity);

	public String buildUrl(String name);

	public String buildSearchUrl(String name, int currentPage);

	public String extractTitle(Element article);

	public Elements extractArticlesElements(Document doc);

	public String extractArticleDate(Element article);

	public String getDateFormatPattern();

	public int getFirstSearchPageIndex();

	public int getPageLimit();

	public ArticleRepository getArticlesRepository();

	public ArticleContentRepository getArticlesContentRepository();

	public Logger getLogger();
}