package monsterCrawler.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import monsterCrawler.entities.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, String> {
}
