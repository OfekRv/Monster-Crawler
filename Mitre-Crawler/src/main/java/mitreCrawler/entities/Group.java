package mitreCrawler.entities;

import java.util.Collection;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
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
@EqualsAndHashCode
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
	@Lob
	@Column(nullable = true, unique = false)
	private String description;
	@ElementCollection
	@Column(nullable = true, unique = false)
	private Collection<String> aliases;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "atk_techniques_in_groups", joinColumns = @JoinColumn(name = "group_id"), inverseJoinColumns = @JoinColumn(name = "technique_id"))
	private Set<Technique> techniques;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "atk_softwares_in_groups", joinColumns = @JoinColumn(name = "group_id"), inverseJoinColumns = @JoinColumn(name = "software_id"))
	private Set<Software> softwares;
}
