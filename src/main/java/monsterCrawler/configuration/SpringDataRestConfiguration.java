package monsterCrawler.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

import monsterCrawler.entities.Article;
import monsterCrawler.entities.ChangeLog;
import monsterCrawler.entities.Group;
import monsterCrawler.entities.Software;
import monsterCrawler.entities.Technique;

@Configuration
public class SpringDataRestConfiguration extends RepositoryRestConfigurerAdapter {
	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		config.exposeIdsFor(Group.class);
		config.exposeIdsFor(Technique.class);
		config.exposeIdsFor(Software.class);
		config.exposeIdsFor(ChangeLog.class);
		config.exposeIdsFor(Article.class);
	}
}
