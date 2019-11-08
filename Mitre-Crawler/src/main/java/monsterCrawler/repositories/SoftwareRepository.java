package monsterCrawler.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import monsterCrawler.entities.Software;

@Repository
public interface SoftwareRepository extends JpaRepository<Software, String> {
}
