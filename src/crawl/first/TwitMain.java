package crawl.first;

import java.io.IOException;

public class TwitMain implements Runnable {
	//String path = "/usr/data/crawl/komoran/twitter/";
	//String resultPath = "/usr/data/crawl/result/twitter/";

	//@SuppressWarnings("resource")
	public void run() {
		try {
			Process proc = Runtime.getRuntime().exec("java -jar twitterBot.jar");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
//		try {
//			ConfigurationBuilder cb = new ConfigurationBuilder();
//			cb.setDebugEnabled(true).setOAuthConsumerKey("aCFJJz1yCSz6hfsDFqLLinr1K")
//					.setOAuthConsumerSecret("UvTRdKdKcxlhrLF03dyvlKbjcYTQVtZdcGSfWkc1SxhxQ5xtV6")
//					.setOAuthAccessToken("758116106577981440-viFuacC2wZX4QYYXZ6Iu5WCJkizVrNY")
//					.setOAuthAccessTokenSecret("u3OLWrZsb5koYWjhnm1sejz4tUoD4pVS9tASnv8OwQ2RS");
//			TwitterFactory tf = new TwitterFactory(cb.build());
//			Twitter twitter = tf.getInstance();
//
//			
//			//////////////////////////////////////////////////////////////////////////////////////
//			BufferedReader reader = new BufferedReader(
//					new InputStreamReader(new FileInputStream(path + "word.txt"), "euc-kr"));
//			String contents = reader.readLine();
//			if (contents == null)
//				return;
//			String word[] = contents.split("/");
//			reader.close();
//
//
//
//			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//			Date date = new Date();
//			Calendar calendar = Calendar.getInstance();
//			calendar.add(Calendar.MINUTE, -30);
//
//			//////////////////////////////////////////////////////////////////////////////////////
//			BufferedReader fileReader = new BufferedReader(new FileReader(new File(path + "index.txt")));
//			int index = Integer.parseInt(fileReader.readLine()) + 1;
//			fileReader.close();
//
//			if (index == 48) {// 48
//				index = 0;
//			}
//
//			FileWriter wr = new FileWriter(path + "index.txt");
//			wr.write(index + "");
//			wr.close();
//			/////////////////////////////////////////////////////////////////////////////////////////
//
//			int numOfSearch = word.length;
//			int limit = (int) Math.floor(15000 / numOfSearch);
//
//			HashMap<String, Integer> map = new HashMap<String, Integer>();
//
//			for (int i = 0; i < numOfSearch; i++) {
//				String search = word[i];
//				Query query = new Query(search);
//				query.setCount(100);
//				query.setSince(dateFormat.format(date)); // query.setSince("2016-08-12");
//				int count = 0;
//
//				QueryResult result;
//				// Search is rate limited at 180 queries per 15 minute window //
//				breakOut: do {
//					result = twitter.search(query);
//					List<Status> tweets = result.getTweets();
//					for (Status tweet : tweets) {
//						if (calendar.getTime().compareTo(tweet.getCreatedAt()) > 0)
//							break breakOut;
//						if (count >= limit)
//							break breakOut;
//						count++;
//					}
//				} while ((query = result.nextQuery()) != null);
//
//				map.put(word[i], count);
//			}
//
//			JSONObject js = new JSONObject(map);
//			FileWriter writer = new FileWriter(resultPath + index + ".txt");
//			if (js.isEmpty()) {
//				writer.write("{}");
//			} else
//				writer.write(js.toJSONString());
//			writer.close();
//
//			if (index == 47) {
//				CumulateData cum = new CumulateData();
//				cum.cumulate("twitter");
//			}
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
}
