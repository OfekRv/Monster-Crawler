package monsterCrawler.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "atk_fake_articles")

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FakeArticle {
	@Id
	private Integer id;
	@Column(nullable = false, unique = false)
	private String url;
}
