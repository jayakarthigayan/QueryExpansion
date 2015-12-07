package com.ir.queryexpansion.model;
public class Document implements Comparable<Document>{
	private Integer docId;
	private Integer docSize;		
	
	public Document() {
		super();
		this.docId = 0;
		this.docSize = 0;
	}
	public Document(Integer docId, Integer docSize) {
		super();
		this.docId = docId;
		this.docSize = docSize;
	}
	public Integer getDocId() {
		return docId;
	}
	public void setDocId(Integer docId) {
		this.docId = docId;
	}
	public Integer getDocSize() {
		return docSize;
	}
	public void setDocSize(Integer docSize) {
		this.docSize = docSize;
	}
	@Override
	public String toString() {
		return "Document [docId=" + docId + ", docSize=" + docSize + "]";
	}
	
	@Override
	public int compareTo(Document o) {
		return Integer.compare(this.getDocId(), o.getDocId());
	}
}
