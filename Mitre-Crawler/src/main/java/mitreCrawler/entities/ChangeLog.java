package mitreCrawler.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "atk_change_logs")

@Getter
@Setter
public class ChangeLog {
	@Id
	@SequenceGenerator(name = "atk_change_log_seq", sequenceName = "atk_change_log_seq", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "atk_change_log_seq")
	@JoinColumn(name = "change_log_id")
	private Integer id;
	@Column(nullable = false, unique = false)
	private LocalDate date;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "atk_groups_in_change_log", joinColumns = @JoinColumn(name = "change_log_id"), inverseJoinColumns = @JoinColumn(name = "group_id"))
	private Set<Group> groups;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "atk_techniques_in_change_log", joinColumns = @JoinColumn(name = "change_log_id"), inverseJoinColumns = @JoinColumn(name = "technique_id"))
	private Set<Technique> techniques;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "atk_softwares_in_change_log", joinColumns = @JoinColumn(name = "change_log_id"), inverseJoinColumns = @JoinColumn(name = "software_id"))
	private Set<Software> softwares;

	public ChangeLog() {
		date = LocalDate.now();
		groups = new HashSet<Group>();
		techniques = new HashSet<Technique>();
		softwares = new HashSet<Software>();
	}

	public <E> void addChange(E changedEntity) {
		if (changedEntity.getClass().equals(Group.class)) {
			groups.add((Group) changedEntity);
		}
		if (changedEntity.getClass().equals(Technique.class)) {
			techniques.add((Technique) changedEntity);
		}
		if (changedEntity.getClass().equals(Software.class)) {
			softwares.add((Software) changedEntity);
		}
	}

	public boolean contanisChange() {
		return !groups.isEmpty() && !techniques.isEmpty() && !softwares.isEmpty();
	}
}
