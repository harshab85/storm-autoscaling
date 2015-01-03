package com.rebalance;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import com.util.Util;

public class Rebalancer {

	public static void rebalance(String topologyName, Map<String, Integer> components) throws IOException, InterruptedException{
		
		InputStream in = null;
		BufferedReader reader = null;
		
		try{									
			String rebalanceCommand = "cmd.exe /c cd %STORM_HOME%/bin && %PYTHON_HOME%/python storm rebalance demo -w 1 -e spout=2";
			System.out.println("Rebalance command : " + rebalanceCommand);
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
}
