package monsterCrawler.controllers;

import java.util.ArrayList;
import java.util.Collection;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import monsterCrawler.entities.Article;
import monsterCrawler.entities.ArticleEntry;
import monsterCrawler.entities.Group;
import monsterCrawler.entities.GroupEntry;
import monsterCrawler.entities.GroupsRelations;
import monsterCrawler.repositories.ArticleRepository;
import monsterCrawler.repositories.GroupRepository;

@RestController
@RequestMapping("api/")
public class RelationGraphController {
	@Inject
	ArticleRepository articleRepository;
	@Inject
	GroupRepository groupRepository;

	@GetMapping("/relationGraph")
	public @ResponseBody GroupsRelations getRelationGraph() {
		Collection<GroupEntry> groups = new ArrayList<>();
		Collection<ArticleEntry> articles;
		for (Group group : groupRepository.findAll()) {
			articles = new ArrayList<>();
			for (Article article : group.getArticles()) {
				articles.add(new ArticleEntry(article.getId(), article.getTitle()));
			}
			groups.add(new GroupEntry(group.getId(), group.getName(), articles));
		}

		return new GroupsRelations(groups);
	}
}