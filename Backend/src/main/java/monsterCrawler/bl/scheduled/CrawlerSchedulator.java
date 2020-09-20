package monsterCrawler.bl.scheduled;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;

import monsterCrawler.bl.crawlers.CrawlersManager;

@Named
public class CrawlerSchedulator {
	@Inject
	private CrawlersManager crawlersManager;
	@Value("${CRAWl_GROUPS}")
	private boolean isCrawlGroups;

	@Scheduled(cron = "${CRAWLER_EXECUTION_TIMING}")
	public void executeCrawlers() {
		if (isCrawlGroups) {
			crawlersManager.crawlGroups();
		}
		crawlersManager.crawlArticles();
	}
}
