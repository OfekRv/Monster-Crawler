package monsterCrawler.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

import monsterCrawler.entities.Article;
import monsterCrawler.entities.Group;

@Configuration
public class SpringDataRestConfiguration extends RepositoryRestConfigurerAdapter {
	@Override
	public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		config.exposeIdsFor(Group.class);
		config.exposeIdsFor(Article.class);
	}
}
