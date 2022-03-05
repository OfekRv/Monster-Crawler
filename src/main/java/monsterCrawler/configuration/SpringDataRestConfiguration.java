package monsterCrawler.configuration;

import javax.inject.Named;

import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;

import monsterCrawler.entities.Article;
import monsterCrawler.entities.Group;

@Named
public class SpringDataRestConfiguration {
    @Bean
    public RepositoryRestConfigurer repositoryRestConfigurer() {
        return RepositoryRestConfigurer.withConfig(config -> {
			config.exposeIdsFor(Group.class);
			config.exposeIdsFor(Article.class);
        });
    }
}