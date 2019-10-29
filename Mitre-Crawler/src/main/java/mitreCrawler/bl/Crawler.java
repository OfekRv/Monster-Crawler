package mitreCrawler.bl;

public interface Crawler<T> {
	public T crawl(String url);
}
