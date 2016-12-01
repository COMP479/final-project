package Indexer.Models;

public class TermFrequencyPair implements Comparable<TermFrequencyPair> {
	private Integer docId;
	private Integer frequency;
	
	public TermFrequencyPair(Integer docId, Integer frequency) {
		super();
		this.docId = docId;
		this.frequency = frequency;
	}
	
	public TermFrequencyPair() {}

	public Integer getDocId() {
		return docId;
	}

	public void setDocId(Integer docId) {
		this.docId = docId;
	}

	public Integer getFrequency() {
		return frequency;
	}

	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}
	
	public void add(Integer added) {
		this.frequency += added;
	}
	
	public String toString() {
		return Integer.toString(getDocId()) + "|" + Integer.toString(getFrequency());
	}
	
	@Override
	public boolean equals(Object o) {
		if ((o instanceof TermFrequencyPair) == false)
			return false;
		TermFrequencyPair tf = (TermFrequencyPair) o;
		return (tf.getDocId() == getDocId());
	}
	
	@Override
	public int hashCode() {
		return getDocId();
	}

	@Override
	public int compareTo(TermFrequencyPair p) {
		return new Integer(this.getDocId()).compareTo(p.getDocId());
	}
}