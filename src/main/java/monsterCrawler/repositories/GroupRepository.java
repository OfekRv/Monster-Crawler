package monsterCrawler.repositories;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import monsterCrawler.entities.Group;
import monsterCrawler.entities.projections.GroupProjection;

@RepositoryRestResource(excerptProjection = GroupProjection.class)
public interface GroupRepository extends JpaRepository<Group, String> {
	@Query("SELECT g FROM Group g ORDER BY g.lastScan ASC NULLS FIRST")
	public Collection<Group> findAllByOrderByLastScanAsc();
}
