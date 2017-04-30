package crawl.daum;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class CumulateData {
	private String pre = "/usr";//"c:";//
	String path = pre + "/data/crawl/result/";
	String resultPath = pre + "/data/crawl/result/";
	
	public void cumulate(String searchEngine) {
		path += searchEngine + "/";
		resultPath += searchEngine + "Cumulative/";
		
		try {
			BufferedReader fileReader;
			fileReader = new BufferedReader(new FileReader(new File(resultPath + "index.txt")));
			int index = Integer.parseInt(fileReader.readLine()) + 1;
			fileReader.close();
			
			if (index == 480) {//48
				index = 0;
			}
		
			FileWriter wr = new FileWriter(resultPath + "index.txt");
			wr.write(index + "");
			wr.close();
			
			List<Map<String, Integer>> products = new ArrayList<>();
			File dir = new File(path);

			for (File file : dir.listFiles()) {
				JSONParser parser = new JSONParser();
				Object obj = parser.parse(new FileReader(file));
				JSONObject jsonObejct = (JSONObject) obj;
				
				@SuppressWarnings("unchecked")
				HashMap<String,Integer> tmp = new ObjectMapper().readValue(jsonObejct.toJSONString(), HashMap.class);
				products.add(tmp);
			}

			Map<String, Integer> cumulativeMap = new HashMap<String, Integer>();
			for (Map<String, Integer> productMap : products) {
				for (Map.Entry<String, Integer> p : productMap.entrySet()) {

					if (cumulativeMap.containsKey(p.getKey())) {
						cumulativeMap.put(p.getKey(), cumulativeMap.get(p.getKey()) + p.getValue());
					} else {
						cumulativeMap.put(p.getKey(), p.getValue());
					}
				}
			}
			
			JSONObject js = new JSONObject(cumulativeMap);
			FileWriter writer = new FileWriter(resultPath + String.format("%03d", index) + ".txt");
			if (js.isEmpty()) {
				writer.write("{}");
			}
			else
				writer.write(js.toJSONString());
			writer.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}