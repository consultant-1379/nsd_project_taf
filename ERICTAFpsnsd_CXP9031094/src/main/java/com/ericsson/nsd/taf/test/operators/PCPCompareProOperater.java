package com.ericsson.nsd.taf.test.operators;

import com.ericsson.cifwk.taf.handlers.implementation.SshRemoteCommandExecutor;
import com.ericsson.cifwk.taf.osgi.client.ApiClient;
import com.ericsson.nsd.taf.test.getters.*;

public class PCPCompareProOperater {/*
	private static SshRemoteCommandExecutor executor = NSDMCGetter.getSshMasterHostExecutor();
	 
	private final ApiClient client = NSDMCGetter.getOsgiClient();
                                     
	
	public String CompareProfile() {
		String testResult=null;
		
		String option = "cd";
		String command=" cd /var/opt/ericsson/nms_epc_healthcheck/reports/";
	  final String checkCommand =command  ;
	 
		
		//String response= executor.simplExec(checkCommand);
		try{
			
		String command2="/opt/ericsson/nms_pcp_cli/bin/cprofiles.sh findall";
		String option2="-n ";
		String option3="TEST1";
		//final String checkCommand = command2 + option2 + option3;	
		String response2= executor.simplExec(command2);
		String [] values = response2.split(" ");
		String check1= values[7];
		String [] values1=check1.split(",");
		String check2=values1[1];
		String pro1=values[17];
		String [] value2=pro1.split(",");
		String pro2=value2[2];
		
		System.out.println("outputsid"+check2);
		System.out.println("outputsid1"+pro2);
		if(check2 != null)
		{
			

		String command3="/opt/ericsson/nms_pcp_cli/bin/cprofiles.sh compare -n "+ check2 + ","+pro2+ " -f /home/nmsadm/ProfileCompare.profile -p plan_test";
		System.out.println("c"+command3);
		String response3= executor.simplExec(command3);
		testResult="ok";
		}
		else
		{
			System.out.println("no profile is present to compare");
		}
		return testResult;
		
		}
		catch(Exception e)
		{
			return e.getMessage().toString();
		}
		
		}
		
		
	*/}



