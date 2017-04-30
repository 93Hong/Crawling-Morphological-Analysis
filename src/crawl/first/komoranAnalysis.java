package crawl.first;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kr.co.shineware.nlp.komoran.core.analyzer.Komoran;
import kr.co.shineware.util.common.model.Pair;

public class komoranAnalysis {
	private boolean isFirst;
	private String pre = "/usr";//"c:";//
	String path = pre + "/data/crawl/komoran/";
	String articles = pre + "/data/crawl/article/";
	String resultPath = pre + "/data/crawl/result/";
	String titlePath = pre + "/data/crawl/article/titles/";
	
	public void analysis(String searchEngine) {
		Komoran komoran = new Komoran(pre + "/data/models-full");
		komoran.setUserDic(pre + "/data/crawl/dic.user");
		
		MakeTitles mkTitle = new MakeTitles();
		int fileName = 0;
		
		int index = readIndex();
		mkEmptyDirectory(new File(titlePath + index));
		
		isFirst = true;
		try {
			path += searchEngine + "/";
			resultPath += searchEngine + "/";
			File dir = new File(articles + searchEngine);
			File[] articleFiles = dir.listFiles();
			Arrays.sort(articleFiles);
			
			if (dir.list().length == 0) {
				makeNewFile(searchEngine);
			}
			else {
				for (File file : articleFiles) {
					BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));//, "UTF-8"));
					String input = "";
					
					while ((reader.readLine()) != null) {
						input = reader.readLine();
					}

					@SuppressWarnings("unchecked")
					List<List<Pair<String, String>>> result = komoran.analyze(input);
					ArrayList<String> ar = new ArrayList<String>();
					
					for (List<Pair<String, String>> eojeolResult : result)
						for (Pair<String, String> wordMorph : eojeolResult)
							if (wordMorph.getSecond().startsWith("NN"))
								ar.add(wordMorph.getFirst());
					
					Set<String> hs = new HashSet<String>();
					hs.addAll(ar);
					
					mkTitle.makeFile(hs, fileName, index);
					fileName++;
					
					if (isFirst)
						makeNewFile(searchEngine);
					overwriteFile(searchEngine, hs);
					
					ar = null;
					reader.close();
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int readIndex() {
		try {
			BufferedReader fileReader;

			fileReader = new BufferedReader(new FileReader(new File(titlePath + "index.txt")));
			int index = (Integer.parseInt(fileReader.readLine()) + 1)%48;
			fileReader.close();

			FileWriter wr = new FileWriter(titlePath + "index.txt", false);
			wr.write(index + "");
			wr.close();

			return index;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	public boolean mkEmptyDirectory(File path) {
		if (!path.exists()) {
			path.mkdir();
			return false;
		}

		File[] files = path.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				mkEmptyDirectory(file);
			} else {
				file.delete();
			}
		}
		return true;
	}
	
	public void makeNewFile(String searchEngine) throws IOException {
		try {
			BufferedReader fileReader = new BufferedReader(
					new FileReader(new File(path + "index.txt")));
			int index = Integer.parseInt(fileReader.readLine()) + 1;
			fileReader.close();
			
			if (index == 48) {//48
				index = 0;
			}
		
			FileWriter wr = new FileWriter(path + "index.txt");
			wr.write(index + "");
			wr.close();

			FileWriter writer = new FileWriter(path + index + ".txt");
			writer.close();
			FileWriter writer2 = new FileWriter(resultPath + index + ".txt");
			writer2.close();
			
			isFirst = false;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void overwriteFile(String searchEngine, Set<String> hs) throws IOException {
		BufferedReader fileReader = new BufferedReader(
				new FileReader(new File(path + "index.txt")));
		int index = Integer.parseInt(fileReader.readLine());
		fileReader.close();
		
		try {
			FileWriter wr = new FileWriter(path + index + ".txt", true);

			for (String str : hs) {
				wr.write(str + ' ');
			}
			wr.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
