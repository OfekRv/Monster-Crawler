package monsterCrawler.bl.crawlers.articles;

import static monsterCrawler.utils.CrawelersUtils.encodeUrl;
import static monsterCrawler.utils.CrawelersUtils.getFirstElementByClass;

import javax.inject.Named;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;

import monsterCrawler.entities.Group;

@Named
public class WiredArticleCrawler extends AbstractArticlesCrawler<Group> implements GroupArticlesCrawler {
    private static final String SEARCH_QUERY = "search/?q=";
    private static final String PAGE_QUERY = "&page=";
    private static final String SORT = "&sort=publishDate,desc";

    @Value("${WIRED_URL}")
    private String wiredUrl;

    @Override
    public String buildUrl(String name) {
        return wiredUrl + SEARCH_QUERY + '"' + encodeUrl(name) + '"' + SORT;
    }

    @Override
    public String buildSearchUrl(String name, int currentPage) {
        return wiredUrl + SEARCH_QUERY + '"' + encodeUrl(name) + '"' + PAGE_QUERY + currentPage + SORT;
    }

    @Override
    public String extractTitle(Element article) {
        return article.selectFirst("h2").text();
    }

    @Override
    public Elements extractArticlesElements(Document doc) {
        return getFirstElementByClass(doc,"archive-list-component__items").select("li");
    }

    @Override
    public String extractArticleDate(Element article) {
        return article.select("time").text();
    }

    @Override
    public String getDateFormatPattern() {
        return "MMMM d, yyyy";
    }

    @Override
    public int getFirstSearchPageIndex() {
        return 2;
    }
}
