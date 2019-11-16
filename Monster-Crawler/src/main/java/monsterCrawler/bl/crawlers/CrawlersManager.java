package monsterCrawler.bl.crawlers;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Named;

import lombok.extern.slf4j.Slf4j;
import monsterCrawler.bl.crawlers.articles.ArticlesCrawler;
import monsterCrawler.bl.crawlers.groups.AttackGroupsCrawler;
import monsterCrawler.entities.Group;
import monsterCrawler.repositories.GroupRepository;

@Slf4j
@Named
public class CrawlersManager {
	@Inject
	private AttackGroupsCrawler groupsCrawler;
	@Inject
	private Collection<ArticlesCrawler<Group>> articlesCrawlers;
	@Inject
	private GroupRepository groupRepository;

	public void crawl() {
		log.info("Started executing groups crawler");
		groupsCrawler.crawl();
		log.info("Finished executing groups crawler");

		log.info("Started executing atricles crawler");
		for (Group group : groupRepository.findAll()) {
			log.info("[GROUP] " + group.getName());
			for (ArticlesCrawler<Group> articlesCrawler : articlesCrawlers) {
				articlesCrawler.crawl(group);
			}
		}
		log.info("Finished executing groups crawler");
	}
}
