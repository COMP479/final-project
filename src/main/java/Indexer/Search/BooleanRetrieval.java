package Indexer.Search;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
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

public class BooleanRetrieval {
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
		CollectionStatistics stats = new CollectionStatistics();
		stats.readStatistics(CompressionLevel.UNFILTERED);
		
		//read index
		Index invertedIndex = BlockManager.readBlock("index/unfilteredIndex.txt");
		
		//ask for user input
		while (true) {
		Scanner in = new Scanner(System.in);
		System.out.println("Enter a search query (quit to exit):");
		String query = in.nextLine();
		in.close();
		if (query.matches("quit")) {
			break;
		}
		
		HashMap<Integer, Double> rankings = new HashMap<Integer, Double>();
		
		//parse query
		List<String> queryTokens = Compress.compress(Spimi.tokenize(query), CompressionLevel.UNFILTERED);
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
			System.out.println("Document: " + entry.getKey() + " Rank Weight: " + entry.getValue());
		}
		}
	}
}
