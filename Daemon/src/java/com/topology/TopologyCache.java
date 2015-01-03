package com.topology;

import java.util.HashMap;
import java.util.Map;

public final class TopologyCache {

	// key - topology name
	// value - Topology Summary object
	private static Map<String, Topology> topologyCache = new HashMap<String, Topology>();
	
	private static TopologyCache INSTANCE = new TopologyCache();
	
	private TopologyCache() {
	}
	
	public static TopologyCache getInstance(){
		return INSTANCE;
	}
	
	public void addTopology(String topologyName, Topology topology){
		topologyCache.put(topologyName, topology);
	}
	
	public Map<String, Topology> getTopologies(){
		return topologyCache;
	}
}

