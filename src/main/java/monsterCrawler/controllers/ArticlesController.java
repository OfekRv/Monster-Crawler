package monsterCrawler.controllers;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import monsterCrawler.bl.ArticlesBl;

@RestController
@RequestMapping("api/articles")
public class ArticlesController {
	@Inject
	private ArticlesBl bl;

	@PostMapping("/{id}/mark-fake")
	public void setFakeNews(@PathVariable int id) {
		bl.setFakeNews(id);
	}

	@GetMapping("/{id}/content")
	public @ResponseBody String getArticleContent(@PathVariable int id) throws Exception {
		return bl.getArticleContent(id);
	}
}
