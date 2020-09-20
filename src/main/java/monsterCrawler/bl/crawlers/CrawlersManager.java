package monsterCrawler.bl.crawlers;

import java.time.LocalDate;
import java.util.Collection;

import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

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

	public void crawlArticles() {
		log.info("Started executing articles crawler");
		for (Group group : groupRepository.findAllByOrderByLastScanAsc()) {
			log.info("[GROUP] " + group.getName());
			for (ArticlesCrawler<Group> articlesCrawler : articlesCrawlers) {
				articlesCrawler.crawl(group);
			}
			updateLastScan(group);
		}
		log.info("Finished executing articles crawler");
	}

	public void crawlGroups() {
		log.info("Started executing groups crawler");
		groupsCrawler.crawl();
		log.info("Finished executing groups crawler");
	}

	@Transactional
	private void updateLastScan(Group group) {
		group.setLastScan(LocalDate.now());
		groupRepository.saveAndFlush(group);
	}
}
