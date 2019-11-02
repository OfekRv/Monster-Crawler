package mitreCrawler.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mitreCrawler.entities.ChangeLog;

@Repository
public interface ChangeLogRepository extends JpaRepository<ChangeLog, Integer> {
}
