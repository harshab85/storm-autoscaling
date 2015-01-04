package com.rebalance;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import com.rebalance.Action.ActionEnum;
import com.rest.TopologyComponents;
import com.topology.Component;
import com.topology.Topology;
import com.util.Util;

public class RebalanceStrategy {

	private Topology topology;	
	private Map<String, Integer> components;
	private boolean shouldRebalance;	
	
	public RebalanceStrategy(Topology topology) throws InterruptedException, ClientProtocolException, IOException {
		this.topology = topology;
		this.components = new HashMap<String, Integer>();
		execute();
	}
	
	public boolean shouldRebalance(){
		return this.shouldRebalance;
	}
	
	public Map<String, Integer> getComponents(){
		return this.components;
	}
	
	public Topology getTopology(){
		return this.topology;
	}
	
	private void execute() throws InterruptedException, ClientProtocolException, IOException {
		
		Action action = this.topology.getLastAction();
		
		if(action == null){
			System.out.println("First component rebalance");
			firstComponentRebalance();
		}
		else if(action.getAction() == ActionEnum.Stop){
			System.out.println("No further scaling possible for topology");
		}
		else{
			rebalance(action);
		}
	}

	private void firstComponentRebalance() throws InterruptedException, ClientProtocolException, IOException {		
		Component nextComponent = this.topology.nextComponent();
		if(nextComponent == null){
			Action lastAction = new Action(nextComponent, ActionEnum.Stop);
			this.topology.setLastAction(lastAction);
			System.out.println("First component is not available");
		}
		/*else if(nextComponent.getExecutors() >= nextComponent.getTasks()){
			Action lastAction = new Action(nextComponent, ActionEnum.MaxRebalance);
			this.topology.setLastAction(lastAction);
			System.out.println("Component can't be rebalanced. Executors equals tasks.");
		}*/
		else{
			System.out.println("Get positive latency");
			System.out.println("Executors :" + nextComponent.getExecutors());
			nextComponent = getPositiveLatency(nextComponent);
			int executors = nextComponent.getExecutors() + 1;
			nextComponent.setExecutors(executors);
			Action lastAction = new Action(nextComponent, ActionEnum.Increase);
			
			this.topology.setLastAction(lastAction);
			this.shouldRebalance = true;
			this.components.put(nextComponent.getId(), executors);
		}		
	}

	private void rebalance(Action action) throws ClientProtocolException, IOException, InterruptedException{
				
		Component oldComponent = action.getComponent();
		int executors = oldComponent.getExecutors();
		System.out.println("Current component : " + oldComponent.getId());
		System.out.println("Executors : " + executors);
		
		double oldLatency = oldComponent.getLatency();
		Component newComponent = refreshComponentData(oldComponent); 
		double newLatency = newComponent.getLatency();				
		double diffPercentage = ((oldLatency-newLatency)/oldLatency) * 100.0;
				
		if(diffPercentage < 5){			
			newComponent = refreshComponentData(oldComponent); 
			newLatency = newComponent.getLatency();				
			diffPercentage = ((oldLatency-newLatency)/oldLatency) * 100.0;								
		}
		
		System.out.println("Old Latency : " + oldLatency);		
		System.out.println("New Latency : " + newLatency);
		System.out.println("Percentage diff : " + diffPercentage);
		
		if(diffPercentage >= 5){
			executors++;
			oldComponent.setExecutors(executors);
			oldComponent.setLatency(newLatency);
			Action lastAction = new Action(oldComponent, ActionEnum.Increase);
			
			this.topology.setLastAction(lastAction);			
			this.shouldRebalance = true;
			this.components.put(oldComponent.getId(), executors);			
		}
		else{
			executors--;			
			Component nextComponent = this.topology.nextComponent();
			if(nextComponent != null){
				nextComponent = refreshComponentData(nextComponent);
				int newExecutors = nextComponent.getExecutors() + 1;
				nextComponent.setExecutors(newExecutors);
				Action lastAction = new Action(nextComponent, ActionEnum.Increase);
				
				System.out.println("Cant rebalance current component");
				System.out.println("Next component: " + nextComponent.getId());
				System.out.println("Next component: old executors: " + (nextComponent.getExecutors()-1));
				System.out.println("Next component: new executors: " + nextComponent.getExecutors());
				
				this.topology.setLastAction(lastAction);			
				this.shouldRebalance = true;
				this.components.put(oldComponent.getId(), executors);
				this.components.put(nextComponent.getId(), newExecutors);
			}
			else{
				System.out.println("No more components to rebalance. Stop");
				Action lastAction = new Action(nextComponent, ActionEnum.Stop);
				
				this.topology.setLastAction(lastAction);
				this.shouldRebalance = true;
				this.components.put(oldComponent.getId(), executors);
			}
			
			
			
			
		}				
	}
	
	private Component getPositiveLatency(Component component) throws InterruptedException, ClientProtocolException, IOException {
		
		double latency = component.getLatency();
		while(latency <= 0){
			System.out.println("Positive Latency Wait: Sleeping for " + Util.POSITIVE_LATENCY_SAMPLES_SLEEP_TIME_MSEC/1000.0 + " secs");
			Thread.sleep(Util.POSITIVE_LATENCY_SAMPLES_SLEEP_TIME_MSEC);			
			component = refreshComponentData(component);
			latency = component.getLatency();
		}
		
		return component;
	}
	
	private Component refreshComponentData(Component component) throws ClientProtocolException, IOException, InterruptedException{
		List<Component> componentsList = TopologyComponents.getComponents(this.topology.getId());
		int index = componentsList.indexOf(component);
		return componentsList.get(index);
	}
	
	public static void main(String[] args) {
		
	}

}
