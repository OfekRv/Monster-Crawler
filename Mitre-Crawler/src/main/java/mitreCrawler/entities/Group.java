package mitreCrawler.entities;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "atk_groups")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Group {
	@Id
	@JoinColumn(name = "group_id")
	private String id;
	@Column(nullable = false, unique = true)
	private String name;
	// @Column(nullable = false, unique = false)
	// private String contentVersion;
	@Column(length = 50000, nullable = true, unique = false)
	private String description;
	@ElementCollection(fetch = FetchType.EAGER)
	@Column(nullable = true, unique = false)
	private Collection<String> aliases;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "atk_techniques_in_groups", joinColumns = @JoinColumn(name = "group_id"), inverseJoinColumns = @JoinColumn(name = "technique_id"))
	private Set<Technique> techniques;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "atk_softwares_in_groups", joinColumns = @JoinColumn(name = "group_id"), inverseJoinColumns = @JoinColumn(name = "software_id"))
	private Set<Software> softwares;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (getClass() != o.getClass())
			return false;
		Group g = (Group) o;
		techniques.forEach(t -> System.out.println(t.getName()));
		g.techniques.forEach(t -> System.out.println(t.getName() + " $ " + t.getId()));
		return Objects.equals(id, g.id) && Objects.equals(name, g.name) && Objects.equals(description, g.description)
				&& aliases.containsAll(g.aliases) && g.aliases.containsAll(aliases)
				&& techniques.containsAll(g.techniques) && g.techniques.containsAll(techniques)
				&& softwares.containsAll(g.softwares) && g.softwares.containsAll(softwares);
	}
}
