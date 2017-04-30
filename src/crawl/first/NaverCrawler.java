/*
 * Retrieve a web page (we'll call it a document) from a website
 * Collect all the links on that document
 * Collect all the words on that document
 * See if the word we're looking for is contained in the list of words
 * Visit the next link
 * Keep track of pages that we've already visited
 * Put a limit on the number of pages to search so this doesn't run for eternity.
 */

package crawl.first;

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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NaverCrawler {
	private static final int MAX_PAGES_TO_SEARCH = 700;
	public static int File_Name = 0;
	
	private static String enter = String.format("%n");
	private static String pre = "/usr"; //"c:";//
	private static String titlePath = pre + "/data/crawl/article/titles/";
	private static String path = pre + "/data/crawl/article/title/naver/";
	private static String rePath = pre + "/data/crawl/article/title/";
	
	public static Map<String, String> titleUrl = new HashMap<String, String>();
	public static ArrayList<String> titles;
	public static ArrayList<String> urls;

	// A collection that contains no duplicate elements.
	private Set<String> pagesVisited = new HashSet<String>();
	// An ordered collection (also known as a sequence).
	private List<String> pagesToVisit = new LinkedList<String>();
	
	public Map<String, String> getMap() {
		return titleUrl;
	}
	public ArrayList<String> getTitles() {
		return titles;
	}
	public ArrayList<String> getUrls() {
		return urls;
	}
	
	public void increaseFileName() {
		File_Name++;
	}
	
	public void crawling(String url) {
		titles = new ArrayList<>();
		urls = new ArrayList<>();
		int count = 0;
		while (this.pagesVisited.size() < MAX_PAGES_TO_SEARCH
				&& count < 1000 && File_Name < 60) {
			count++;
			String currentUrl;
			NaverCrawlerLeg leg = new NaverCrawlerLeg();

			if (this.pagesToVisit.isEmpty()) { // At first
				currentUrl = url;
				this.pagesVisited.add(url);
			} else { // Other
				currentUrl = this.nextUrl();
			}
			leg.crawl(currentUrl, File_Name);
			this.pagesToVisit.addAll(leg.getLinks());
		}

		// For title & url
		//mkTitleUrl();
		
		//mkTitleFile();
		//cumulTitleFile();
		
		File_Name = 0;
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
			
			FileWriter writer = new FileWriter(rePath + "naverTitles.txt", false);
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
	
	/**
	 * Returns the next URL to visit (in the order that they were found). We
	 * also do a check to make sure this method doesn't return a URL that has
	 * already been visited.
	 * 
	 * @return
	 */
	private String nextUrl() {
		String nextUrl;
		do {
			// Returns the element that was removed from the list.
			nextUrl = this.pagesToVisit.remove(0);

			// exception handling about next to visit Url.. good
		} while (nextUrl == null || nextUrl.length() == 0
				|| nextUrl.contains(".*(\\.(css|js|gif|jpg" + "|png|mp3|mp3|zip|gz))$")
				|| this.pagesVisited.contains(nextUrl)
				|| !nextUrl.contains("news.naver"));
		// if next url is empty (null or 0 length)
		// or already visited, remove in pagesToVisit

		this.pagesVisited.add(nextUrl);
		return nextUrl;
	}
}