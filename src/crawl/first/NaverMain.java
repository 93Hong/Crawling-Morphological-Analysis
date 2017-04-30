package crawl.first;

import java.io.File;

public class NaverMain implements Runnable {
	static int id = 0;
	private String pre = "/usr";//"c:";//
	
	public void run() {
		id += 1;
		System.out.print(id + " n_start || ");
		NaverCrawler spider = new NaverCrawler();

		String s = pre + "/data/crawl/article/naver";
		File f = new File(s);
		String[] entries = f.list();

		if (f.exists())
			for (String tmp : entries) {
				File currentFile = new File(f.getPath(), tmp);
				currentFile.delete();
			}
		else
			f.mkdir();

		spider.crawling("http://news.naver.com");
		System.out.print(id + " n_crawl end  || ");
		komoranAnalysis komo = new komoranAnalysis();
		komo.analysis("naver");
		
		MakeData mk = new MakeData();
		mk.makeData("naver");
		System.out.println(id + " n_end");
	}
}