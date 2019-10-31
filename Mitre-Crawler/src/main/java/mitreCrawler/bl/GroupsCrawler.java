package mitreCrawler.bl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.inject.Named;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import mitreCrawler.entities.Group;
import mitreCrawler.entities.Technique;

@Named
public class GroupsCrawler implements Crawler<Group> {
	private static final int ID_INDEX = 0;
	private static final int TACTIC_INDEX = 1;

	private static final int ID_PREFIX_CHAR_COUNT = 4;
	private static final int TACTIC_PREFIX_CHAR_COUNT = 8;
	private static final int VERSION_PREFIX_CHAR_COUNT = 9;

	private static final String TACTICS_SEPERATOR = ",";

	@Override
	public Collection<Group> crawl(String url) {
		Collection<Group> groups = new ArrayList<Group>();
		try {
			Document doc = Jsoup.connect(url).get();
			Collection<String> groupLinks = extractGroupLinksElements(doc);

			for (String link : groupLinks) {
				doc = Jsoup.connect(link).get();
				groups.add(new Group(extractId(doc), extractName(doc), extractVersion(doc), extractDescription(doc),
						extractGroupAliases(doc), getGroupTechniques(doc), null));
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return groups;
	}

	private Collection<String> extractGroupLinksElements(Document doc) {
		return doc.getElementsByClass("group-nav-desktop-view").select("a").next().stream()
				.map(link -> link.absUrl("href")).collect(Collectors.toList());
	}

	private String extractId(Document doc) {
		return doc.getElementsByClass("card-data").get(ID_INDEX).text().substring(ID_PREFIX_CHAR_COUNT);
	}

	private String extractVersion(Document doc) {
		Elements details = doc.getElementsByClass("card-data");

		int versionIndex = 1;
		if (details.size() == 3) {
			versionIndex = 2;
		}

		if (details.size() == 4) {
			versionIndex = 3;
		}

		return details.get(versionIndex).text().substring(VERSION_PREFIX_CHAR_COUNT);
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

	private Collection<Technique> getGroupTechniques(Document doc) {
		return extractGroupTechniquesLinks(doc).stream().map(link -> getTechniqueFromLink(link))
				.collect(Collectors.toCollection(ArrayList::new));
	}

	private Technique getTechniqueFromLink(String url) {
		try {
			Document doc = Jsoup.connect(url).get();
			new Technique(extractName(doc), extractId(doc), extractVersion(doc), extractTactics(doc),
					extractDescription(doc));
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	private String[] extractTactics(Document doc) {
		Elements details = doc.getElementsByClass("card-data");
		if (details.size() == 2) {
			return new String[0];
		}

		return details.get(TACTIC_INDEX).text().substring(TACTIC_PREFIX_CHAR_COUNT).split(TACTICS_SEPERATOR);
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
}
