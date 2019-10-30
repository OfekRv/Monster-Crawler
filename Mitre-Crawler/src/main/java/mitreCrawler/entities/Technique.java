package mitreCrawler.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Technique {
	private String name;
	private String id;
	private String version;
	private String tactic;
	private String description;
}
