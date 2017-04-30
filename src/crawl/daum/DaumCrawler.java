/*
 * Retrieve a web page (we'll call it a document) from a website
 * Collect all the links on that document
 * Collect all the words on that document
 * See if the word we're looking for is contained in the list of words
 * Visit the next link
 * Keep track of pages that we've already visited
 * Put a limit on the number of pages to search so this doesn't run for eternity.
 */

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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DaumCrawler {
	private static final int MAX_PAGES_TO_SEARCH = 500;
	public static int File_Name = 0;
	
	private static String enter = String.format("%n");
	private static String pre = "/usr";//"c:";//
	private static String path = pre + "/data/crawl/article/title/daum/";
	private static String rePath = pre + "/data/crawl/article/title/";

	public static Map<String, String> titleUrl = new HashMap<String, String>();
	
	// A collection that contains no duplicate elements.
	private Set<String> pagesVisited = new HashSet<String>();
	// An ordered collection (also known as a sequence).
	private List<String> pagesToVisit = new LinkedList<String>();

	public Map<String, String> getMap() {
		return titleUrl;
	}
	
	public void increaseFileName() {
		File_Name++;
	}
	
	/**
	 * Our main launching point for the Spider's functionality. Internally it
	 * creates spider legs that make an HTTP request and parse the response (the
	 * web page).
	 * 
	 * @param url
	 *            - The starting point of the spider
	 * @param searchWord
	 *            - The word or string that you are searching for
	 */

	public void crawling(String url) {
		int count = 0;
		while (this.pagesVisited.size() < MAX_PAGES_TO_SEARCH
				&& count < 1000) {
			count++;
			String currentUrl;
			DaumCrawlerLeg leg = new DaumCrawlerLeg();

			if (this.pagesToVisit.isEmpty()) { // At first
				currentUrl = url;
				this.pagesVisited.add(url);
			} else { // Other
				currentUrl = this.nextUrl();
			}
			leg.crawl(currentUrl, File_Name);
			//if(leg.crawl(currentUrl, File_Name)) // Lots of stuff happening here. Look at the
			//	File_Name++;					 // crawl method in SpiderLeg

			// Appends all of the elements in the specified collection to the
			// end of this list
			System.out.println(count);
			this.pagesToVisit.addAll(leg.getLinks());
		}
		mkTitleFile();
		cumulTitleFile();
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
			//System.out.println(allLines.size());
			
			FileWriter writer = new FileWriter(rePath + "daumTitles.txt", false);
			for (int i = 0; i < allLines.size(); i++) {
				writer.write(allLines.get(i) + enter);
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
				//System.out.println(entry.getKey() + ", " + entry.getValue());
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
				|| !nextUrl.contains("v.media.daum"));
		// if next url is empty (null or 0 length)
		// or already visited, remove in pagesToVisit

		this.pagesVisited.add(nextUrl);
		return nextUrl;
	}
}