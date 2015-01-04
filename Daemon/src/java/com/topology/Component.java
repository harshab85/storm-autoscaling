package com.topology;


public class Component {
	
	private int executors;
	private double latency;
	private String id;
	private int tasks;
	public int getExecutors() {
		return executors;
	}
	public void setExecutors(int executors) {
		this.executors = executors;
	}
	public double getLatency() {
		return latency;
	}
	public void setLatency(double latency) {
		this.latency = latency;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getTasks() {
		return tasks;
	}
	public void setTasks(int tasks) {
		this.tasks = tasks;
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if(obj instanceof Component){
			Component component = (Component)obj;
			return (component.getId().equals(this.getId()));
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {		
		return 37 * this.getId().hashCode();
	}
}
