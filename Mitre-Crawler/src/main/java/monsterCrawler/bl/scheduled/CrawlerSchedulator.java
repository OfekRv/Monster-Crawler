package monsterCrawler.bl.scheduled;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.scheduling.annotation.Scheduled;

import monsterCrawler.bl.crawlers.CrawlersManager;

@Named
public class CrawlerSchedulator {
	@Inject
	private CrawlersManager crawlersManager;

	//@Scheduled(cron = "${cron.expression}")
	public void executeCrawlers() {
		crawlersManager.crawl();
	}
}
