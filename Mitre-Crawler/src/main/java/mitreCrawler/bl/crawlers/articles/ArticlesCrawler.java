package mitreCrawler.bl.crawlers.articles;

public interface ArticlesCrawler<E> {
	public void crawl(E entityToCrawl);

}
