package mitreCrawler.bl;

import java.io.IOException;

import javax.inject.Named;

import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import mitreCrawler.entities.Group;

@Named
public class GroupsCrawler implements Crawler<Group> {
	@Override
	public Group crawl(String url) {
		HttpGet request = new HttpGet(url);

		try (CloseableHttpClient httpClient = HttpClients.createDefault();
				CloseableHttpResponse response = httpClient.execute(request)) {
			System.out.println(response.getProtocolVersion());
			System.out.println(response.getStatusLine().getStatusCode());
			System.out.println(response.getStatusLine().getReasonPhrase());
			System.out.println(response.getStatusLine().toString());

			HttpEntity entity = response.getEntity();
			String result = EntityUtils.toString(entity);
			System.out.println(result);

		} catch (ParseException e) {
			// TODO check what to throw
			e.printStackTrace();
		} catch (IOException e) {
			// TODO check what to throw
			e.printStackTrace();
		}

		return null;
	}
}
