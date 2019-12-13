package monsterCrawler.bl.crawlers.articles;

import java.time.LocalDate;

import javax.inject.Inject;

import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import lombok.extern.slf4j.Slf4j;
import monsterCrawler.entities.NamedEntity;
import monsterCrawler.repositories.ArticleContentRepository;
import monsterCrawler.repositories.ArticleRepository;

@Slf4j
public abstract class AbstractArticlesCrawler<E extends NamedEntity> implements ArticlesCrawler<E> {
	@Value("${DEFUALT_SEARCH_PAGES_CRAWL_LIMIT}")
	private int pageLimit;
	@Value("${CRAWL_START_YEAR}")
	private int startYear;
	@Inject
	private ArticleRepository articlesRepository;
	@Inject
	private ArticleContentRepository articlesContentRepository;

	@Override
	public boolean isArticleToCrawl(Element articleElement) {
		LocalDate articleDate = getArticleDate(articleElement);
		return articleDate != null && getArticleDate(articleElement).getYear() >= startYear;
	}

	@Override
	public int getPageLimit() {
		return pageLimit;
	}

	@Override
	public ArticleRepository getArticlesRepository() {
		return articlesRepository;
	}

	@Override
	public ArticleContentRepository getArticlesContentRepository() {
		return articlesContentRepository;
	}

	@Override
	public Logger getLogger() {
		return log;
	}
}
