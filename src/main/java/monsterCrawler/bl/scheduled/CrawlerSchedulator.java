package monsterCrawler.bl.scheduled;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.scheduling.annotation.Scheduled;

import monsterCrawler.bl.crawlers.CrawlersManager;

@Named
public class CrawlerSchedulator {
	@Inject
	private CrawlersManager crawlersManager;

	@Scheduled(cron = "${ARTICLES_CRAWLER_EXECUTION_TIMING}")
	public void executeArticlesCrawler() {
		crawlersManager.crawl();
	}

	@Scheduled(cron = "${GROUPS_CRAWLER_EXECUTION_TIMING}")
	public void executeGroupsCrawler() {
		crawlersManager.crawl();
	}
}
