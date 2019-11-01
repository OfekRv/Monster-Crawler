package mitreCrawler.entities;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "atk_techniques")

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
public class Technique {
	@Id
	@JoinColumn(name = "techniques_id")
	private String id;
	@Column(nullable = false, unique = true)
	private String name;
	//@Column(nullable = false, unique = false)
	//private String contentVersion;
	@ElementCollection
	@Column(nullable = true, unique = false)
	private Collection<String> tactic;
	@Column(length = 50000, nullable = true, unique = false)
	private String description;
}