package monsterCrawler.controllers;

import java.time.LocalDate;

import javax.inject.Inject;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import monsterCrawler.bl.statistics.StatisticsBl;
import monsterCrawler.contracts.GroupsArticlesCount;

@RestController
@RequestMapping("api/statistics")
public class StatisticsController {
	@Inject
	private StatisticsBl bl;

	@GetMapping("/group")
	public @ResponseBody GroupsArticlesCount getGroupArticlesCount(
			@RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDate,
			@RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate endDate) {
		return bl.getGroupsArticlesCount(startDate, endDate);
	}

	@GetMapping("/lastScanAvgMs")
	public double getAverageScanTime() {
		return bl.averageGroupScanTime();
	}
}
