package com.rest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.topology.Component;
import com.util.Util;

public class TopologyComponents {
	
	public static List<Component> getComponents(String topologyId) throws ClientProtocolException, IOException, InterruptedException{		
		
		String results = RestAPI.getResults(Util.SERVER_HOST + Util.TOPOLOGY_INFORMATION_URL + topologyId);
		JsonParser parser = new JsonParser();
		JsonElement jsonResults = parser.parse(results);
		
		JsonObject jsonObject = jsonResults.getAsJsonObject();
		JsonArray spoutsArray = (JsonArray)jsonObject.get("spouts");
		JsonArray boltsArray = (JsonArray)jsonObject.get("bolts");
		JsonArray topologyStats = (JsonArray)jsonObject.get("topologyStats");
		
		double latency = 0.0;
		if(topologyStats.size() > 0){
			JsonElement stats = topologyStats.get(1);
			JsonObject statsObject = stats.getAsJsonObject();
			String completeLatency = statsObject.get("completeLatency").getAsString();
			latency = Double.parseDouble(completeLatency);
		}
		
		List<Component> components = new ArrayList<Component>(spoutsArray.size() + boltsArray.size());
		
		for(int i=0; i<spoutsArray.size(); i++){			
			JsonObject spoutObject = (JsonObject)spoutsArray.get(i);
			Component component = new Component();
			
			String executors = spoutObject.get("executors").getAsString();
			component.setExecutors(Integer.parseInt(executors));
			
			String id = spoutObject.get("spoutId").getAsString();
			component.setId(id);
			
			//String latency = spoutObject.get("completeLatency").getAsString();
			component.setLatency(latency);
			
			String tasks = spoutObject.get("tasks").getAsString();
			component.setTasks(Integer.parseInt(tasks));
			
			components.add(component);
		}
		
		
		for(int i=0; i<boltsArray.size(); i++){			
			JsonObject boltObject = (JsonObject)boltsArray.get(i);
			Component component = new Component();
			
			String executors = boltObject.get("executors").getAsString();
			component.setExecutors(Integer.parseInt(executors));
			
			String id = boltObject.get("boltId").getAsString();
			component.setId(id);
			
			//String latency = boltObject.get("processLatency").getAsString();
			component.setLatency(latency);
			
			String tasks = boltObject.get("tasks").getAsString();
			component.setTasks(Integer.parseInt(tasks));
			
			components.add(component);
		}
		
		return components;
	}
	
	public static void main(String[] args) throws ClientProtocolException, IOException, InterruptedException {
		getComponents("autoscaletest-3-1420323429");
	}
}
