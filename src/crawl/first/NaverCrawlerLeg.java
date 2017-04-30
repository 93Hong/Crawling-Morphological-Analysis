package crawl.first;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NaverCrawlerLeg {
	// We'll use a fake USER_AGENT so the web server thinks the robot is a
	// normal web browser.
	private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
	private List<String> links = new LinkedList<String>();
	private Document htmlDocument;
	private String pre = "/usr";//"c:";//
	NaverCrawler na = new NaverCrawler();

	/**
	 * This performs all the work. It makes an HTTP request, checks the
	 * response, and then gathers up all the links on the page. Perform a
	 * searchForWord after the successful crawl
	 * 
	 * @param url
	 *            - The URL to visit
	 * @return whether or not the crawl was successful
	 */
	public boolean crawl(String url, int File_Name) {
		try {
			Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
			Document htmlDocument = connection.get();
			this.htmlDocument = htmlDocument;

			// 200 is the HTTP OK status code indicating that everything is
			// great.
			if (connection.response().statusCode() == 200) {
				//System.out.println("\nNaver : " + url);
			}
			if (!connection.response().contentType().contains("text/html")) {
				System.out.println("**Failure** Retrieved something other than HTML");
				return false;
			}

			// specify all the URLs on a page such as a[href]
			Elements linksOnPage = htmlDocument.select("a[href]");
			if (linksOnPage.size() == 0)
				return true;

			for (Element link : linksOnPage) {
				this.links.add(link.absUrl("href"));
			}

			String article = null;
			if (this.htmlDocument != null)
				if (this.htmlDocument.body() != null)
					if (this.htmlDocument.body().getElementsByAttributeValueMatching("id", "articleBodyContents") != null)
						article = this.htmlDocument.body().getElementsByAttributeValueMatching("id", "articleBodyContents").text();
			
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MINUTE, -30);
			
			if (article != null && !article.isEmpty()) {
				String time[] = this.htmlDocument.body().getElementsByAttributeValueMatching("class", "t11")
						.text().split(" ", 3);
				//this.htmlDocument.title();
				String articleTime = time[0] + " " + time[1];
				String thirtyMinAgo = dateFormat.format(calendar.getTime());
				
				if (thirtyMinAgo.compareTo(articleTime) < 0 && !na.getMap().containsKey(this.htmlDocument.title())) {
					makeFile(File_Name);
					na.getTitles().add(this.htmlDocument.title());
					na.getUrls().add(url);
					na.getMap().put(this.htmlDocument.title(), url);
					na.increaseFileName();
					return true;
				}
			}
			return false;
			

		} catch (IOException ioe) {
			// We were not successful in our HTTP request
			return false;
		}
	}

	/**
	 * Performs a search on the body of on the HTML document that is retrieved.
	 * This method should only be called after a successful crawl.
	 * 
	 * @param searchWord
	 *            - The word or string to look for
	 * @return whether or not the word was found
	 */

	public void makeFile(int fileName) {
		// Defensive coding. This method should only be used after a successful
		// crawl.
		if (this.htmlDocument == null) {
			System.out.println("ERROR! Call crawl() before performing analysis on the document");
			System.exit(0);
		}
		try {
			String article = this.htmlDocument.body().getElementsByAttributeValueMatching("id", "articleBodyContents")
					.text();
			if (!article.isEmpty()) {
				String file = pre + "/data/crawl/article/naver/" + String.format("%02d", fileName) + ".txt";

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

	public List<String> getLinks() {
		return this.links;
	}

}