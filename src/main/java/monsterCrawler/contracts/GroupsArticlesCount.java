package monsterCrawler.contracts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GroupsArticlesCount {
    private Collection<GroupArticlesCount> groups;
}
