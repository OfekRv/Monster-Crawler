package monsterCrawler.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import monsterCrawler.entities.ArticleContent;

@Repository
public interface ArticleContentRepository extends JpaRepository<ArticleContent, Integer> {
}
