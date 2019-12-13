package monsterCrawler.entities.projecttions;

import org.springframework.data.rest.core.config.Projection;

import monsterCrawler.entities.Group;

// for article projection
@Projection(name = "group", types = { Group.class })
public interface GroupProjection {
	public String getName();
}
