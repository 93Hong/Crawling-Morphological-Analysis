package crawl.first;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

import org.json.simple.JSONObject;

public class MakeData {
	private String pre = "/usr";//"c:";//
	String path = pre + "/data/crawl/komoran/";
	String resultPath = pre + "/data/crawl/result/";
	
	public void makeData(String searchEngine) {
		Scanner s;
		path += searchEngine + "/";
		resultPath += searchEngine + "/";
		try {
			BufferedReader fileReader = new BufferedReader(new FileReader(new File(path + "index.txt")));
			int index = Integer.parseInt(fileReader.readLine());
			fileReader.close();
			
			s = new Scanner(new File(path + index + ".txt"));
			//("C:/Data/crawl/komoran/" + index + ".txt"));//----------------------

			ArrayList<String> list = new ArrayList<String>();

			while (s.hasNext()) {
				list.add(s.next());
			}
			s.close();
			
			Collections.sort(list);
			
			int count = 1;
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			for (int i = 0; i < list.size()-1; i++) {
				if (list.get(i).equals(list.get(i+1))) {
					count++;
				} else {
					map.put(list.get(i), count);
					count = 1;
				}
			}
			
			JSONObject js = new JSONObject(map);
			FileWriter writer = new FileWriter(resultPath + index + ".txt");
			//("C:/Data/crawl/komoran/" + index + "_Result.txt");
			if (js.isEmpty()) {
				writer.write("{}");
			}
			else
				writer.write(js.toJSONString());
			writer.close();
			
			CumulateData cum = new CumulateData();
			cum.cumulate(searchEngine);
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}