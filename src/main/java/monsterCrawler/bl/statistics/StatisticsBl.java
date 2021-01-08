package monsterCrawler.bl.statistics;

import static java.time.temporal.ChronoUnit.MILLIS;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import monsterCrawler.contracts.GroupArticlesCount;
import monsterCrawler.contracts.GroupsArticlesCount;
import monsterCrawler.entities.Group;
import monsterCrawler.repositories.ArticleRepository;
import monsterCrawler.repositories.GroupRepository;

@Named
public class StatisticsBl {
	private final static int FIRST_GROUP_INDEX = 0;

	@Inject
	private ArticleRepository articleRepository;
	@Inject
	private GroupRepository groupRepository;

	public GroupsArticlesCount getGroupsArticlesCount(LocalDate startDate, LocalDate endDate) {
		Collection<GroupArticlesCount> articlesCount = new ArrayList<GroupArticlesCount>();
		for (Group group : groupRepository.findAll()) {
			int groupArticlesCount = articleRepository
					.findAllByDateBetweenAndGroupsContaining(startDate, endDate, group).size();
			if (groupArticlesCount > 0) {
				articlesCount.add(new GroupArticlesCount(group.getId(), group.getName(), groupArticlesCount));
			}
		}

		return new GroupsArticlesCount(articlesCount.stream().sorted().collect(Collectors.toList()));
	}

	public double averageGroupScanTime() {
		List<Group> groups = getScannedGroups();
		return sumScanMiliseconds(groups) / groups.size();
	}

	private List<Group> getScannedGroups() {
		return groupRepository.findAllByOrderByLastScanAsc().stream().filter(group -> group.getLastScan() != null)
				.collect(Collectors.toList());
	}

	private long sumScanMiliseconds(List<Group> groups) {
		long scanSeconds = 0;
		int currentGroupIndex;
		for (currentGroupIndex = FIRST_GROUP_INDEX; currentGroupIndex < groups.size() - 1; currentGroupIndex++) {
			scanSeconds += milisecondsBetween(groups.get(currentGroupIndex), groups.get(currentGroupIndex + 1));
		}

		milisecondsBetween(groups.get(currentGroupIndex), groups.get(FIRST_GROUP_INDEX));

		return scanSeconds;
	}

	private long milisecondsBetween(Group followingScannedGroup, Group prevScannedGroup) {
		return MILLIS.between(followingScannedGroup.getLastScan(), prevScannedGroup.getLastScan());
	}
}
