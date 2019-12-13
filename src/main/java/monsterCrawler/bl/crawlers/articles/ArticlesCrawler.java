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

import javax.transaction.Transactional;

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
		Set<String> entityNames = new HashSet<String>();
		entityNames.addAll(getAlternativeEntityNames(entityToCrawl));
		entityNames.add(entityToCrawl.getName());
		String url;
		for (String entityName : entityNames) {
			url = buildUrl(entityName);
			try {
				extractArticlesElements(getRequest(url))
						.forEach(article -> CrawlArticle(entityToCrawl, entityName, article));
				crawlNextPagesArticles(entityToCrawl, entityName);
			} catch (IOException e) {
				getLogger().warn("[ARTICLE] Could not search server with crawler: " + this.getClass().getName()
						+ " for entity: " + entityToCrawl.getName());
			}
		}
	}

	public default void CrawlArticle(E entityToCrawl, String name, Element articleElement) {
		String articleUrl = extractUrl(articleElement);
		Article article;
		String content;
		getLogger().info("[ARTICLE] checking \"" + articleUrl + "\"");

		if (getArticlesRepository().existsByUrl(articleUrl)) {
			article = getArticlesRepository().findByUrl(articleUrl);
			if (!article.isRelatedEntity(entityToCrawl)) {
				content = getArticlesContentRepository().findById(article.getId()).get().getContent();
				if (content.contains(paddedWithSpaces(name))) {
					getLogger().info("[ARTICLE] found another related entity in \"" + articleUrl + "\"");
					relateEntityAndSave(entityToCrawl, article);
				}
			}
		} else {
			content = downloadAsCleanHtml(articleUrl);
			getLogger().info("[ARTICLE] downloading \"" + articleUrl + "\"");
			if (content.contains(paddedWithSpaces(name))) {
				getLogger().info("[ARTICLE] saving \"" + articleUrl + "\"");
				article = new Article(articleUrl, extractTitle(articleElement), getArticleDate(articleElement));
				article = relateEntityAndSave(entityToCrawl, article);
				getArticlesContentRepository().saveAndFlush(new ArticleContent(article.getId(), content));
			}
		}
	}

	@Transactional
	public default Article relateEntityAndSave(E entityToCrawl, Article article) {
		article.addRelatedEntity(entityToCrawl);
		return getArticlesRepository().saveAndFlush(article);
	}

	public default void crawlNextPagesArticles(E entity, String name) {
		int currentPage = getFirstSearchPageIndex();
		int lastPage = getPageLimit();
		Elements currentArticlesElements = new Elements();
		Document doc;
		do {
			try {
				doc = getPage(name, currentPage);
				currentArticlesElements = extractArticlesElements(doc);
				currentArticlesElements.forEach(article -> CrawlArticle(entity, name, article));
			} catch (IOException e) {
				getLogger()
						.warn("[ARTICLE] Could not get next articles, maybe not exists or server problem. stopped before page number "
								+ currentPage + " (" + this.getClass().getName() + ")");
			}

			currentPage++;
		} while (!currentArticlesElements.isEmpty() && currentPage <= lastPage);
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
