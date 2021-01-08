package monsterCrawler.bl;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import monsterCrawler.entities.Article;
import monsterCrawler.entities.ArticleContent;
import monsterCrawler.entities.FakeArticle;
import monsterCrawler.repositories.ArticleContentRepository;
import monsterCrawler.repositories.ArticleRepository;
import monsterCrawler.repositories.FakeArticleRepository;

@Named
public class ArticlesBl {
	@Inject
	private ArticleRepository articleRepository;
	@Inject
	private ArticleContentRepository articleContentRepository;
	@Inject
	private FakeArticleRepository fakeArticleRepository;

	@Transactional
	public void setFakeNews(int id) {
		Article original = articleRepository.findById(id).get();
		FakeArticle fake = new FakeArticle(original.getId(), original.getUrl());
		articleRepository.delete(original);
		fakeArticleRepository.save(fake);
	}

	public String getArticleContent(int id) throws Exception {
		Optional<ArticleContent> article = articleContentRepository.findById(id);
		if (article.isPresent()) {
			return article.get().getContent();
		} else {
			throw new Exception();
		}
	}
}
