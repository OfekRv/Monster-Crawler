package monsterCrawler.bl.crawlers.groups;

import lombok.extern.slf4j.Slf4j;
import monsterCrawler.entities.Group;
import monsterCrawler.repositories.GroupRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

@Named
@Slf4j
public class AttackGroupsCrawler implements GroupsCrawler {
    private static final int ID_INDEX = 0;
    private static final int OVERVIEW_INDEX = 0;
    private static final int ID_PREFIX_CHAR_COUNT = 4;
    private static final int GROUP_WITH_ALIASES_MIN_TABLE_COUNT = 2;
    private static final int ALIASES_TABLE = 0;

    @Value("${MITRE_ATTACK_GROUPS_URL}")
    private String groupsUrl;

    @Inject
    private GroupRepository groupsRepository;

    @Override
    public void crawl() {
        try {
            Document doc = Jsoup.connect(groupsUrl).get();
            Collection<String> groupLinks = extractGroupLinksElements(doc);
            Group currentGroup;
            for (String link : groupLinks) {
                doc = Jsoup.connect(link).get();
                String groupName = extractName(doc);
                log.info("[GROUP] getting \"" + groupName + "\"");
                String groupId = extractId(doc);
                String groupDescription = extractDescription(doc);

                if (!groupsRepository.existsById(groupId)) {
                    currentGroup = new Group(extractId(doc), groupName, groupDescription, extractGroupAliases(doc),
                            null, null);
                } else {
                    currentGroup = groupsRepository.findById(groupId).get();
                    currentGroup.setAliases(extractGroupAliases(doc));
                    currentGroup.setDescription(groupDescription);
                }
                groupsRepository.saveAndFlush(currentGroup);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Collection<String> extractGroupLinksElements(Document doc) {
        Elements groups = doc.getElementsByClass("group-nav-desktop-view").select("a");
        groups.remove(OVERVIEW_INDEX);
        return groups.stream().map(link -> link.absUrl("href")).collect(Collectors.toList());
    }

    private Collection<String> extractGroupAliases(Document doc) {
        Collection<String> aliases = new ArrayList<>();
        Elements tableValues = doc.getElementsByClass("table table-bordered table-alternate mt-2");

        if (tableValues.size() < GROUP_WITH_ALIASES_MIN_TABLE_COUNT) {
            return aliases;
        }

        tableValues = tableValues.get(ALIASES_TABLE).select("td");

        for (int i = 0; i < tableValues.size(); i += 2) {
            aliases.add(tableValues.get(i).text());
        }

        return aliases;
    }

    private String extractId(Document doc) {
        return doc.getElementsByClass("card-data").get(ID_INDEX).text().substring(ID_PREFIX_CHAR_COUNT);
    }

    private String extractDescription(Document doc) {
        return doc.getElementsByClass("col-md-8 description-body").select("p").text();
    }

    private String extractName(Document doc) {
        return doc.select("h1").text();
    }
}
