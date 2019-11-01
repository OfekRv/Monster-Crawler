package mitreCrawler.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mitreCrawler.entities.Software;

@Repository
public interface SoftwareRepository extends JpaRepository<Software, String> {
}
