package mitreCrawler.bl.crawlers.articles;

import javax.inject.Inject;

import org.slf4j.Logger;

import lombok.extern.slf4j.Slf4j;
import mitreCrawler.entities.NamedEntity;
import mitreCrawler.repositories.ArticleRepository;

@Slf4j
public abstract class AbstractArticlesCrawler<E extends NamedEntity> implements ArticlesCrawler<E> {
	@Inject
	private ArticleRepository articlesRepository;

	@Override
	public ArticleRepository getRepository() {
		return articlesRepository;
	}

	@Override
	public Logger getLogger() {
		return log;
	}
}
