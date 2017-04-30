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

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class DaumCrawler {
	private static final int MAX_PAGES_TO_SEARCH = 1500;
	public static int File_Name = 0;

	// A collection that contains no duplicate elements.
	private Set<String> pagesVisited = new HashSet<String>();

	// An ordered collection (also known as a sequence).
	// When the crawler visits a page it collects all the URLs on that page
	// and we just append them to this list.
	// For breadth-first approach.
	private List<String> pagesToVisit = new LinkedList<String>();

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

		while (this.pagesVisited.size() < MAX_PAGES_TO_SEARCH) {
			String currentUrl;
			DaumCrawlerLeg leg = new DaumCrawlerLeg();

			if (this.pagesToVisit.isEmpty()) { // At first
				currentUrl = url;
				this.pagesVisited.add(url);
			} else { // Other
				currentUrl = this.nextUrl();
			}
			if (leg.crawl(currentUrl, File_Name)) // Lots of stuff happening here
				File_Name++;

			// Appends all of the elements in the specified collection to the
			// end of this list
			this.pagesToVisit.addAll(leg.getLinks());
		}
		File_Name = 0;
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
				|| !nextUrl.contains("media.daum"));
		// if next url is empty (null or 0 length)
		// or already visited, remove in pagesToVisit

		this.pagesVisited.add(nextUrl);
		return nextUrl;
	}
}
