package com.ir.queryexpansion.model;

import org.codehaus.jackson.annotate.JsonAutoDetect;

@JsonAutoDetect
public class QueryExpansionResponse {
	String roccioQuery;
	String associationQuery;
	String metricQuery;
	String scalarQuery;			
	
	public QueryExpansionResponse() {
		super();
		this.roccioQuery = "";
		this.associationQuery = "";
		this.metricQuery = "";
		this.scalarQuery = "";
	}		
	
	public QueryExpansionResponse(String roccioQuery, String associationQuery, String metricQuery, String scalarQuery) {
		super();
		this.roccioQuery = roccioQuery;
		this.associationQuery = associationQuery;
		this.metricQuery = metricQuery;
		this.scalarQuery = scalarQuery;
	}

	public String getRoccioQuery() {
		return roccioQuery;
	}
	public void setRoccioQuery(String roccioQuery) {
		this.roccioQuery = roccioQuery;
	}
	public String getAssociationQuery() {
		return associationQuery;
	}
	public void setAssociationQuery(String associationQuery) {
		this.associationQuery = associationQuery;
	}
	public String getMetricQuery() {
		return metricQuery;
	}
	public void setMetricQuery(String metricQuery) {
		this.metricQuery = metricQuery;
	}
	public String getScalarQuery() {
		return scalarQuery;
	}
	public void setScalarQuery(String scalarQuery) {
		this.scalarQuery = scalarQuery;
	}

	@Override
	public String toString() {
		return "QueryExpansionResponse [roccioQuery=" + roccioQuery + ", associationQuery=" + associationQuery
				+ ", metricQuery=" + metricQuery + ", scalarQuery=" + scalarQuery + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((associationQuery == null) ? 0 : associationQuery.hashCode());
		result = prime * result + ((metricQuery == null) ? 0 : metricQuery.hashCode());
		result = prime * result + ((roccioQuery == null) ? 0 : roccioQuery.hashCode());
		result = prime * result + ((scalarQuery == null) ? 0 : scalarQuery.hashCode());
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
		QueryExpansionResponse other = (QueryExpansionResponse) obj;
		if (associationQuery == null) {
			if (other.associationQuery != null)
				return false;
		} else if (!associationQuery.equals(other.associationQuery))
			return false;
		if (metricQuery == null) {
			if (other.metricQuery != null)
				return false;
		} else if (!metricQuery.equals(other.metricQuery))
			return false;
		if (roccioQuery == null) {
			if (other.roccioQuery != null)
				return false;
		} else if (!roccioQuery.equals(other.roccioQuery))
			return false;
		if (scalarQuery == null) {
			if (other.scalarQuery != null)
				return false;
		} else if (!scalarQuery.equals(other.scalarQuery))
			return false;
		return true;
	}
		
}
