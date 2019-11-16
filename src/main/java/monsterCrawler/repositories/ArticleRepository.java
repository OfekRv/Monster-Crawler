package monsterCrawler.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import monsterCrawler.entities.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
	public boolean existsByUrl(String url);

	public Article findByUrl(String url);
}
