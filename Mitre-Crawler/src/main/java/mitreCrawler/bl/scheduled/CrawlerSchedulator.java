package mitreCrawler.bl.scheduled;

import javax.inject.Inject;
import javax.inject.Named;

import org.springframework.scheduling.annotation.Scheduled;

import lombok.extern.slf4j.Slf4j;
import mitreCrawler.bl.Crawler;
import mitreCrawler.entities.Group;

@Named
@Slf4j
public class CrawlerSchedulator {

	@Inject
	Crawler<Group> groupsCrawler;

	@Scheduled(cron = "${cron.expression}")
	public void executeCharts() {
		log.info("started executing crawler");
		groupsCrawler.crawl("https://attack.mitre.org/groups/");
		log.info("finished executing crawler");
	}
}
