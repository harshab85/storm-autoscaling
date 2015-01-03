package com.util;

public final class Util {

	public static final int REBALANCER_MANAGER_SLEEP_TIME_MSEC = 45000;
	
	public static final int REST_API_SLEEP_TIME_MSEC = 15000;
		
	public static final int POSITIVE_LATENCY_SAMPLES_SLEEP_TIME_MSEC = 10000;
	
	public static final int REBALANCE_WAIT_TIME_MSEC = 15000;
	
	public static final int SERVER_PORT = 9091;
	
	public static final String SERVER_HOST = "http://localhost:8080/";
	
	public static final String TOPOLOGY_SUMMARY_URL = "api/v1/topology/summary/";
	
	public static final String TOPOLOGY_INFORMATION_URL = "api/v1/topology/";
	
	public static final String REBALANCE_COMMAND = "cmd.exe /c cd %STORM_HOME%/bin && %PYTHON_HOME%/python storm rebalance";
	
	public static final String REBALANCE_WAIT_COMMAND = "-w";
		
	public static final String REBALANCE_COMPONENT_COMMAND = "-e";
	
	public static final String SPACE = " ";

	public static final int REBALANCE_DEFAULT_WAIT_TIME = 1;

	public static final String EQUALS = "=";
}
