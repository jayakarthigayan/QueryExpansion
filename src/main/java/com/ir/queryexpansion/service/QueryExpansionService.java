package com.ir.queryexpansion.service;

import java.util.HashMap;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ir.queryexpansion.model.QueryExpansionRequest;
import com.ir.queryexpansion.model.QueryExpansionResponse;
import com.ir.queryexpansion.util.QueryExpansionAlgos;

@Controller
@RequestMapping("/qe")
public class QueryExpansionService {
	
	@RequestMapping(value="/getexpquery",
					method=RequestMethod.POST,
					headers="Accept=application/json",
					produces={MediaType.APPLICATION_JSON_VALUE},
					consumes={MediaType.APPLICATION_JSON_VALUE})
	@ResponseBody
	public QueryExpansionResponse getExpandedQueries(@RequestBody QueryExpansionRequest queryExpReq)
	{
		QueryExpansionResponse response = new QueryExpansionResponse();
		System.out.println(queryExpReq.getQueryText());
		QueryExpansionAlgos algos = new QueryExpansionAlgos();
		final HashMap<String,String> expandedQueries 
					= algos.getAllExpansions(queryExpReq.getQueryText(), 
							queryExpReq.getMactchingDocs());
		System.out.println(expandedQueries);
		response.setAssociationQuery(expandedQueries.get("associationQuery"));
		response.setRoccioQuery(expandedQueries.get("roccioQuery"));
		response.setScalarQuery(expandedQueries.get("scalarQuery"));
		response.setMetricQuery(expandedQueries.get("metricQuery"));		
		return response;
	}
}
