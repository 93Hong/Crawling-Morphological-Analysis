package crawl.daum;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class NewCrawlerLeg {
	// We'll use a fake USER_AGENT so the web server thinks the robot is a
	// normal web browser.
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
	private Document htmlDocument;
	private String pre = "/usr";//"c:";//
	NewCrawler da = new NewCrawler();

	public boolean crawl(String url, int File_Name) {
		try {
			Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
			Document htmlDocument = connection.get();
			this.htmlDocument = htmlDocument;

			// 200 is the HTTP OK status code indicating that everything is
			// great.
			if (connection.response().statusCode() == 200) {
				// System.out.println("\nDaum : " + url);
			}
			if (!connection.response().contentType().contains("text/html")) {
				System.out.println("**Failure** Retrieved something other than HTML");
				return false;
			}

			String article = null;
			if (this.htmlDocument != null) {
				if (this.htmlDocument.body() != null) {
					if (this.htmlDocument.body().getElementsByAttributeValueMatching("id", "harmonyContainer") != null)
						article = this.htmlDocument.body().getElementsByAttributeValueMatching("id", "harmonyContainer")
								.text();
					else
						return false;
				} else
					return false;
			} else
				return false;

			DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MINUTE, -30);

			if (article != null && !article.isEmpty()) {
				String articleTime = url.split("v/")[1].substring(0, 12);
				String thirtyMinAgo = dateFormat.format(calendar.getTime());

				if (thirtyMinAgo.compareTo(articleTime) < 0) {
					if (!da.getMap().containsKey(this.htmlDocument.title())
							&& (this.htmlDocument.title().toUpperCase().charAt(0) < 'A'
							|| this.htmlDocument.title().toUpperCase().charAt(0) > 'Z')) {
						makeFile(File_Name);
						da.getTitles().add(this.htmlDocument.title());
						da.getUrls().add(url);
						da.getMap().put(this.htmlDocument.title(), url);
						da.increaseFileName();
						return true;
					}
				}
			}
			//////////////////////////////////

			return false;

		} catch (IOException ioe) {
			// We were not successful in our HTTP request
			return false;
		}
	}

	public void makeFile(int fileName) {
		// Defensive coding. This method should only be used after a successful
		// crawl.
		if (this.htmlDocument == null) {
			System.out.println("!!ERROR! Call crawl() before performing analysis on the document");
			System.exit(0);
		}
		try {
			String article = this.htmlDocument.body().getElementsByAttributeValueMatching("id", "harmonyContainer")
					.text();
			if (!article.isEmpty()) {
				String file = pre + "/data/crawl/article/daum/" + String.format("%02d", fileName) + ".txt";

				FileWriter writer = new FileWriter(file, false);
				writer.write(this.htmlDocument.title() + "\n");
				writer.write(article + "\n");
				writer.close();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}