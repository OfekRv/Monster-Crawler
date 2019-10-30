package mitreCrawler.bl;

import java.util.Collection;

import mitreCrawler.entities.Group;

public interface Crawler<T> {
	public Collection<Group> crawl(String url);
}
