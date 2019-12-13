package monsterCrawler.bl.crawlers.groups;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Named;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;

import lombok.extern.slf4j.Slf4j;
import monsterCrawler.entities.Group;
import monsterCrawler.entities.Software;
import monsterCrawler.entities.Technique;
import monsterCrawler.repositories.GroupRepository;
import monsterCrawler.repositories.SoftwareRepository;
import monsterCrawler.repositories.TechniqueRepository;

@Named
@Slf4j
public class AttackGroupsCrawler implements GroupsCrawler {
	private static final int ID_INDEX = 0;
	private static final int TACTIC_INDEX = 1;
	private static final int SOFTWARE_INDEX = 1;
	private static final int TECHNIQUE_INDEX = 2;

	private static final int OVERVIEW_INDEX = 0;

	private static final int ID_PREFIX_CHAR_COUNT = 4;
	private static final int TACTIC_PREFIX_CHAR_COUNT = 8;

	private static final String TACTICS_SEPERATOR = ",";

	@Value("${MITRE_ATTACK_GROUPS_URL}")
	private String groupsUrl;

	@Inject
	private GroupRepository groupsRepository;
	@Inject
	private SoftwareRepository softwaresRepository;
	@Inject
	private TechniqueRepository techniquesRepository;

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
				currentGroup = new Group(extractId(doc), groupName, extractDescription(doc), extractGroupAliases(doc),
						getGroupTechniques(doc), getGroupSoftwares(doc), null);
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

		if (tableValues.size() < 3) {
			return aliases;
		}

		tableValues = tableValues.get(0).select("td");

		for (int i = 0; i < tableValues.size(); i += 2) {
			aliases.add(tableValues.get(i).text());
		}

		return aliases;
	}

	private Collection<String> extractTactics(Document doc) {
		Collection<String> tactics = new ArrayList<>();
		Elements details = doc.getElementsByClass("card-data");
		if (details.size() == 2) {
			return tactics;
		}

		return tactics = new ArrayList<String>(Arrays
				.asList(details.get(TACTIC_INDEX).text().substring(TACTIC_PREFIX_CHAR_COUNT).split(TACTICS_SEPERATOR)));
	}

	private Set<Technique> getGroupTechniques(Document doc) {
		Set<Technique> techniques = new HashSet<>();
		for (String techniqueLink : extractGroupTechniquesLinks(doc)) {
			techniques.add(getTechniqueFromLink(techniqueLink));
		}

		techniques.stream().filter(technique -> technique != null)
				.forEach(technique -> techniquesRepository.saveAndFlush(technique));
		return techniques;
	}

	private Collection<String> extractGroupTechniquesLinks(Document doc) {
		Collection<String> techniquesLinks = new ArrayList<>();
		Elements tables = doc.getElementsByClass("table table-bordered table-alternate mt-2");

		for (int i = 0; i < tables.size(); i++) {
			Element currentTable = tables.get(i);
			if (firstColumnNameOfTable(currentTable.getAllElements()).text().equals("Domain")) {
				Elements rows = currentTable.select("tbody").select("tr");
				for (int j = 0; j < rows.size(); j++) {
					techniquesLinks
							.add(rows.get(j).select("td").get(TECHNIQUE_INDEX).select("a").first().absUrl("href"));
				}
			}
		}

		return techniquesLinks;
	}

	private Technique getTechniqueFromLink(String url) {
		Technique technique = null;
		try {
			Document doc = Jsoup.connect(url).get();
			String techniqueName = extractName(doc);
			log.info("[TECHNIQUE] getting \"" + techniqueName + "\"");
			technique = new Technique(extractId(doc), techniqueName, extractTactics(doc), extractDescription(doc));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return technique;
	}

	private Set<Software> getGroupSoftwares(Document doc) {
		Set<Software> softwares = new HashSet<>();
		for (String softwareLink : extractGroupSoftwaresLinks(doc)) {
			softwares.add(getSoftwareFromLink(softwareLink));
		}

		softwares.stream().filter(software -> software != null)
				.forEach(software -> softwaresRepository.saveAndFlush(software));
		return softwares;
	}

	private Collection<String> extractGroupSoftwaresLinks(Document doc) {

		Collection<String> softwaresLinks = new ArrayList<>();
		Elements tables = doc.getElementsByClass("table table-bordered table-alternate mt-2");

		for (int i = 0; i < tables.size(); i++) {
			Element currentTable = tables.get(i);
			if (firstColumnNameOfTable(currentTable.getAllElements()).text().equals("ID")) {
				Elements rows = currentTable.select("tbody").select("tr");
				for (int j = 0; j < rows.size(); j++) {
					softwaresLinks.add(rows.get(j).select("td").get(SOFTWARE_INDEX).select("a").first().absUrl("href"));
				}
			}
		}

		return softwaresLinks;
	}

	private Software getSoftwareFromLink(String url) {
		Software software = null;
		try {
			Document doc = Jsoup.connect(url).get();
			String softwareName = extractName(doc);
			log.info("[SOFTWARE] getting \"" + softwareName + "\"");
			software = new Software(extractId(doc), softwareName, extractDescription(doc));
		} catch (IOException e) {
			e.printStackTrace();
		}
		softwaresRepository.save(software);
		return software;
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

	private Element firstColumnNameOfTable(Elements tableValues) {
		return tableValues.select("th").first();
	}
}
