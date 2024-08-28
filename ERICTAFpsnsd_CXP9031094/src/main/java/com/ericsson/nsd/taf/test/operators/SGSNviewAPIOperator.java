package com.ericsson.nsd.taf.test.operators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Singleton;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.osgi.client.ApiClient;
import com.ericsson.cifwk.taf.tools.cli.CLI;
import com.ericsson.cifwk.taf.tools.cli.Shell;
import com.ericsson.nsd.taf.test.getters.NSDMCGetter;
import com.ericsson.nsd.taf.test.util.HelperUtility;
import com.ericsson.oss.taf.hostconfigurator.HostGroup;
import com.ericsson.cifwk.taf.data.User;
import com.ericsson.cifwk.taf.tools.cli.CLICommandHelper;

@Operator(context = Context.CLI)
@Singleton
public class SGSNviewAPIOperator /*implements ViewOperator*/{

	public static final String SGSNListInCS="SgsnListInCS";

	private final ApiClient client = NSDMCGetter.getOsgiClient();
	 private static final CLICommandHelper handler = new CLICommandHelper(HostGroup.getOssmaster(), HostGroup.getOssmaster().getNmsadmUser());
	private CLI cli;
	private Shell shell;

	Logger logger = Logger.getLogger(SGSNviewAPIOperator.class);
	


	public String checkViewEnabled( String viewName){
		if(viewName.equals("SGSN"))
		{
		String testResult=invokeGroovyMethodOnArgs("NSDSGSNStatusviewCheck", "getMoList","");
		final String[] nodeNames=testResult.substring(1, testResult.length()-1).split(",");
		List<String> nodeNameList=Arrays.asList(nodeNames);
		try {
			List<String> listOfFDN = getNodeNameListFromCS();

			if (nodeNameList.size() > 0 && listOfFDN.size() > 0) {
				if(nodeNameList.size() == listOfFDN.size()){
					if(!listOfFDN.removeAll(nodeNameList)){	

						return "FAIL";

					}
				}else{
					return "FAIL";
				}
				return "OK";
			}
			else if(nodeNameList.size() == listOfFDN.size()){
				return "OK";
			}
			return "FAIL";
			
		}
		catch(Exception e)
		{
			return e.getMessage().toString();
		}
			
		}else {
			return "FAIL";
		}
	}



	private List<String> getNodeNameListFromCS(){
		//String command="/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt MeContext -f neType==12";
		List<String> listOfFdnName=new ArrayList<String>();
		String command=DataHandler.getAttribute(SGSNviewAPIOperator.SGSNListInCS).toString();
		
		
		String response= executeCommand(command);
		logger.debug("SGSNLIST response : $"+response+"$");
		
		String[] lines= response.split("\n");
		for (int i = 0; i < lines.length; i++) {
			////System.out.println("$"+lines[i].trim()+"$");
			 if(lines[i].startsWith("SubNetwork"))
			 {
				 logger.debug("nodeName :$"+lines[i].trim()+"$");
				listOfFdnName.add(lines[i].trim());
			 }
		}
		
		//System.out.println("TAF_NSD: FDN is : "+listOfFdnName);
		return listOfFdnName;
	}	
	private String invokeGroovyMethodOnArgs(final String className, final String method, final String... args) {

		String respVal = null;
		respVal = client.invoke(className, method, args).getValue();
		//System.out.println("respVal"+respVal);
		//log.info(String.format("Invoking %1$s: %2$s", method, respVal));
		return respVal;
	}
	
	
	public String executeCommand( String command) {
		logger.info("Command : " + command);
		final String stdout = handler.simpleExec(command);
		final int exitCode = handler.getCommandExitValue();

		if (exitCode != 0) {
			logger.error("Error comes while writing to the shell "+stdout);
		} else {
			
			logger.info("Command result: " + stdout);
		}
		return stdout;
		/*Host host=HostGroup.getOssmaster();//DataHandler.getHostByName("ossmaster");

		initializeShell(host);
		//String command = getCommand(commandRef);
		shell.writeln(command);
		sleepFor(5000);
		String response = shell.read();
		return response;*/
	}
	private void sleepFor(final long sleepTime) { 
        try { 
            Thread.sleep(sleepTime); 
        } catch (final InterruptedException e) { 
            logger.error("Exception occured while sleep",e); 
        } 
    }

	public String getCommand(String commandRef) {
		String command = (String)DataHandler.getAttribute(commandRef);
		return command;
	}

	
	/*private Shell initializeShell(Host host){
		User user=HelperUtility.getNmsadmUser();
		cli = new CLI(host,user);
		if(shell == null){
			logger.debug("Creating new shell instance");
			shell = cli.openShell();
		}
		return shell;
	}
	*/
	

}


