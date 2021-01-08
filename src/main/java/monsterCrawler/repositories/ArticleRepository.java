package monsterCrawler.repositories;

import java.time.LocalDate;
import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import monsterCrawler.entities.Article;
import monsterCrawler.entities.Group;
import monsterCrawler.entities.projections.ArticleProjection;

@RepositoryRestResource(excerptProjection = ArticleProjection.class)
public interface ArticleRepository extends JpaRepository<Article, Integer> {
	public boolean existsByUrl(String url);

	public Article findByUrl(String url);

	public Collection<Article> findAllByDateBetweenAndGroupsContaining(LocalDate startDate, LocalDate endDate,
			Group group);
}
