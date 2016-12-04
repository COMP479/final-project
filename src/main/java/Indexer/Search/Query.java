package Indexer.Search;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.TreeSet;

import Indexer.Enums.CompressionLevel;
import Indexer.IndexBuilder.BlockManager;
import Indexer.IndexBuilder.Compress;
import Indexer.IndexBuilder.Spimi;
import Indexer.Models.CollectionStatistics;
import Indexer.Models.Index;
import Indexer.Models.TermFrequencyPair;

public class Query {
	private static Map<Integer, String> loadTitles() {
		Map<Integer, String> titles = new HashMap<Integer, String>();
		try {
			FileReader inputStream = new FileReader(System.getProperty("user.dir") + "/src/docids/titlesAndDocs");
			BufferedReader in = new BufferedReader(inputStream);
			
			String line = null;
			while ((line = in.readLine()) != null) {
				int fileStart = line.indexOf('[');
				int fileEnd = line.indexOf(',');
				int valueEnd = line.indexOf(']');
				int id = Integer.parseInt(line.substring(fileStart+1, fileEnd));
				String title = line.substring(fileEnd+1, valueEnd);
				titles.put(id, title);
			}
			in.close();
			inputStream.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		return titles;
	}
	
	//bm25 ranking
	private static double makeRanking(Integer id, double documentFrequency, double termFrequency, CollectionStatistics stats) {
		double N = stats.size();
		double k1 = 1.4;
		double b = 0.7;
		double Lave = stats.getAverage();
		double Ld = stats.get(id);
		double ranking = Math.log((N/documentFrequency)) * (((k1 + 1) * termFrequency)/(k1 * ((1 - b) + (b * (Ld/Lave))) + termFrequency));
		return ranking;
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		Map<Integer, String> titles = loadTitles();
		CollectionStatistics stats = new CollectionStatistics();
		stats.readStatistics(CompressionLevel.CASE_FOLDING);
		
		//read index
		Index invertedIndex = BlockManager.readBlock("src/index/casefoldedIndex.txt");
		
		//ask for user input
		Scanner in = new Scanner(System.in);
		System.out.println("Enter a search query:");
		String query = in.nextLine();
		in.close();
		
		HashMap<Integer, Double> rankings = new HashMap<Integer, Double>();
		
		//parse query
		List<String> queryTokens = Compress.compress(Spimi.tokenize(query), CompressionLevel.CASE_FOLDING);
		//loop term
		for(String token : queryTokens) {
			//get docs
			TreeSet<TermFrequencyPair> postingList = invertedIndex.get(token);
			//loop docs
			for(TermFrequencyPair posting : postingList) {
				//get current ranking for doc
				double currentRanking = (rankings.containsKey(posting.getDocId())) ? rankings.get(posting.getDocId()) : 0;
				//update ranking
				currentRanking = currentRanking + makeRanking(posting.getDocId(), postingList.size(), posting.getFrequency(), stats);
				rankings.put(posting.getDocId(), currentRanking);
			}
		}
		
		//sort by ranking
		List<Entry<Integer, Double>> entries = new ArrayList<Entry<Integer, Double>>(rankings.entrySet());
		Collections.sort(entries, new Comparator<Entry<Integer, Double>>() {
		    public int compare(Entry<Integer, Double> e1, Entry<Integer, Double> e2) {
		        return e2.getValue().compareTo(e1.getValue());
		    }
		});
		
		System.out.println("These are the matching documents:");
		for (Entry<Integer, Double> entry : entries) {
			System.out.println("Document: " + titles.get(entry.getKey()) + " Rank Weight: " + entry.getValue());
		}
	}
}
