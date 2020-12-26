package monsterCrawler.bl.crawlers.articles;

import monsterCrawler.entities.Group;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Named;

import static monsterCrawler.utils.CrawelersUtils.EMPTY;
import static monsterCrawler.utils.CrawelersUtils.encodeUrl;

@Named
public class CpoMagazineArticleCrawler extends AbstractArticlesCrawler<Group> implements GroupArticlesCrawler {
    private static final String SEARCH_QUERY = "?s=";

    @Value("${CPO_MAG_URL}")
    private String cpoMagazineUrl;

    @Override
    public String buildUrl(String name) {
        return cpoMagazineUrl + SEARCH_QUERY + '"' + encodeUrl(name) + '"';
    }

    @Override
    public String buildSearchUrl(String name, int currentPage) {
        return EMPTY;
    }

    @Override
    public String extractTitle(Element article) {
        return article.selectFirst("h3").text();
    }

    @Override
    public Elements extractArticlesElements(Document doc) {
        return doc.select("main").select("article");
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
        return 1;
    }

    @Override
    public void crawlNextPagesArticles(Group entity, String name) {
        // No need
    }
}
