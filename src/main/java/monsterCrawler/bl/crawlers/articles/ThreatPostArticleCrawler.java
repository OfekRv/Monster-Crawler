package monsterCrawler.bl.crawlers.articles;

import monsterCrawler.entities.Group;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Named;
import java.io.IOException;

import static monsterCrawler.utils.CrawelersUtils.*;

@Named
public class ThreatPostArticleCrawler extends AbstractArticlesCrawler<Group> implements GroupArticlesCrawler {
    private static final String SEARCH = "?s=";
    private static final String API_URL = "wp-admin/admin-ajax.php";

    @Value("${THREAT_POST_URL}")
    private String ThreatPostUrl;

    @Override
    public String buildUrl(String name) {
        return ThreatPostUrl + SEARCH + '"' + encodeUrl(name) + '"';
    }

    @Override
    public String buildSearchUrl(String name, int currentPage) {
        return ThreatPostUrl + API_URL;
    }

    @Override
    public Elements extractArticlesElements(Document doc) {
        return getElementsByClass(doc,
                "c-card c-card--horizontal--half@md c-card--horizontal@lg c-card--horizontal--flat@md js-post-item");
    }

    @Override
    public String extractTitle(Element article) {
        return getFirstElementByClass(article, "c-card__title").text();
    }

    @Override
    public String extractArticleDate(Element article) {
        return article.select("time").attr("datetime").split("T")[0];
    }

    @Override
    public String getDateFormatPattern() {
        return "yyyy-MM-dd";
    }

    @Override
    public Document getPage(String name, int pageIndex) throws IOException {
        return createConnection(buildSearchUrl(name, pageIndex)).data("action", "loadmore")
                .data("query", encodeUrl("{\"s\":\"" + name + "\",\"order\":\"DESC\"}"))
                .data("page", Integer.toString(pageIndex)).post();
    }

    @Override
    public int getFirstSearchPageIndex() {
        return 1;
    }
}
