package mitreCrawler.bl;

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
import org.jsoup.select.Elements;

import lombok.extern.slf4j.Slf4j;
import mitreCrawler.entities.Group;
import mitreCrawler.entities.Software;
import mitreCrawler.entities.Technique;
import mitreCrawler.repositories.GroupRepository;
import mitreCrawler.repositories.SoftwareRepository;
import mitreCrawler.repositories.TechniqueRepository;

@Named
@Slf4j
public class GroupsCrawler implements Crawler<Group> {
	private static final int ID_INDEX = 0;
	private static final int TACTIC_INDEX = 1;
	private static final int SOFTWAR_INDEX = 1;

	private static final int ID_PREFIX_CHAR_COUNT = 4;
	private static final int TACTIC_PREFIX_CHAR_COUNT = 8;
	private static final int VERSION_PREFIX_CHAR_COUNT = 9;

	private static final String TACTICS_SEPERATOR = ",";

	@Inject
	private GroupRepository groupsRepository;

	@Inject
	private SoftwareRepository softwaresRepository;

	@Inject
	private TechniqueRepository techniquesRepository;

	@Override
	public Collection<Group> crawl(String url) {
		Collection<Group> crawledGroups = new ArrayList<Group>();
		try {
			Document doc = Jsoup.connect(url).get();
			Collection<String> groupLinks = extractGroupLinksElements(doc);

			Group currentGroup;
			for (String link : groupLinks) {
				doc = Jsoup.connect(link).get();
				String groupName = extractName(doc);
				log.info("[GROUP] getting \"" + groupName + "\"");
				currentGroup = new Group(extractId(doc), groupName, extractDescription(doc), extractGroupAliases(doc),
						getGroupTechniques(doc), getGroupSoftwares(doc));
				crawledGroups.add(currentGroup);
				groupsRepository.save(currentGroup);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return crawledGroups;
	}

	private Collection<String> extractGroupLinksElements(Document doc) {
		return doc.getElementsByClass("group-nav-desktop-view").select("a").next().stream()
				.map(link -> link.absUrl("href")).collect(Collectors.toList());
	}

	private String extractId(Document doc) {
		return doc.getElementsByClass("card-data").get(ID_INDEX).text().substring(ID_PREFIX_CHAR_COUNT);
	}

	private String extractDescription(Document doc) {
		return doc.getElementsByClass("col-md-8 description-body").text();
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

	private Set<Technique> getGroupTechniques(Document doc) {
		Set<Technique> techniques = new HashSet<>();
		for (String techniqueLink : extractGroupTechniquesLinks(doc)) {
			techniques.add(getTechniqueFromLink(techniqueLink));
		}

		techniques.stream().filter(technique -> technique != null)
				.forEach(technique -> techniquesRepository.save(technique));
		return techniques;
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

	private Collection<String> extractTactics(Document doc) {
		Collection<String> tactics = new ArrayList<>();
		Elements details = doc.getElementsByClass("card-data");
		if (details.size() == 2) {
			return tactics;
		}

		return tactics = new ArrayList<String>(Arrays
				.asList(details.get(TACTIC_INDEX).text().substring(TACTIC_PREFIX_CHAR_COUNT).split(TACTICS_SEPERATOR)));
	}

	private Set<Software> getGroupSoftwares(Document doc) {
		Set<Software> softwares = new HashSet<>();
		for (String techniqueLink : extractGroupSoftwaresLinks(doc)) {
			softwares.add(getSoftwareFromLink(techniqueLink));
		}

		softwares.stream().filter(software -> software != null).forEach(software -> softwaresRepository.save(software));
		return softwares;
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

	private String extractName(Document doc) {
		return doc.select("h1").text();
	}

	private Collection<String> extractGroupTechniquesLinks(Document doc) {
		Collection<String> techniquesLinks = new ArrayList<>();
		Elements tableValues = doc.getElementsByClass("table table-bordered table-alternate mt-2");

		int techniquesTableIndex = 1;
		if (tableValues.size() == 2) {
			techniquesTableIndex = 0;
		}

		if (tableValues.size() == 1) {
			if (tableValues.select("h2").is("Techniques Used")) {
				techniquesTableIndex = 0;
			} else {
				return techniquesLinks;
			}
		}

		tableValues = tableValues.get(techniquesTableIndex).select("td");

		for (int i = 0; i < tableValues.size(); i += 2) {
			Elements raw = tableValues.get(i).select("a");
			if (raw.size() > 0) {
				String link = raw.first().absUrl("href");
				if (!link.equals("")) {
					techniquesLinks.add(link);
				}
			}
		}

		return techniquesLinks;
	}

	private Collection<String> extractGroupSoftwaresLinks(Document doc) {
		Collection<String> softwaresLinks = new ArrayList<>();
		Elements tableValues = doc.getElementsByClass("table table-bordered table-alternate mt-2");

		int softwaresTableIndex = 2;
		if (tableValues.size() == 2) {
			softwaresTableIndex = 1;
		}

		if (tableValues.size() == 1) {
			if (tableValues.select("h2").is("Software")) {
				softwaresTableIndex = 0;
			} else {
				return softwaresLinks;
			}
		}

		tableValues = tableValues.get(softwaresTableIndex).select("tr").next();

		for (int i = 0; i < tableValues.size(); i++) {
			softwaresLinks.add(tableValues.get(i).select("td").get(SOFTWAR_INDEX).select("a").first().absUrl("href"));
		}

		return softwaresLinks;
	}
}
