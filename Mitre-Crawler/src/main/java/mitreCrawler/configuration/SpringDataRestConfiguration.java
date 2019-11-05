package mitreCrawler.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

import mitreCrawler.entities.Article;
import mitreCrawler.entities.ChangeLog;
import mitreCrawler.entities.Group;
import mitreCrawler.entities.Software;
import mitreCrawler.entities.Technique;

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
