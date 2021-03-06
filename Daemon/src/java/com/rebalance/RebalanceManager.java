package com.rebalance;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import com.exception.TopologyNotFoundException;
import com.rest.TopologyComponents;
import com.rest.TopologySummary;
import com.topology.Component;
import com.topology.Topology;
import com.topology.TopologyCache;
import com.util.Util;

public class RebalanceManager implements Runnable {

	private Object lock = new Object();
	private static boolean startedThread = false;
	
	public static void start(){		
		if(!startedThread){
			RebalanceManager manager = new RebalanceManager();
			manager.run();
			startedThread = true;
			System.out.println("Started Rebalance Manager");
		}
	}

	@Override 
	public void run() {		
		try{			
			synchronized (lock) {		
				while(TopologyCache.hasTopologies()){				
					
					System.out.println();
					System.out.println("General Wait: Waiting for " + Util.REBALANCER_MANAGER_SLEEP_TIME_MSEC/1000 + " secs");
					lock.wait(Util.REBALANCER_MANAGER_SLEEP_TIME_MSEC);
					
					Map<String, Topology> topologies = TopologyCache.getInstance().getTopologies();
					Iterator<String> topologyIterator = topologies.keySet().iterator();
					while(topologyIterator.hasNext()){
						String topologyName = topologyIterator.next();
						Topology topology = topologies.get(topologyName);
						
						RebalanceStrategy strategy = new RebalanceStrategy(topology);
						if(strategy.shouldRebalance()){						
							Map<String, Integer> components = strategy.getComponents();
							Rebalancer.rebalance(topologyName, components);						
						}
						
						TopologyCache.getInstance().addTopology(topologyName, topology);
					}
				}
			}			
		}
		catch(Exception e){
			e.printStackTrace();
		}		
	}	
	
	public static void main(String[] args) throws ClientProtocolException, IOException, TopologyNotFoundException, InterruptedException {
		
		Topology topology = TopologySummary.getSummary("autoscaletest");
		String topologyId = topology.getId();
		List<Component> components = TopologyComponents.getComponents(topologyId);
		topology.setComponents(components);
		
		TopologyCache.getInstance().addTopology("autoscaletest", topology);
		
		RebalanceManager manger = new RebalanceManager();
		manger.run();
	}
}
