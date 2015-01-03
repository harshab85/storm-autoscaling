package com.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import com.util.Util;

public class RestAPI {

	private static HttpClient client = HttpClientBuilder.create().build();
	
	public static String getResults(String restURL) throws ClientProtocolException, IOException, InterruptedException{
		
		BufferedReader rd = null;
		try{			
			HttpGet request = new HttpGet(restURL);			
			
			System.out.println("Waiting for " + Util.REST_SLEEP_TIME_MSEC/1000.0 + " secs");
			Thread.sleep(Util.REST_SLEEP_TIME_MSEC);
			
			HttpResponse response = client.execute(request);
			
			rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			 
			StringBuilder result = new StringBuilder();
			String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
			}
			
			return result.toString();
		}
		finally{
			if(rd != null){
				rd.close();
			}
		}
	}
	
}
