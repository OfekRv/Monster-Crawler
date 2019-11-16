package monsterCrawler.bl.crawlers.articles;

import javax.inject.Inject;

import org.slf4j.Logger;

import lombok.extern.slf4j.Slf4j;
import monsterCrawler.entities.NamedEntity;
import monsterCrawler.repositories.ArticleContentRepository;
import monsterCrawler.repositories.ArticleRepository;

@Slf4j
public abstract class AbstractArticlesCrawler<E extends NamedEntity> implements ArticlesCrawler<E> {
	@Inject
	private ArticleRepository articlesRepository;
	@Inject
	private ArticleContentRepository articlesContentRepository;

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
