package Indexer.Models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Set;
import java.util.TreeMap;

import Indexer.Enums.CompressionLevel;

public class CollectionStatistics {
	TreeMap<Integer, Integer> collection = new TreeMap<Integer, Integer>();
	
	public void insert(Integer id, Integer length) {
		collection.put(id, length);
	}
	
	public int size(){
		return collection.size();
	}
	
	public Integer get(Integer s){
		return collection.get(s);
	}
	
	public double getAverage() {
		double size = collection.size();
		double length = 0;
		Set<Integer> ids = collection.keySet();
		
		for(Integer id : ids) {
			length = length + collection.get(id);
		}
		
		return (length/size);
	}
	
	public void writeStatistics(CompressionLevel level) {
		File file = new File("src/stats/" + getFileName(level) + ".txt");
		
		try { 
			Writer writer = new FileWriter(file);
			
			StringBuffer stats = new StringBuffer();
			
			for(Integer id : collection.keySet()) {
				stats.append(id + ":" + collection.get(id) + "\n");
			}
			
			writer.write(stats.toString());
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		}
	}
	
	public void readStatistics(CompressionLevel level) {
		try {
			FileReader inputStream = new FileReader("src/stats/" + getFileName(level) + ".txt");
			BufferedReader in = new BufferedReader(inputStream);
			
			String line = null;
			while ((line = in.readLine()) != null) {
				String[] docInfo = line.split(":");
				this.insert(Integer.parseInt(docInfo[0]), Integer.parseInt(docInfo[1]));
			}
			
			in.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private String getFileName(CompressionLevel level) {
		switch (level) {
			case UNFILTERED:
				return "unfilteredIndex";
			case CASE_FOLDING:
				return "casefoldedIndex";
			case NO_NUMBERS:
				return "nonumbersIndex";
			case STOPW_150:
				return "stopw150Index";
			case STOPW_30:
				return "stopw30Index";
			default:
				return "unfilteredIndex";
		}
	}
}