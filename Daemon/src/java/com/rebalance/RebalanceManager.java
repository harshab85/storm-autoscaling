package com.rebalance;

import java.util.Iterator;
import java.util.Map;

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
				System.out.println();
				System.out.println("Waiting for " + Util.REBALANCER_MANAGER_SLEEP_TIME_MSEC/1000 + " secs");
				wait(Util.REBALANCER_MANAGER_SLEEP_TIME_MSEC);
				
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
				}				
			}			
		}
		catch(Exception e){
			e.printStackTrace();
		}		
	}	
}
