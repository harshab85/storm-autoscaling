package com.daemon;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

import com.rebalance.RebalanceManager;
import com.rest.TopologyComponents;
import com.rest.TopologySummary;
import com.topology.Component;
import com.topology.Topology;
import com.topology.TopologyCache;
import com.util.Util;

public class AutoScaleDaemon {

	
	
	@SuppressWarnings({ "resource", "deprecation" })
	public static void main(String[] args) {		
		System.out.println("Started Daemon");
		
		while(true){						
			try{
				ServerSocket serverSocket = new ServerSocket(Util.SERVER_PORT);
				Socket socket = serverSocket.accept();
				
				InputStream in = socket.getInputStream();
				DataInputStream dis = new DataInputStream(in);
				
				String input = dis.readLine();				
				if(input != null && !input.isEmpty()){
					String topologyName = input.trim();
					System.out.println("Topology name from Storm : " + topologyName);
					
					System.out.println("Getting topology summary");
					Topology topology = TopologySummary.getSummary(topologyName);
					String topologyId = topology.getId();
					
					System.out.println("Getting topology components");
					List<Component> components = TopologyComponents.getComponents(topologyId);
					topology.setComponents(components);
					
					TopologyCache.getInstance().addTopology(topology.getName(), topology);					
					RebalanceManager.start();
				}
				
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}		
	}
}
