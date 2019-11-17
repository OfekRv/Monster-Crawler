package monsterCrawler.entities;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RelationGraph {
	private Collection<GroupEntry> groups;
	private Collection<ArticleEntry> articles;
}
