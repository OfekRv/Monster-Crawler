package monsterCrawler.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import monsterCrawler.entities.ArticleContent;

@Transactional
@Repository
public interface ArticleContentRepository extends JpaRepository<ArticleContent, Integer> {
}
