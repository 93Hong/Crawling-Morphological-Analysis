package crawl.daum;

import java.io.File;

public class DaumMain implements Runnable{
	static int id = 0;
	private String pre = "/usr";//"c:";//
	
	public void run() {
		id += 1;
		System.out.print(id + " d_start  || ");
		//DaumCrawler spider = new DaumCrawler();
		NewCrawler spider = new NewCrawler();
		
		/////////////////////////////////////////////////////////////
		String s = pre + "/data/crawl/article/daum";
		File f = new File(s);
		String[] entries = f.list();
		
		if (f.exists())
			for (String tmp : entries) {
			    File currentFile = new File(f.getPath(), tmp);
			    currentFile.delete();
			}
		else
			f.mkdir();
	
		spider.crawling();
		System.out.print(id + " d_crawl end  || ");
		komoranAnalysis komo = new komoranAnalysis();
		komo.analysis("daum");
		
		MakeData mk = new MakeData();
		mk.makeData("daum");
		System.out.println(id + " d_end");
	}
}