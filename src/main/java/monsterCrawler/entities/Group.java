package monsterCrawler.entities;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

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
	@Column(nullable = true, unique = false)
	private LocalDate lastScan;

	public Group(String id, String name, String description, Collection<String> aliases, Set<Technique> techniques,
			Set<Software> softwares, LocalDate lastScan) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.aliases = aliases;
		this.techniques = techniques;
		this.softwares = softwares;
		this.lastScan = lastScan;
	}
}
