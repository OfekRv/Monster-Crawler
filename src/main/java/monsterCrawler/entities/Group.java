package monsterCrawler.entities;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "atk_groups")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Group implements NamedEntity {
	@Id
	@JoinColumn(name = "group_id")
	private String id;
	@Column(nullable = false, unique = true)
	private String name;
	@Column(length = 50000, nullable = true, unique = false)
	private String description;
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "atk_group_aliases")
	@Column(nullable = true, unique = false)
	private Collection<String> aliases;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "atk_techniques_in_groups", joinColumns = @JoinColumn(name = "group_id"), inverseJoinColumns = @JoinColumn(name = "technique_id"))
	private Set<Technique> techniques;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "atk_softwares_in_groups", joinColumns = @JoinColumn(name = "group_id"), inverseJoinColumns = @JoinColumn(name = "software_id"))
	private Set<Software> softwares;
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "groups")
	private Set<Article> articles;

	public Group(String id, String name, String description, Collection<String> aliases, Set<Technique> techniques,
			Set<Software> softwares) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.aliases = aliases;
		this.techniques = techniques;
		this.softwares = softwares;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (getClass() != o.getClass())
			return false;
		Group g = (Group) o;

		Collection<String> groupNames = techniques.stream().map(Technique::getName).collect(Collectors.toList());
		Collection<String> objectGroupNames = g.techniques.stream().map(Technique::getName)
				.collect(Collectors.toList());

		return Objects.equals(id, g.id) && Objects.equals(name, g.name) && Objects.equals(description, g.description)
				&& aliases.containsAll(g.aliases) && g.aliases.containsAll(aliases)
				&& groupNames.containsAll(objectGroupNames) && objectGroupNames.containsAll(groupNames)
				&& softwares.containsAll(g.softwares) && g.softwares.containsAll(softwares);
	}

}
