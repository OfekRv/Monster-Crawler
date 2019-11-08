package monsterCrawler.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "atk_atricles")

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Article {
	@Id
	@SequenceGenerator(name = "atk_article_seq", sequenceName = "atk_article_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "atk_article_seq")
	@JoinColumn(name = "article_id")
	private Integer id;
	@Column(nullable = false, unique = false)
	private String url;
	@Column(nullable = false, unique = false)
	private String title;
	@Column(nullable = true, unique = false)
	private LocalDate date;
	@Lob
	@JsonIgnore
	private String content;
	// should move to group when the group crawler will also crawler articles
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "atk_groups_in_articles", joinColumns = @JoinColumn(name = "article_id"), inverseJoinColumns = @JoinColumn(name = "group_id"))
	private Set<Group> groups;

	public Article(String url, String title, String content, LocalDate date) {
		super();
		this.url = url;
		this.title = title;
		this.content = content;
		this.date = date;
		this.groups = new HashSet<Group>();
	}

	public <E> void addRelatedEntity(E Entity) {
		if (Entity.getClass().equals(Group.class)) {
			groups.add((Group) Entity);
		}
	}

	public <E> boolean isRelatedEntity(E Entity) {
		if (Entity.getClass().equals(Group.class)) {
			return groups.stream().map(Group::getId).collect(Collectors.toList()).contains(((Group) Entity).getId());
		}

		return false;
	}
}
