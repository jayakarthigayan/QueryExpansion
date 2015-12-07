package com.ir.queryexpansion.model;
import java.util.TreeSet;

public class Term {

	private String term;
	private TreeSet<String> vocabularyTerms;
	private TreeSet<PostingEntry> postingEntries;		
	
	public Term() {
		super();
		this.term = "";
		this.postingEntries = new TreeSet<PostingEntry>();
		this.vocabularyTerms = new TreeSet<String>();
	}

	public Term(String term, TreeSet<PostingEntry> postingEntries, TreeSet<String> vocabularyTerms) {
		super();
		this.term = term;
		this.postingEntries = postingEntries;
		this.vocabularyTerms = vocabularyTerms;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}		

	public TreeSet<PostingEntry> getPostingEntries() {
		return postingEntries;
	}

	public void setPostingEntries(TreeSet<PostingEntry> postingEntries) {
		this.postingEntries = postingEntries;
	}

	public TreeSet<String> getVocabularyTerms() {
		return vocabularyTerms;
	}

	public void setVocabularyTerms(TreeSet<String> vocabularyTerms) {
		this.vocabularyTerms = vocabularyTerms;
	}

	@Override
	public String toString() {
		return "Term [term=" + term + ", vocabularyTerms=" + vocabularyTerms + ", postingEntries=" + postingEntries
				+ "]";
	}			
		
}
