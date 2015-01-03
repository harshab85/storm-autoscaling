package com.topology;

import java.util.Iterator;
import java.util.List;

import com.rebalance.Action;

public class Topology {

	private String id;
	private String name;
	private List<Component> components;
	private ComponentsIterator iterator;
	private Action lastAction;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public List<Component> getComponents() {
		return components;
	}
	
	public void setComponents(List<Component> components) {
		this.components = components;		
		ComponentsIterator iterator = new ComponentsIterator(this.components);
		this.iterator = iterator;
	}
	
	public Action getLastAction() {
		return lastAction;
	}

	public void setLastAction(Action lastAction) {
		this.lastAction = lastAction;
	}

	public Component nextComponent(){
		if(this.iterator.hasNext()){
			return this.iterator.next();
		}
		else{
			return null;
		}
	}
	
	
	class ComponentsIterator implements Iterator<Component>{

		private List<Component> components;
		private int index = 0;
		
		public ComponentsIterator(List<Component> components){
			this.components = components;
		}
		
		@Override
		public boolean hasNext() {			
			return index < components.size();
		}

		@Override
		public Component next() {						
			Component component = components.get(index);
			index++;
			return component;
		}

		@Override
		public void remove() {
			if(index < components.size()){
				components.remove(index);
			}
		}		
	}			
}
