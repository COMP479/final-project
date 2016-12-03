package Sentiment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import Indexer.Enums.CompressionLevel;

public class SentimentAnalysis {
	public static void analyze(CompressionLevel level) {
		String[] depts = { "biology", "chemistry", "exercise-science", "geography-planning-environment", "math-stats", "physics", "psychology", "science-college" };
		Map<String, Integer> deptScores = new HashMap<String, Integer>();
		deptScores = buildMap(deptScores, depts);
		String svFilename =  System.getProperty("user.dir") + "/src/sentiment/sentimentValues" + level;
		
		try {
			FileReader inputStream = new FileReader(svFilename);
			BufferedReader in = new BufferedReader(inputStream);
			
			String line = null;
			while ((line = in.readLine()) != null) {
				int fileStart = line.indexOf('[');
				int fileEnd = line.indexOf(',');
				int valueEnd = line.indexOf(']');
				String file = line.substring(fileStart+1, fileEnd);
				int score = Integer.parseInt(line.substring(fileEnd+1, valueEnd));
				
				for (String dept : depts) {
					if (file.contains(dept)) {
						int currScore = deptScores.get(dept);
						deptScores.put(dept, currScore + score);
						break;
					}
				}
			}
			in.close();
			inputStream.close();
			
			writeDepartments(deptScores, level);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private static Map<String, Integer> buildMap(Map<String, Integer> deptScores, String[] depts) {
		for(String dept : depts) {
			deptScores.put(dept, 0);
		}
		return deptScores;
	}
	
	private static void writeDepartments(Map<String, Integer> deptScores, CompressionLevel level) {
		try {
			Writer writer = new FileWriter(System.getProperty("user.dir") + "/src/sentiment/departmentValues" + level);

			StringBuffer sb = new StringBuffer();
			//write each term and posting list to a new line in the block
			Iterator<Entry<String, Integer>> it = deptScores.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry<String, Integer> pair = (Map.Entry<String, Integer>)it.next();
		        sb.append("[" + pair.getKey() + "," + pair.getValue() + "]\n");
		    }
			
			writer.write(sb.toString());
			writer.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
