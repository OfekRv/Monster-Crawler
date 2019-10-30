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

	private static final int ID_PREFIX_CHAR_COUNT = 4;
	private static final int VERSION_PREFIX_CHAR_COUNT = 9;

	@Override
	public Collection<Group> crawl(String url) {
		Collection<Group> groups = new ArrayList<Group>();
		try {
			Document doc = Jsoup.connect(url).get();
			Collection<String> groupLinks = extractGroupLinksElements(doc);

			for (String link : groupLinks) {
				doc = Jsoup.connect(link).get();
				groups.add(new Group(extractGroupId(doc), extractGroupName(doc), extractGroupVersion(doc),
						extractGroupDescription(doc), extractGroupAliases(doc), getGroupTechniques(doc), null));
				System.out.println(extractGroupName(doc));
				extractGroupAliases(doc).stream().forEach(alias -> System.out.println(alias));
				System.out.println(extractGroupDescription(doc));
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

	private String extractGroupName(Document doc) {
		return doc.select("h1").text();
	}

	private String extractGroupId(Document doc) {
		return doc.getElementsByClass("card-data").get(ID_INDEX).text().substring(ID_PREFIX_CHAR_COUNT);
	}

	private String extractGroupVersion(Document doc) {
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

	private String extractGroupDescription(Document doc) {
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
		Collection<Technique> techniques = new ArrayList<>();

		extractGroupTechniquesLinks(doc);

		return techniques;
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
					System.out.println(link);
					techniquesLinks.add(link);
				}
			}
		}

		return techniquesLinks;
	}
}
