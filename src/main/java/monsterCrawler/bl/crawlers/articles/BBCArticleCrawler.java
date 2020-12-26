package monsterCrawler.bl.crawlers.articles;

import monsterCrawler.entities.Group;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Named;

import static monsterCrawler.utils.CrawelersUtils.*;

@Named
public class BBCArticleCrawler extends AbstractArticlesCrawler<Group> implements GroupArticlesCrawler {
    private static final String SEARCH = "search?q=";
    private static final String PAGE_QUERY = "&page=";
    private static final int DATE_INDEX = 2;

    @Value("${BBC_URL}")
    private String bbcUrl;

    @Override
    public String buildUrl(String name) {
        return bbcUrl + SEARCH + '"' + encodeUrl(name) + '"';
    }

    @Override
    public String buildSearchUrl(String name, int currentPage) {
        return bbcUrl + SEARCH + '"' + encodeUrl(name) + '"' + PAGE_QUERY + currentPage;
    }

    @Override
    public String extractTitle(Element article) {
        return article.selectFirst("span").text();
    }

    @Override
    public Elements extractArticlesElements(Document doc) {
        return getElementsByClass(doc, "css-1c71b3r-Promo ett16tt0");
    }

    @Override
    public String extractArticleDate(Element article) {
        return getFirstElementByClass(article, "css-1hizfh0-MetadataSnippet ecn1o5v0").
                select("span").get(DATE_INDEX).text();
    }

    @Override
    public String getDateFormatPattern() {
        return "d MMM yyyy";
    }

    @Override
    public int getFirstSearchPageIndex() {
        return 1;
    }
}
