package crawl.first;

import java.io.File;

public class DaumMain implements Runnable {
	@Override
	public void run() {
		DaumCrawler spider = new DaumCrawler();
		
		/////////////////////////////////////////////////////////////
		String s = "/usr/data/crawl/article/daum";
		//"C:/Data/crawl/article/daum";//-----------------------------
		File f = new File(s);
		String[]entries = f.list();
		
		if (f.exists())
			for(String tmp: entries){
			    File currentFile = new File(f.getPath(), tmp);
			    currentFile.delete();
			}
		else
			f.mkdir();
		//////////////////////////////////////////////////////////////
		
		spider.crawling("http://media.daum.net");
		komoranAnalysis komo = new komoranAnalysis();
		komo.analysis("daum");
		
		MakeData mk = new MakeData();
		mk.makeData("daum");
	}
}