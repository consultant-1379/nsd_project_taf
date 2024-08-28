package com.ericsson.nsd.taf.test.operators;

public interface NSDMCOperator {
	
	String executeCommandStatus(String McName, String command);
	
	String executeCommandoffline(String MCName, String commandRef);

	String executeCommandOnline(String MCName, String commandRef);
}


