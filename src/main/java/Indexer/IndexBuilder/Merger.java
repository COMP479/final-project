package Indexer.IndexBuilder;

import java.io.File;
import java.util.TreeSet;

import Indexer.Models.Index;
import Indexer.Models.TermFrequencyPair;

public class Merger {
	public static void merge (String location) {
		System.out.println("Merging blocks...");
		//get a list of the block files
		Index index = new Index();
		File blocks = new File("src/blocks");
		String[] blockList = blocks.list();
		
		System.out.println("Index created...");
		for (String block : blockList) {
			//merge each block and read block into index
			index = mergeLists(index, BlockManager.readBlock("src/blocks/" + block));
		}
		
		System.out.println("Deleting blocks...");
		//delete blocks from disk to make directory ready for next index
		BlockManager.writeIndex(index, location);
		for (File file : blocks.listFiles()) {
			if (!file.getPath().toLowerCase().endsWith(".gitkeep") )
				file.delete();
		}
		
	}
	
	public static Index mergeLists(Index main, Index block) {
		//loop each term
		for(String term : block.keySet()) {
			TreeSet<TermFrequencyPair> pairs = block.get(term);
			for(TermFrequencyPair p : pairs) {
				main.insert(p.getDocId(), term, p.getFrequency());
			}
		}
		return main;
	}
}
