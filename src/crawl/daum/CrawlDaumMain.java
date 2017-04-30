package crawl.daum;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CrawlDaumMain {
	private final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
	public static void main(String[] args) {
		Runnable daum = new DaumMain();
		
		scheduler.scheduleAtFixedRate(daum, 0, 30, TimeUnit.MINUTES);
	}
}