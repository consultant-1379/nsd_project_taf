package com.ericsson.nsd.taf.test.constants;

public enum MirrorMIBsynchStatus {

	SYNCHRONIZED("3"),
	UNSYNCHRONIZED("4"),
	DISCOVERED("5");
	
	  

	
	public String mirrorMIBsynchStatus;
	
	MirrorMIBsynchStatus(String mirrorMIBsynchStatus){
		
		this.mirrorMIBsynchStatus=mirrorMIBsynchStatus;
	}

	

	public String getMirrorMIBsynchStatus() {
		return mirrorMIBsynchStatus;
	}
}
