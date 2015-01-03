package com.rebalance;

import java.util.Map;

import com.rebalance.Action.ActionEnum;
import com.topology.Topology;

public class RebalanceStrategy {

	private Topology topology;	
	private Map<String, Integer> components;
	private boolean shouldRebalance;	
	
	public RebalanceStrategy(Topology topology) {
		this.topology = topology;
		execute();
	}
	
	public boolean shouldRebalance(){
		return this.shouldRebalance;
	}
	
	public Map<String, Integer> getComponents(){
		return this.components;
	}
	
	private void execute() {
		
		Action action = this.topology.getLastAction();
		
		if(action == null){
			
		}
		else if(action.getAction() == ActionEnum.Stop){
			System.out.println("No further scaling possible for topology");
		}
		else{
			
		}
	}

}
