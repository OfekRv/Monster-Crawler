package mitreCrawler.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import mitreCrawler.entities.Article;

@Transactional
@Repository
public interface ArticleRepository extends JpaRepository<Article, Integer> {
	public boolean existsByUrl(String url);

	public Article findByUrl(String url);
}
