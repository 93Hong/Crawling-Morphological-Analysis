package crawl.first;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Set;

public class MakeTitles {
	private String pre = "/usr"; //"c:";
	private String path = pre + "/data/crawl/article/titles/";
	private String enter = String.format("%n");

	public void makeFile(Set<String> hs, int fileName, int index) {
		String filePath = path + index + "/";

		NaverCrawler na = new NaverCrawler();

		try {
			String file = filePath + fileName + "_n.txt";
			FileWriter writer = new FileWriter(file, false);
			writer.write(na.getTitles().get(fileName) + enter);
			writer.write(na.getUrls().get(fileName) + enter);
			writer.write(hs.toString() + enter);
			writer.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
