package monsterCrawler.bl.crawlers.articles;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import lombok.extern.slf4j.Slf4j;
import monsterCrawler.entities.NamedEntity;
import monsterCrawler.repositories.ArticleContentRepository;
import monsterCrawler.repositories.ArticleRepository;

@Slf4j
public abstract class AbstractArticlesCrawler<E extends NamedEntity> implements ArticlesCrawler<E> {
	@Value("${DEFUALT_SEARCH_PAGES_LIMIT}")
	private int pageLimit;
	@Inject
	private ArticleRepository articlesRepository;
	@Inject
	private ArticleContentRepository articlesContentRepository;

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
