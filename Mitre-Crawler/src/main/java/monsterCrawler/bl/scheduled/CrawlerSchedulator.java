package monsterCrawler.bl.scheduled;

import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.scheduling.annotation.Scheduled;

import lombok.extern.slf4j.Slf4j;
import monsterCrawler.bl.crawlers.articles.ArticlesCrawler;
import monsterCrawler.bl.crawlers.groups.AttackGroupsCrawler;
import monsterCrawler.entities.Group;
import monsterCrawler.repositories.GroupRepository;

@Named
@Slf4j
public class CrawlerSchedulator {
	@Inject
	private AttackGroupsCrawler groupsCrawler;
	@Inject
	private Collection<ArticlesCrawler<Group>> articlesCrawlers;
	@Inject
	private GroupRepository groupRepository;

	// @Scheduled(cron = "${cron.expression}")
	public void executeGroupsCrawler() {
		log.info("started executing groups crawler");
		groupsCrawler.crawl();
		log.info("finished executing groups crawler");
	}

	@Scheduled(cron = "${cron.expression}")
	public void executeArticlesCrawler() {
		log.info("started executing atricles crawler");
		for (Group group : groupRepository.findAll()) {
			log.info("[GROUP] " + group.getName());
			for (ArticlesCrawler<Group> articlesCrawler : articlesCrawlers) {
				articlesCrawler.crawl(group);
			}
		}

		log.info("finished executing groups crawler");
	}
}
