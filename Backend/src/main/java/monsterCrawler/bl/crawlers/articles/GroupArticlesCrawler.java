package monsterCrawler.bl.crawlers.articles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import monsterCrawler.entities.Group;

public interface GroupArticlesCrawler extends ArticlesCrawler<Group> {
	@Override
	public default Collection<String> buildUrls(Group entity) {
		Collection<String> urls = new ArrayList<String>();
		urls.add(buildUrl(entity.getName()));
		entity.getAliases().forEach(alias -> buildUrl(alias));
		return urls;
	}

	@Override
	public default Collection<String> buildSearchUrls(Group entity, int currentPage) {
		Collection<String> urls = new ArrayList<String>();
		urls.add(buildSearchUrl(entity.getName(), currentPage));
		entity.getAliases().forEach(alias -> buildSearchUrl(alias, currentPage));
		return urls;
	}

	@Override
	public default Collection<String> getAlternativeEntityNames(Group entity) {
		return Optional.ofNullable(entity.getAliases()).orElse(Collections.emptySet());
	}
}
