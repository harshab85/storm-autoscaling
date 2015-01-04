package com.rest;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.exception.TopologyNotFoundException;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.topology.Topology;
import com.util.Util;

public class TopologySummary {

	private static Gson gson = new Gson();
	
	public static Topology getSummary(String topologyName) throws ClientProtocolException, IOException, TopologyNotFoundException, InterruptedException{		
		String results = RestAPI.getResults(Util.SERVER_HOST + Util.TOPOLOGY_SUMMARY_URL);
		JsonParser parser = new JsonParser();
		JsonElement jsonResults = parser.parse(results);
		
		JsonObject jsonTopology = filterResults(jsonResults, topologyName);	
		Topology topology = gson.fromJson(jsonTopology, Topology.class);
		return topology;
	}

	private static JsonObject filterResults(JsonElement jsonResults, String topologyName) throws TopologyNotFoundException {
		
		JsonObject resultsObject = jsonResults.getAsJsonObject();
		JsonArray resultsArray = (JsonArray)resultsObject.get("topologies");
		
		for(int i=0; i<resultsArray.size(); i++){			
			JsonObject result = (JsonObject)resultsArray.get(i);
			if(topologyName.equals(result.get("name").getAsString())){				
				return result;				
			}
		}
		
		throw new TopologyNotFoundException("The topology is not present in the summary : " + topologyName);
	}
	
	public static void main(String[] args) throws ClientProtocolException, IOException, TopologyNotFoundException, InterruptedException {
		
		getSummary("autoscaletest");
		
	}
	
}
