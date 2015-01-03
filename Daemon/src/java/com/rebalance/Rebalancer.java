package com.rebalance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

public class Rebalancer {

	public static void rebalance(String topologyName, Map<String, Integer> components) throws IOException{
		
		InputStream in = null;
		BufferedReader reader = null;
		
		try{
			
			String rebalanceCommand = null;
			System.out.println("Rebalance command :" + rebalanceCommand);
			Process process = Runtime.getRuntime().exec(rebalanceCommand);
			in = process.getInputStream();
			
			reader = new BufferedReader(new InputStreamReader(in)); 
			String line = null;
			StringBuilder response = new StringBuilder();
			while((line = reader.readLine()) != null){
				response.append(line);
			}
			
			System.out.println("Response: " + response.toString());
			System.out.println();
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
	
}
