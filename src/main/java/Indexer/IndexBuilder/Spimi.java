package Indexer.IndexBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import Indexer.Enums.CompressionLevel;
import Indexer.Models.Collection;
import Indexer.Models.CollectionStatistics;
import Indexer.Models.Document;
import Indexer.Models.DocumentArticle;
import Indexer.Models.Index;

public class Spimi {
	public static void run (Collection collection, CompressionLevel level) {
		//trigger garbage collection before getting initial memory
		System.gc();
		CollectionStatistics stats = new CollectionStatistics();
		long initialMemory = java.lang.Runtime.getRuntime().freeMemory();
		long blockSizeLimit = 200;
		Index index = new Index();
		int blockCount = 0;
		int docWordCount = 0;
		
		//loop every document in reuters
		System.out.println("Tokenizing, compressing, and creating blocks from collection...");
		for (DocumentArticle document : collection.getDocuments()) {
			Document doc = new Document();
			
			//build document with ID and compressed tokens
			doc.setId(document.getId());
			doc.setTokens(Compress.compress(tokenize(document.getContent().getBody()), level));
			
			//loop every token in document
			for(String term : doc.getTokens()) {
				long currentMemory = java.lang.Runtime.getRuntime().freeMemory();
				long usedMemory = initialMemory - currentMemory;
				
				//check if memory block size limit is reached
				if ((usedMemory/1024/1024) > blockSizeLimit) {
					//write block, free memory, create new index
					BlockManager.writeBlock(blockCount, index);
					blockCount++;
					index = null;
					System.gc();
					index = new Index();
				}
				
				//insert posting
				index.insert(doc.getId(), term, 1);
				docWordCount++;
			}
			//keep track of doc lengths
			stats.insert(doc.getId(), docWordCount);
			docWordCount = 0;
		}
		
		//if final index has data write the final block
		if(!index.isEmpty()){
			BlockManager.writeBlock(blockCount, index);
			blockCount++;
			index = null;
			System.gc();
			index = new Index();
		}
		stats.writeStatistics(level);
	}
	
	public static List<String> tokenize(String content) {
		List<String> tokens = new ArrayList<String>();
		
		//check if document is empty
		if(content != null) {
			//tokenize on newlines, / and spaces
			StringTokenizer tokenizer = new StringTokenizer(content, "\n/ ");
			
			//create token list
			while (tokenizer.hasMoreTokens()) {
				tokens.add(tokenizer.nextToken());
			}
		}
		
		return tokens;
	}
	
	
}
