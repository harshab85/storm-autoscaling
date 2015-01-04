package com.rebalance;

import com.topology.Component;

public class Action {

	private Component component;
	private ActionEnum action;
	
	public Action(Component component, ActionEnum action) {
		this.component = component;
		this.action =action;
	}
	
	public Component getComponent() {
		return component;
	}

	public void setComponent(Component component) {
		this.component = component;
	}

	public ActionEnum getAction() {
		return action;
	}

	public void setAction(ActionEnum action) {
		this.action = action;
	}

	public enum ActionEnum{
		Increase, Decrease, Stop, MaxRebalance
	}
}
