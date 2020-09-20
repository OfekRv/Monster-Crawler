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
public class GroupEntry {
	private String id;
	private String name;
	private Collection<ArticleEntry> articles;
}
