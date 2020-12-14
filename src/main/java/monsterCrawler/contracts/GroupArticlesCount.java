package monsterCrawler.contracts;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GroupArticlesCount implements Comparable<GroupArticlesCount> {
    private String id;
    private String name;
    private int articlesCount;

    public int compareTo(GroupArticlesCount c) {
        return c.articlesCount - this.articlesCount;
    }
}
