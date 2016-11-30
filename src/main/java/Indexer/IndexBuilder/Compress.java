package Indexer.IndexBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Indexer.Enums.CompressionLevel;

public class Compress {
	public static List<String> stopWords30 = Arrays.asList("i","a","about","an","and","are","as","at",
			"be","by","for","from","has","in","is","it","of","on","that","the","this","to","was","were",
			"what","when","where","who","will","with"
			);
	
	public static List<String> stopWords150 = Arrays.asList("a","about","above","after","again","against",
			"all","am","an","and","any","are","as","at","be","because","been","before","being","below",
			"between","both","but","by","can","cannot","could","did","do","does","doing","don't","down",
			"during","each","few","for","from","further","had","has","have","haven't","having","he","he's",
			"her","here","hers","herself","him","himself","his","hot","how","i","i'd","i'll","i'm","i've",
			"if","in","into","is","isn't","it","it's","its","itself","let's","me","more","most","my","no",
			"nor","not","of","off","on","once","only","or","other","ought","our","ours","ourselves","out",
			"over","own","same","she","should","so","some","such","than","that","that's","the","their",
			"theirs","them","themselves","then","there","there's","these","they","they'll","they're",
			"they've","this","those","through","to","too","under","until","up","very","was","wasn't","we",
			"we'd","we'll","we're","we've","were","weren't","what","what's","when","where","which","while",
			"who","whom","why","with","won't","would","wouldn't","you","you're","your","yours","yourself",
			"reuter"
			);
	
	public static List<String> compress(List<String> tokens, CompressionLevel level) {
		List<String> tmp = new ArrayList<String>();
		String compToken = "";
		for (String token : tokens) {
			//always remove punctuation
			compToken = stripPunctuation(token);
			
			//apply different levels of compression
			if(!level.equals(CompressionLevel.UNFILTERED)) {
				compToken = stripNumeric(compToken);
			}
			
			if(!level.equals(CompressionLevel.UNFILTERED) && !level.equals(CompressionLevel.NO_NUMBERS)) {
				compToken = caseFold(compToken);
			}
			
			if(level.equals(CompressionLevel.STOPW_30)) {
				compToken = stripStopWord(compToken, stopWords30);
			}
			
			if(level.equals(CompressionLevel.STOPW_150)) {
				compToken = stripStopWord(compToken, stopWords150);
			}
			
			//dont add empty tokens to the index
			if(!compToken.matches("")) {
				tmp.add(compToken);
			}
		}
		
		return tmp;
	}
	
	public static String stripPunctuation(String token){
		//remove characters that are not letters or numbers
		return token.replaceAll("[^a-zA-Z0-9]+", "");
	}
	
	public static String caseFold(String token){
		//remove upper case
		return token.toLowerCase();
	}
	
	public static String stripStopWord(String token, List<String> stopWords){
		//make token empty if token is in the stop word list
		return stopWords.contains(token) ? "" : token;
	}
	
	public static String stripNumeric(String token)
	{
		//if token only has numbers make it empty
		return token.matches("-?\\d+(\\.\\d+)?") ? "" : token;
	}
}
