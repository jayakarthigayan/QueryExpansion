package com.ir.queryexpansion.model;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonAutoDetect;

@JsonAutoDetect
public class QueryExpansionRequest {
	String queryText;		
	List<String> mactchingDocs;

	public QueryExpansionRequest() {
		super();
		this.queryText = "";
		this.mactchingDocs = new ArrayList<String>();
	}			

	public QueryExpansionRequest(String queryText, List<String> mactchingDocs) {
		super();
		this.queryText = queryText;
		this.mactchingDocs = mactchingDocs;
	}

	public String getQueryText() {
		return queryText;
	}

	public void setQueryText(String queryText) {
		this.queryText = queryText;
	}

	public List<String> getMactchingDocs() {
		return mactchingDocs;
	}

	public void setMactchingDocs(List<String> mactchingDocs) {
		this.mactchingDocs = mactchingDocs;
	}

	@Override
	public String toString() {
		return "QueryExpansionRequest [queryText=" + queryText + ", mactchingDocs=" + mactchingDocs + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((mactchingDocs == null) ? 0 : mactchingDocs.hashCode());
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
		if (mactchingDocs == null) {
			if (other.mactchingDocs != null)
				return false;
		} else if (!mactchingDocs.equals(other.mactchingDocs))
			return false;
		if (queryText == null) {
			if (other.queryText != null)
				return false;
		} else if (!queryText.equals(other.queryText))
			return false;
		return true;
	}
		
}
