package mitreCrawler.bl;

import java.util.Collection;

public interface Crawler<T> {
	public Collection<T> crawl(String url);
}
