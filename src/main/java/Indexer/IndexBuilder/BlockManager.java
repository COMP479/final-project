package Indexer.IndexBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.StringTokenizer;

import Indexer.Models.Index;
import Indexer.Models.TermFrequencyPair;

public class BlockManager {
	public static void writeBlock(int block, Index index) {
		File file = new File("src/blocks/block"+Integer.toString(block)+".txt");

		writeToFile(index, file);
	}

	public static Index readBlock(String location) {
		Index index = new Index();
		try {
			FileReader inputStream = new FileReader(location);
			BufferedReader in = new BufferedReader(inputStream);
			
			//read block line by line
			String line = null;
			while ((line = in.readLine()) != null) {
				int termEnd = line.indexOf('[');
				int postingEnd = line.indexOf(']');
				//parse term
				String term = line.substring(0, termEnd);
				//make posting list a string of docids separated by space so it can be tokenized back into the treeset
				String postingString = line.substring(termEnd+1, postingEnd).replace(",", " ");
				//tokenize docids
				StringTokenizer tokenizer = new StringTokenizer(postingString, " ");
				
				//rebuild the index
				while (tokenizer.hasMoreTokens()) {
					String token = tokenizer.nextToken();
					if (!token.matches("")) {
						String[] pair = token.split("\\|");
						index.insert(Integer.parseInt(pair[0]), term, Integer.parseInt(pair[1]));
					}
				}
			}
			in.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return index;
	}
	
	public static void writeIndex(Index index, String location) {
		File file = new File("src/index/" + location);

		writeToFile(index, file);
	}

	private static void writeToFile(Index index, File file) {
		try {
			Writer writer = new FileWriter(file);

			StringBuffer theBlock = new StringBuffer();
			//write each term and posting list to a new line in the block
			for(String term : index.sort().keySet()) {
				theBlock.append(term+"[");
				for(TermFrequencyPair p : index.get(term)) {
					theBlock.append(p.toString() + ",");
				}
				theBlock.append("]\n");
			}
			writer.write(theBlock.toString());
			writer.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}
}
