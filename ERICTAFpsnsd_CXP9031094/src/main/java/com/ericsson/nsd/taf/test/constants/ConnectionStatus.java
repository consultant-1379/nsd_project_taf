package com.ericsson.nsd.taf.test.constants;

public enum ConnectionStatus {
	CONNECTED("2"),
	DISCONNECTED("3");
	
	  

	
	public String connectionStatus;
	
	ConnectionStatus(String connectionStatus){
		
		this.connectionStatus=connectionStatus;
	}

	

	public String getConnectionStatus() {
		return connectionStatus;
	}
	 

}