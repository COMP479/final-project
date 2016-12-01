package Indexer.IndexBuilder;

import java.util.Arrays;
import java.util.List;

import Indexer.Models.Index;

public class IndexStatistics {
	//index locations
	public static List<String> indexes = Arrays.asList("unfilteredIndex.txt", "nonumbersIndex.txt", "casefoldedIndex.txt", "stopw30Index.txt", "stopw150Index.txt"
			);
	
	public static void compareCompression() {
		System.out.println("Generating compression statistics...");
		int count = 0;
		double[] indexWords = new double[5];
		double[] indexPostings = new double[5];
		String[] methods = {"Unfiltered", "No Numbers", "Case Folded", "30 Stop Words", "150 Stop Words"};
		
		//loop each index file
		for (String indexLocation : indexes) {
			Index index = BlockManager.readBlock("src/index/" + indexLocation);
			//calculate index statistics
			indexWords[count] = index.size();
			indexPostings[count] = index.postingSize();
			
			//print statistics to console
			if(count == 0) {
				System.out.println(methods[count] + " Word Types (Terms): " + Math.round(indexWords[count]) + " Change: - Cum. Change: - "
						+ "Non-Positional Postings: " + Math.round(indexPostings[count]) + " Change: - Cum. Change: - ");
			} else {
				//perform compression comparison
				int wordChange = (int) Math.ceil((((indexWords[count-1]/indexWords[count]) - 1) * 100));
				int cumWordChange = (int) Math.ceil((((indexWords[0]/indexWords[count]) - 1) * 100));
				int postingChange = (int) Math.ceil((((indexPostings[count-1]/indexPostings[count]) - 1) * 100));
				int cumPostingChange = (int) Math.ceil((((indexPostings[0]/indexPostings[count]) - 1) * 100));
				
				System.out.println(methods[count] + " Word Types (Terms): " + Math.round(indexWords[count]) + " Change: " + wordChange + " Cum. Change: " + cumWordChange
						+ " Non-Positional Postings: " + Math.round(indexPostings[count]) + " Change: " + postingChange + " Cum. Change: " + cumPostingChange);
			}
			count++;
		}
	}
}
