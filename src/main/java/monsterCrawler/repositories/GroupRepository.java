package monsterCrawler.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import monsterCrawler.entities.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, String> {
	@Query("SELECT g FROM Group g ORDER BY g.lastScan ASC NULLS FIRST")
	public Collection<Group> findAllByOrderByLastScanAsc();
}
