package monsterCrawler.entities;

import java.util.Collection;
import java.util.Objects;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "atk_techniques")

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Technique implements NamedEntity {
	@Id
	@JoinColumn(name = "techniques_id")
	private String id;
	@Column(nullable = false, unique = false)
	private String name;
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "atk_techniques_tactics")
	@Column(nullable = true, unique = false)
	private Collection<String> tactic;
	@Column(length = 50000, nullable = true, unique = false)
	private String description;

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null)
			return false;
		if (getClass() != o.getClass())
			return false;
		Technique t = (Technique) o;
		return Objects.equals(id, t.id) && Objects.equals(name, t.name) && Objects.equals(description, t.description)
				&& tactic.containsAll(t.tactic) && t.tactic.containsAll(tactic);
	}
}