package monsterCrawler.controllers;

import monsterCrawler.bl.statistics.StatisticsBl;
import monsterCrawler.contracts.GroupsArticlesCount;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.time.LocalDate;

@RestController
@RequestMapping("api/statistics")
public class StatisticsController {
    @Inject
    private StatisticsBl bl;

    @GetMapping("/group")
    public @ResponseBody
    GroupsArticlesCount getGroupArticlesCount(@RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate startDate,
                                              @RequestParam @DateTimeFormat(pattern = "dd-MM-yyyy") LocalDate endDate) {
        return bl.getGroupsArticlesCount(startDate, endDate);
    }
}
