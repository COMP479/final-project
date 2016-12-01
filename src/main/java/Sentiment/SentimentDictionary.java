package Sentiment;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.TreeMap;

//uses singleton pattern
public class SentimentDictionary {

	private static SentimentDictionary instance = new SentimentDictionary();
	
	//term + sentiment value pairs
	private TreeMap<String, Integer> Dictionary = new TreeMap<String, Integer>();

	//Get the only object available
	public static SentimentDictionary getInstance(){
	      return instance;
	}
	
	//private constructor only called once
	private SentimentDictionary(){
		
		System.out.println("Building Sentiment Dictionary...");
		//read from aFinn file and add all terms and values to Dictionary
		String aFinnPath =  System.getProperty("user.dir") + "/src/sentiment/AFINN-111.txt";
		
		try {
			FileReader inputStream = new FileReader(aFinnPath);
			BufferedReader in = new BufferedReader(inputStream);
			
			String line = null;
			while ((line = in.readLine()) != null) {
				//some sentiments are two words long so need to get all words
				String term = line.substring(0, line.lastIndexOf("\t"));
				int value = Integer.parseInt(line.substring(line.lastIndexOf("\t") + 1));
				this.insert(term, value);
			}
			in.close();
			inputStream.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private void insert(String term, int value){
		Dictionary.put(term, value);
	}
	
	public int getValue(String term){
		if(Dictionary.containsKey(term)){
			return Dictionary.get(term);
		}
		return 0;
	}
}
