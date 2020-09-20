package monsterCrawler.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import monsterCrawler.entities.Article;
import monsterCrawler.entities.projecttions.ArticleProjection;

@RepositoryRestResource(excerptProjection = ArticleProjection.class)
public interface ArticleRepository extends JpaRepository<Article, Integer> {
	public boolean existsByUrl(String url);

	public Article findByUrl(String url);
}
