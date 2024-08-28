package com.ericsson.nsd.taf.test.operators;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.handlers.RemoteFileHandler;
import com.ericsson.cifwk.taf.tools.cli.handlers.impl.RemoteObjectHandler;
import com.ericsson.cifwk.taf.tools.cli.CLI;
import com.ericsson.cifwk.taf.tools.cli.CLICommandHelper;
import com.ericsson.cifwk.taf.tools.cli.Shell;
import com.ericsson.nsd.taf.test.constants.StaticConstants;
import com.ericsson.nsd.taf.test.util.HelperUtility;
import com.ericsson.oss.taf.hostconfigurator.HostGroup;
import com.ericsson.cifwk.taf.data.User;
import com.ericsson.cifwk.taf.tools.cli.CLICommandHelper;

@Operator(context = Context.CLI)
@Singleton
public class NeModificationOperatorCLI implements INeModificationOperator{
	
	private static final Logger log = Logger.getLogger(NeModificationOperatorCLI.class);
	
	private static final IViewOperator viewOperator = new ViewOperatorImpl();
	 private static final CLICommandHelper handler = new CLICommandHelper(HostGroup.getOssmaster(), HostGroup.getOssmaster().getNmsadmUser());
/*	 	public static final String SGSNListInCS="SgsnListInCS";
	    public static final String GGSNListInCS="GgsnListInCS";
	    public static final String CPGListInCS = "CPGListInCS";
		public static final String MSCListInCS = "MSCListInCS";
		public static final String EPGListInCS = "EPGListInCS";*/
		
	 private CLI cli;
	 private Shell shell;
	
	@Override
	public String checkAddition(String moName, String commandRef) {
		copyFiles();
		try {
			executeCommand(commandRef);
			TimeUnit.SECONDS.sleep(90);
			//Thread.sleep(30000);
			return checkViewEnabled(moName);
			
			}
		catch(Exception e)
		{
			 e.getMessage().toString();
			 return "FAIL";
		}
	}

	@Override
	public String checkDeletion(String moName, String commandRef) {
		copyFiles();
		try {
			executeCommand(commandRef);
			TimeUnit.SECONDS.sleep(60);
			//Thread.sleep(30000);
			return checkViewEnabled(moName);
			
			}
		catch(Exception e)
		{
			 e.getMessage().toString();
			 return "FAIL";
		}
	}
	
	public String executeCommand( String command) {
		log.info("Command : " + command);
		final String stdout = handler.simpleExec(command);
		final int exitCode = handler.getCommandExitValue();

		if (exitCode != 0) {
			log.error("Error comes while writing to the shell "+stdout);
		} else {
			
			log.info("Command result: " + stdout);
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
	
	/*private Shell initializeShell(Host host){
		User user=HelperUtility.getNmsadmUser();
		cli = new CLI(host, user);		
		if(shell == null){
			log.debug("Creating new shell instance");
			shell = cli.openShell();
		}
		return shell;
	}*/
	private static void sleepFor(final long sleepTime) { 
        try { 
            Thread.sleep(sleepTime); 
        } catch (final InterruptedException e) { 
            log.error("Exception occured while sleep",e); 
        } 
    }

    public String checkViewEnabled( String viewName){
    	System.out.println("NeModificationOperatorCLI : view name is $ "+viewName+" $");	

    	List<String> nodeNameList= new ArrayList<String>();
    	String testResult;
		try {
			testResult = viewOperator.invokeGroovyMethodOnArgs("NSDSGSNStatusviewCheck", "getMoList",viewName);
	    	if(!testResult.equals("[]")){

	    		final String[] nodeNames=testResult.substring(1, testResult.length()-1).split(",");
	    		nodeNameList=Arrays.asList(nodeNames);	
	    	}

		System.out.println("NeModificationOperatorCLI : From Groovy list is  :"+nodeNameList+ " and size is "+nodeNameList.size());
    	//try {
    		List<String> listOfFDN = viewOperator.getNodeNameListFromCS(viewName);
    		if(viewName.equals("PSPOOL")){
    			listOfFDN.remove("-1");
    		}
    		System.out.println("NeModificationOperatorCLI : From CS list is :"+listOfFDN+" and size is "+listOfFDN.size());
    		if(nodeNameList.isEmpty() && listOfFDN.isEmpty()){
    			return "OK";

    		}
    		else if (nodeNameList.size() > 0 && listOfFDN.size() > 0) {
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
    		e.printStackTrace();
    		return "FAIL";

    	}


    }
    
    /*List<String> getNodeNameListFromCS(String viewName){
		//String command="/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS lt MeContext -f neType==12";
		List<String> listOfFdnName=new ArrayList<String>();
		String command="";
		//List<String> listOfFdnName=new ArrayList<String>();
		if(viewName.equalsIgnoreCase("SGSN")){
		command=DataHandler.getAttribute(StaticConstants.SGSNListInCS).toString();
		}if(viewName.equalsIgnoreCase("GGSN")){
			command=DataHandler.getAttribute(StaticConstants.GGSNListInCS).toString();
		}if(viewName.equalsIgnoreCase("CPG")){
			command=DataHandler.getAttribute(StaticConstants.CPGListInCS).toString();
		}if(viewName.equalsIgnoreCase("EPG")){
			command=DataHandler.getAttribute(StaticConstants.EPGListInCS).toString();
		}if(viewName.equalsIgnoreCase("MSCS")){
			command=DataHandler.getAttribute(StaticConstants.MSCListInCS).toString();
		}
		
		String response= executeCommand(command);
		//System.out.println("NeModificationOperatorCLI : command is : $ "+command+"$");
		
		//System.out.println("NeModificationOperatorCLI : SGSNLIST response : $ "+response+"$");
		
		String[] lines= response.split("\n");
		for (int i = 0; i < lines.length; i++) {
			////System.out.println("$"+lines[i].trim()+"$");
			 if(lines[i].startsWith("SubNetwork"))
			 {
				 log.debug("NeModificationOperatorCLI : nodeName :$"+lines[i].trim()+"$");
				//listOfFdnName.add(lines[i].trim());
				listOfFdnName.add(lines[i].trim().substring(lines[i].trim().lastIndexOf("=")+ 1,lines[i].length()-1));
			 }
		}
		
		//System.out.println("TAF_NSD: FDN is : "+listOfFdnName);
		return listOfFdnName;
	}	*/
    private void copyFiles() {
        //Host host = DataHandler.getHostByName("ossmaster");
    	File[] allFiles = null;
    	Host host= HostGroup.getOssmaster();/*PsNsdOperator.getMasterhost();*/
        RemoteObjectHandler remote = new RemoteObjectHandler(host);

        String remoteFileLocation = "/home/nmsadm/";    //unix address

        //final List<String> rawGroovyFiles = FileFinder.findFile(".xml");

	
		String absPath = new File("src/main/resources/netsimXML/").getAbsolutePath();
		File file=new File(absPath);
		if(file!=null){
		allFiles = file.listFiles();
		}
		if(allFiles!=null && allFiles.length>0){
        for(int i=0; i<allFiles.length; i++){
        remote.copyLocalFileToRemote(allFiles[i].getAbsolutePath(),remoteFileLocation);
        }
		}
	}

}
