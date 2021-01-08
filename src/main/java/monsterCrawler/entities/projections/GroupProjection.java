package monsterCrawler.entities.projections;

import java.time.OffsetDateTime;
import java.util.Collection;

import org.springframework.data.rest.core.config.Projection;

import monsterCrawler.entities.Group;

@Projection(name = "group", types = { Group.class })
public interface GroupProjection {
	public String getId();

	public String getName();

	public String getDescription();

	public Collection<String> getAliases();

	public OffsetDateTime getLastScan();
}
