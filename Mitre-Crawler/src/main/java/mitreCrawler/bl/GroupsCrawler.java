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
						extractGroupDescription(doc), null, null, null));
				System.out.println(extractGroupId(doc) + "-" + extractGroupName(doc) + "-" + extractGroupVersion(doc));
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
}
