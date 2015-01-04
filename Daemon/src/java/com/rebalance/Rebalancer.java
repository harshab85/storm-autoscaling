package com.rebalance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.Map;

import com.util.Util;

public class Rebalancer {

	public static void rebalance(String topologyName, Map<String, Integer> components) throws IOException, InterruptedException{
		rebalance(topologyName, Util.REBALANCE_DEFAULT_WAIT_TIME, components);
	}
	
	public static void rebalance(String topologyName, int waitTime, Map<String, Integer> components) throws IOException, InterruptedException{
		
		StringBuilder componentsString = new StringBuilder();
		Iterator<String> iter = components.keySet().iterator();
		while(iter.hasNext()){			
			String component = iter.next();
			int executors = components.get(component);			
			componentsString.append(Util.REBALANCE_COMPONENT_COMMAND);
			componentsString.append(Util.SPACE);
			componentsString.append(component);
			componentsString.append(Util.EQUALS);
			componentsString.append(executors);
			componentsString.append(Util.SPACE);			
		}
				
		InputStream in = null;
		BufferedReader reader = null;
		
		try{							
			StringBuilder rebalanceCommand = new StringBuilder();
			rebalanceCommand.append(Util.REBALANCE_COMMAND);
			rebalanceCommand.append(Util.SPACE);
			rebalanceCommand.append(topologyName);
			rebalanceCommand.append(Util.SPACE);
			rebalanceCommand.append(Util.REBALANCE_WAIT_COMMAND);
			rebalanceCommand.append(Util.SPACE);
			rebalanceCommand.append(waitTime);
			rebalanceCommand.append(Util.SPACE);			
			rebalanceCommand.append(componentsString.toString().trim());
									
			System.out.println("Rebalance command : " + rebalanceCommand);
			Process process = Runtime.getRuntime().exec(rebalanceCommand.toString());
			in = process.getInputStream();
			
			reader = new BufferedReader(new InputStreamReader(in)); 
			String line = null;
			StringBuilder response = new StringBuilder();
			while((line = reader.readLine()) != null){
				response.append(line);
			}
			
			//System.out.println("Response: " + response.toString());
			System.out.println("Rebalancer: Sleeping for " + Util.REBALANCE_WAIT_TIME_MSEC/1000.0 + " secs");
			System.out.println();
			
			Thread.sleep(Util.REBALANCE_WAIT_TIME_MSEC);
		}
		finally{
			
			if(reader != null){
				reader.close();
			}
			
			if(in != null){
				in.close();
			}						
		}
	}
	
	/*public static void main(String[] args) throws IOException, InterruptedException {
		
		Map<String, Integer> components = new HashMap<String, Integer>(3);
		components.put("spout", 1);
		components.put("split", 1);
		components.put("count", 1);		
		rebalance("demo", components);		
	}*/
}
