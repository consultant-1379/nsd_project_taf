package com.ericsson.nsd.taf.test.constants;

public enum NodeType {
	
	SGSN_NEW(27,"ECIM_Top"),
	SGSN_OLD(12,"SGSN_NODE_MODEL"),
	EPG_SSR(17,"ECIM_Top"),
	EPG_JUNIPER(17,"EPG_NODE_MODEL"),
	SASN(35,"ECIM_Top"),
	DSC(21,"ECIM_Top"),
	MSC(8,"MSC_NODE_MODEL"),
	GGSN(10, "GGSN_NODE_MODEL"),
	MSCBC(9,"MSC_NODE_MODEL" ),
	CPG(11,"CPG_NODE_MODEL"),
	 DNS(13, "DNS_NODE_MODEL");
	  

	public int neType;
	public String neMIMName;
	
	NodeType(int neType, String neMIMName){
		this.neType=neType;
		this.neMIMName=neMIMName;
	}

	 public int getNeType() {
	        return neType;
	    }

	public String getNeMIMName() {
		return neMIMName;
	}
	 

}