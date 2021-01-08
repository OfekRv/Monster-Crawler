package monsterCrawler.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import monsterCrawler.entities.FakeArticle;

public interface FakeArticleRepository extends JpaRepository<FakeArticle, Integer> {
	public boolean existsByUrl(String url);
}
