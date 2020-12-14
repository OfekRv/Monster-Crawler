package monsterCrawler.bl.statistics;

import monsterCrawler.contracts.GroupArticlesCount;
import monsterCrawler.contracts.GroupsArticlesCount;
import monsterCrawler.entities.Group;
import monsterCrawler.repositories.ArticleRepository;
import monsterCrawler.repositories.GroupRepository;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Named
public class StatisticsBl {
    @Inject
    private ArticleRepository articleRepository;
    @Inject
    private GroupRepository groupRepository;

    public GroupsArticlesCount getGroupsArticlesCount(LocalDate startDate, LocalDate endDate) {
        Collection<GroupArticlesCount> articlesCount = new ArrayList<GroupArticlesCount>();
        for (Group group : groupRepository.findAll()) {
            int groupArticlesCount = articleRepository.findAllByDateBetweenAndGroupsContaining(startDate, endDate, group).size();
            if (groupArticlesCount > 0) {
                articlesCount.add(new GroupArticlesCount(group.getId(), group.getName(), groupArticlesCount));
            }
        }

        return new GroupsArticlesCount(articlesCount.stream().sorted().collect(Collectors.toList()));
    }
}
