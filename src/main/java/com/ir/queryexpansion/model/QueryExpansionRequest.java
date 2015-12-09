package com.ir.queryexpansion.model;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;

@JsonAutoDetect
public class QueryExpansionRequest {
	String queryText;		
	List<String> matchingDocs;

	public QueryExpansionRequest() {
		super();
		this.queryText = "";
		this.matchingDocs = new ArrayList<String>();
	}			

	public QueryExpansionRequest(String queryText, List<String> matchingDocs) {
		super();
		this.queryText = queryText;
		this.matchingDocs = matchingDocs;
	}

	public String getQueryText() {
		return queryText;
	}

	public void setQueryText(String queryText) {
		this.queryText = queryText;
	}

	public List<String> getMatchingDocs() {
		return matchingDocs;
	}

	public void setMatchingDocs(List<String> matchingDocs) {
		this.matchingDocs = matchingDocs;
	}

	@Override
	public String toString() {
		return "QueryExpansionRequest [queryText=" + queryText + ", matchingDocs=" + matchingDocs + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((matchingDocs == null) ? 0 : matchingDocs.hashCode());
		result = prime * result + ((queryText == null) ? 0 : queryText.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QueryExpansionRequest other = (QueryExpansionRequest) obj;
		if (matchingDocs == null) {
			if (other.matchingDocs != null)
				return false;
		} else if (!matchingDocs.equals(other.matchingDocs))
			return false;
		if (queryText == null) {
			if (other.queryText != null)
				return false;
		} else if (!queryText.equals(other.queryText))
			return false;
		return true;
	}
		
}
