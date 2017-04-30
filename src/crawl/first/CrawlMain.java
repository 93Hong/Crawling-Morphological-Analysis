package crawl.first;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CrawlMain {
	private final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	public static void main(String[] args) {
		Runnable naver = new NaverMain();
		//Runnable daum = new DaumMain();
		//Runnable twitter = new TwitMain();
		
		scheduler.scheduleAtFixedRate(naver, 0, 30, TimeUnit.MINUTES);
		//scheduler.scheduleAtFixedRate(daum, 2, 30, TimeUnit.MINUTES);
		//scheduler.scheduleAtFixedRate(twitter, 2, 30, TimeUnit.MINUTES);
		
	}
}