package Indexer.Models;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Index {
	//Index object
	TreeMap<String, TreeSet<TermFrequencyPair>> index = new TreeMap<String, TreeSet<TermFrequencyPair>>();
	
	public void insert(Integer id, String term, Integer add) {
		boolean found = false;
		if (!index.containsKey(term)) {
			//insert new term
			TreeSet<TermFrequencyPair> postings = new TreeSet<TermFrequencyPair>();
			postings.add(new TermFrequencyPair(id, add));
			index.put(term,  postings);
		} else {
			TreeSet<TermFrequencyPair> set = index.get(term);
			//insert new doc in posting list set
			
			for (TermFrequencyPair p : set) {
				if (p.getDocId() == id) {
					p.add(add);
					found = true;
					break;
				}
			}
			
			if(!found) {
				index.get(term).add(new TermFrequencyPair(id, add));
			}
		}
	}
	
	//unused sort
	public Map<String, TreeSet<TermFrequencyPair>> sort() {
        Map<String, TreeSet<TermFrequencyPair>> sorted = new TreeMap<String,  TreeSet<TermFrequencyPair>>(index);
        return sorted;
	}
	
	public TreeSet<TermFrequencyPair> get(String s){
		return index.get(s);
	}
	
	//Count total posting size
	public int postingSize() {
		int size = 0;
		//loop every entry in treemap index
		for (Map.Entry<String, TreeSet<TermFrequencyPair>> entry : index.entrySet()) {
			//count size of each posting list
			TreeSet<TermFrequencyPair> set = entry.getValue();
			size += set.size();
		}
		return size;
	}
	
	public void clear() {
		index.clear();
	}
	
	public boolean isEmpty(){
		return index.isEmpty();
	}
	
	public int size(){
		return index.size();
	}
	
	public Set<String> keySet() {
		return index.keySet();
	}
	
	public boolean containsKey(String s){
		return index.containsKey(s);
	}
	
	public void put(String term, TreeSet<TermFrequencyPair> postings) {
		index.put(term,  postings);
	}
}
