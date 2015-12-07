package com.ir.queryexpansion.model;

public class PostingEntry implements Comparable<PostingEntry>{

	private Document doc;
	private int docTermFrequency;
	
	public PostingEntry(Document doc, int docTermFrequency) {
		super();
		this.doc = doc;
		this.docTermFrequency = docTermFrequency;
	}
	
	public PostingEntry() {
		super();
	}

	public Document getDoc() {
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

	public int getDocTermFrequency() {
		return docTermFrequency;
	}

	public void setDocTermFrequency(int docTermFrequency) {
		this.docTermFrequency = docTermFrequency;
	}

	@Override
	public String toString() {
		return "PostingEntry [doc=" + doc + ", docTermFrequency=" + docTermFrequency + "]";
	}
	
	@Override
	public int compareTo(PostingEntry o) {		
		return Integer.compare(this.doc.getDocId(), o.doc.getDocId());
	}		
}
