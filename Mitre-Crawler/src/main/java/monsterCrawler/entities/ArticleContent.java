package monsterCrawler.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
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
@Table(name = "atk_articles_content")

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ArticleContent {
	@Id
	@JoinColumn(name = "article_id")
	private Integer id;
	@Column(columnDefinition = "TEXT", nullable = true, unique = false)
	@Basic(fetch = FetchType.LAZY)
	private String content;
}
