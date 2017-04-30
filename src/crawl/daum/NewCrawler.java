package crawl.daum;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class NewCrawler {
	private final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
	public static int File_Name = 0;
	
	private static String enter = String.format("%n");
	private static String pre = "/usr";//"c:";//
	private static String titlePath = pre + "/data/crawl/article/titles/";
	private static String path = pre + "/data/crawl/article/title/daum/";
	private static String rePath = pre + "/data/crawl/article/title/";

	private Set<String> searchLink = new HashSet<String>();
	public static Map<String, String> titleUrl = new HashMap<String, String>();
	public static ArrayList<String> titles; // article's title
	public static ArrayList<String> urls; // article's url
	
	public void crawling() {
		titles = new ArrayList<>();
		urls = new ArrayList<>();
		
		for (int i = 1; i < 21; i++) {
			crawl("http://media.daum.net/breakingnews?page=" + i);
		}

		Iterator<String> it = searchLink.iterator();
		NewCrawlerLeg leg = new NewCrawlerLeg();

		while (it.hasNext() && File_Name < 80) {
			String s = it.next();
			leg.crawl(s, File_Name);
		}
		
		// For title & url
		//mkTitleUrl();
		
		//mkTitleFile();
		//cumulTitleFile();
		File_Name = 0;
	}

	public boolean crawl(String url) {
		try {
			Connection connection = Jsoup.connect(url).userAgent(this.USER_AGENT);
			Document htmlDocument = connection.get();

			if (connection.response().statusCode() != 200) {
				return false;
			}
			if (!connection.response().contentType().contains("text/html")) {
				System.out.println("**Failure** Retrieved something other than HTML");
				return false;
			}

			// specify all the URLs on a page such as a[href]
			Elements linksOnPage = htmlDocument.getElementsByAttributeValueMatching("class", "list_news2 list_allnews")
					.select("a[href]");

			if (linksOnPage.size() == 0)
				return true;

			for (Element link : linksOnPage) {
				if (link.toString().contains("v.media.daum.net/v"))
					searchLink.add(link.absUrl("href"));
			}

		} catch (IOException ioe) {
			// We were not successful in our HTTP request
			return false;
		}
		return false;
	}
	
	public Map<String, String> getMap() {
		return titleUrl;
	}
	
	public void increaseFileName() {
		File_Name++;
	}
	
	public ArrayList<String> getTitles() {
		return titles;
	}
	public ArrayList<String> getUrls() {
		return urls;
	}
	
	public void mkTitleUrl() {
		try {
			FileWriter writer = new FileWriter(titlePath + "titles.txt");
			for (int i = 0; i < titles.size(); i++) {
				writer.write(titles.get(i) + enter);
				writer.write(urls.get(i) + enter);
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void cumulTitleFile() {
		try {
			List<String> allLines = new ArrayList<String>();
			File dir = new File(path);
			for (String file : dir.list()) {
				if (file.equals("index.txt"))
					continue;
				List<String> lines = Files.readAllLines(Paths.get(path + file), StandardCharsets.UTF_8);
				allLines.addAll(lines);
			}
			//System.out.println(allLines.size());
			
			FileWriter writer = new FileWriter(rePath + "daumTitles.txt", false);
			for (int i = 0; i < allLines.size(); i++) {
				writer.write(allLines.get(i) + enter);
			}
			writer.close();
			titleUrl = null;
			titleUrl = new HashMap<String, String>();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@SuppressWarnings("rawtypes")
	public static void mkTitleFile() {
		try {
			BufferedReader fileReader = new BufferedReader(new FileReader(new File(path + "index.txt")));
			int index = Integer.parseInt(fileReader.readLine()) + 1;
			index = index % 48;
			fileReader.close();

			FileWriter wr = new FileWriter(path + "index.txt", false);
			wr.write(index + "");
			wr.close();

			FileWriter writer = new FileWriter(path + index + ".txt");
			for (Map.Entry entry : titleUrl.entrySet()) {
				writer.write(entry.getValue() + ", " + entry.getKey() + enter);
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
