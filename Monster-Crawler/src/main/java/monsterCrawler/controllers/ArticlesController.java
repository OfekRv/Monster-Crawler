package monsterCrawler.controllers;

import java.util.Optional;

import javax.inject.Inject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import monsterCrawler.entities.ArticleContent;
import monsterCrawler.repositories.ArticleContentRepository;

@RestController
@RequestMapping("api/articles")
public class ArticlesController {
	@Inject
	ArticleContentRepository articleContentRepository;

	@GetMapping("/{id}/content")
	public @ResponseBody String getArticleContent(@PathVariable int id) throws Exception {
		Optional<ArticleContent> article = articleContentRepository.findById(id);
		if (article.isPresent()) {
			return article.get().getContent();
		} else {
			throw new Exception();
		}
	}
}
