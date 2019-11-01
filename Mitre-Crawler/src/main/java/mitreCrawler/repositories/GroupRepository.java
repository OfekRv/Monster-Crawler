package mitreCrawler.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mitreCrawler.entities.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, String> {
}
