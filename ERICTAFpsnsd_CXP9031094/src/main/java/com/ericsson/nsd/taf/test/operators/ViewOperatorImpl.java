/**
 * 
 */
package com.ericsson.nsd.taf.test.operators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.validation.OverridesAttribute;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.data.User;
import com.ericsson.cifwk.taf.data.UserType;
import com.ericsson.cifwk.taf.osgi.client.ApiClient;
import com.ericsson.cifwk.taf.tools.cli.CLI;
import com.ericsson.cifwk.taf.tools.cli.Shell;
import com.ericsson.nsd.taf.test.constants.ConnectionStatus;
import com.ericsson.nsd.taf.test.constants.MirrorMIBsynchStatus;
import com.ericsson.nsd.taf.test.constants.NodeType;
import com.ericsson.nsd.taf.test.constants.StaticConstants;
import com.ericsson.nsd.taf.test.util.HelperUtility;
import com.ericsson.oss.taf.cshandler.CSDatabase;
import com.ericsson.oss.taf.cshandler.CSHandler;
import com.ericsson.oss.taf.cshandler.CSTestHandler;
import com.ericsson.oss.taf.cshandler.SimpleFilterBuilder;
import com.ericsson.oss.taf.cshandler.model.Fdn;
import com.ericsson.oss.taf.cshandler.model.Filter;
import com.ericsson.oss.taf.hostconfigurator.HostGroup;
import com.ericsson.cifwk.taf.tools.cli.CLICommandHelper;

/**
 * @author xchashr
 *
 */
@Operator(context = Context.CLI)
public class ViewOperatorImpl implements IViewOperator {
	
	private final CSHandler csHandler = new CSTestHandler(HostGroup.getOssmaster(), CSDatabase.Segment);
	
	 private static final CLICommandHelper handler = new CLICommandHelper(HostGroup.getOssmaster(), HostGroup.getOssmaster().getNmsadmUser());
	
    private static final Logger log = Logger.getLogger(StaticConstants.class);
 
    private static ApiClient apiClient;
    
    private CLI cli;
    
	private Shell shell;
	
	private static Map<String, String> mapOfAttributes= null;
	
	
	
	/*private static IViewOperator viewOperatorInstance;
	
	public IViewOperator getInstance() {
		if (viewOperatorInstance == null)
		{
			viewOperatorInstance = new ViewOperatorImpl();
		}
		return viewOperatorInstance;
		
	}*/

	public void setApiClient(ApiClient apiClient) {
		this.apiClient = apiClient;
		
	}
	
	public ApiClient getApiClient() {
		return apiClient;
	}
 
    public String checkViewData(String viewName){

    	String testresult=checkViewEnabled(viewName);
    	return testresult;
    }
    
    @SuppressWarnings("unused")
	public String checkViewEnabled( String viewName){
       	System.out.println("view name is $ "+viewName+" $");	
    	String groovyInput = null;
    	String csInput=null;
    	if(viewName.equals("SN_TRACKINGAREA")){
    		groovyInput="SN_TRACKINGAREA";
    		csInput="TrackingArea";
    	}else if(viewName.equals("PIU")){
    		groovyInput="SGSNBOARD";
			csInput = "PIU";
		} else if (viewName.equals("RA") || viewName.equals("Ra")) {
			groovyInput = "ROUTINGAREA";
			csInput = viewName;
		} else if (viewName.equals("PicSlot")) {
			groovyInput = "EPGBOARD";
			csInput = "PicSlot";
		} else {
			groovyInput =viewName;
    		csInput=viewName;
    	}
    	List<String> nodeNameList= new ArrayList<String>();
    	
    	/*try {
			Thread.sleep(400);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
    	try {
			String testResult=invokeGroovyMethodOnArgs("NSDSGSNStatusviewCheck", "getMoList",groovyInput);
			if(!testResult.equals("[]")){

				final String[] nodeNames=testResult.substring(1, testResult.length()-1).split(",");
				nodeNameList=Arrays.asList(nodeNames);	
			}

				System.out.println("ViewOperatorImpl : From Groovy list is  :"+nodeNameList+ " and size is "+nodeNameList.size());
			  		   		
				List<String> listOfNodes = getNodeNameListFromCS(csInput);
				
				if(viewName.equals("PSPOOL")){
					listOfNodes.remove("-1");
				}
				System.out.println("ViewOperatorImpl : From CS list is :"+listOfNodes+" and size is "+listOfNodes.size());
				if(nodeNameList.isEmpty() && listOfNodes.isEmpty()){
					return "OK";

				}
				else if (nodeNameList.size() > 0 && listOfNodes.size() > 0) {
					if(nodeNameList.size() == listOfNodes.size()){
						if(!listOfNodes.removeAll(nodeNameList)){	

							return "FAIL";

						}
					}else{
						return "FAIL";
					}
					return "OK";
				}
				else if(nodeNameList.size() == listOfNodes.size()){
					return "OK";
				}
				return "FAIL";
		} catch (Exception e) {
			return "FAIL";
		}

    	}

    @Override
    public String checkTopologyData(String viewName){
    	String testresult=checkTopologyEnabled(viewName);
    	return testresult;
    }
    
    
   	public String checkTopologyEnabled( String viewName){
   		String groovyInput = viewName;
   		String csInput = viewName;
       	List<String> nodeNameList= new ArrayList<String>();
       	try {
   			String testResult=invokeGroovyMethodOnArgs("TopologyviewCheck", "getMoList",groovyInput);
   			if(!testResult.isEmpty())
   			{
   				String[] fdnList = testResult.substring(1, testResult.length()).split(";");
   				nodeNameList=Arrays.asList(fdnList);
   			}
			log.info("ViewOperatorImpl : From Groovy list is  :"+nodeNameList+ " and size is "+nodeNameList.size());
			List<String> listOfNodes = getFDNListFromCS(csInput);
			if(viewName.equals("PSPOOL")){
				listOfNodes.remove("-1");
			}
			log.info("ViewOperatorImpl : From CS list is :"+listOfNodes+" and size is "+listOfNodes.size());
			if(nodeNameList.isEmpty() && listOfNodes.isEmpty()){
				return "OK";
			}
			else if (nodeNameList.size() > 0 && listOfNodes.size() > 0) {
				if(nodeNameList.size() == listOfNodes.size()){
					if(!listOfNodes.removeAll(nodeNameList)){	
						return "FAIL";
					}
				}else{
					return "FAIL";
				}
				return "OK";
			}
			else if(nodeNameList.size() == listOfNodes.size()){
				return "OK";
			}
			return "FAIL";
   		} catch (Exception e) {
   			log.info("Exception occured in ViewOperatorImpl.checkTopologyEnabled: ",e);
   			return "FAIL";
   		}
       	}

	@SuppressWarnings("unchecked")
	private List<String> getNodeNameListFromCSHanlderWithFilter(final int neType)
	{
		
		final Filter filter = SimpleFilterBuilder.builder().attr("neType").equalTo(neType).build();
		log.error("Output from Filter:$"+filter+"$");
		final List<Fdn> listOfNodes = csHandler.getByType("MeContext", new Filter(filter.toString()));
	//	final List<Object> listOfNodes= csHandler.getByType("MeContext", filter.toString());
		System.out.println("****************"+listOfNodes.size());
		List<String> nodeNameList = new ArrayList<String>();
		for(Fdn fdnObj : listOfNodes){
			//if(obj instanceof String){
			   //String fdns = (String)obj;
			   nodeNameList.add(getNodeName(fdnObj.getFdn()));
			//}
		}
		return nodeNameList;	
					
		
	}
	@SuppressWarnings("unchecked")
	private List<String> getNodeFdnListFromCSHanlderWithFilter(final int neType)
	{
		
		final Filter filter = SimpleFilterBuilder.builder().attr("neType").equalTo(neType).build();
		log.error("Output from Filter:$"+filter+"$");
		final List<Fdn> listOfNodes = csHandler.getByType("MeContext", new Filter(filter.toString()));
	//	final List<Object> listOfNodes= csHandler.getByType("MeContext", filter.toString());
		System.out.println("****************"+listOfNodes.size());
		List<String> nodeNameList = new ArrayList<String>();
		for(Fdn fdnObj : listOfNodes){
			//if(obj instanceof String){
			   //String fdns = (String)obj;
			   nodeNameList.add(fdnObj.getFdn());
			//}
		}
		return nodeNameList;	
					
		
	}
	@SuppressWarnings("unchecked")
	private List<String> getNodeNameListFromCSHanlderWithoutFilter(final String CSinput)
	{	

		
		//final Filter filter = SimpleFilterBuilder.builder().attr("neType").equalTo(neType).build();
		//log.error("Output from Filter:$"+filter+"$");
		final List<Fdn> listOfNodes= csHandler.getByType(CSinput);
		List<String> nodeNameList = new ArrayList<String>();
		for(Fdn fdnObj : listOfNodes){
			//if(obj instanceof String){
			  // String fdns = (String)obj;
			//System.out.println("arnav --> Nodes From Cshandler"+fdnObj.toString());
			   nodeNameList.add(getNodeName(fdnObj.toString()));
			//}
			
		}
		return nodeNameList;	
					

	}
	@SuppressWarnings("unchecked")
	private List<String> getNodeFdnListFromCSHanlderWithoutFilter(final String CSinput)
	{	

		
		//final Filter filter = SimpleFilterBuilder.builder().attr("neType").equalTo(neType).build();
		//log.error("Output from Filter:$"+filter+"$");
		final List<Fdn> listOfNodes= csHandler.getByType(CSinput);
		List<String> nodeNameList = new ArrayList<String>();
		for(Fdn fdnObj : listOfNodes){
			//if(obj instanceof String){
			  // String fdns = (String)obj;
			//System.out.println("arnav --> Nodes FDNlist From Cshandler"+fdnObj.toString());
			   nodeNameList.add(fdnObj.toString());
			//}
			
		}
		return nodeNameList;	
					

	}
	
	private String getNodeName(String nodeFdn){
		String nodeName = nodeFdn.substring(nodeFdn.trim().lastIndexOf("=")+1, nodeFdn.length());
		return nodeName;
	}

	public List<String> getNodeNameListFromCS(final String viewName){
		List<String> listOfNodes=null;
		try {
    		if(viewName.equals("SGSN"))
    		{
       		listOfNodes = getNodeNameListFromCSHanlderWithFilter(NodeType.SGSN_NEW.getNeType());
       		List<String> listOfSGSNNodes = getNodeNameListFromCSHanlderWithFilter(NodeType.SGSN_OLD.getNeType());
       		listOfNodes.addAll(listOfSGSNNodes);
			System.out.println("Nodes From Cshandler"+listOfNodes);
    		}
    		if(viewName.equals("GGSN"))
    		{
    		listOfNodes = getNodeNameListFromCSHanlderWithFilter(NodeType.GGSN.getNeType());
    		}
    		if(viewName.equals("MSC"))
    		{
    		listOfNodes = getNodeNameListFromCSHanlderWithFilter(NodeType.MSC.getNeType());
    		}
    		if(viewName.equals("MSCBC"))
    		{
    		listOfNodes = getNodeNameListFromCSHanlderWithFilter(NodeType.MSCBC.getNeType());
    		}
    		if(viewName.equals("CPG"))
    		{
    		listOfNodes = getNodeNameListFromCSHanlderWithFilter(NodeType.CPG.getNeType());
    		}
    		if(viewName.equals("EPG"))
    		{
    		listOfNodes = getNodeNameListFromCSHanlderWithFilter(NodeType.EPG_JUNIPER.getNeType());// can use EPG_SSR also as both have same neType = 17
    		//List<String> listOfEPGNodes = getNodeNameListFromCSHanlderWithFilter(NodeType.EPG_SSR.getNeType());
       		//listOfNodes.addAll(listOfEPGNodes);
    		}
    		if(viewName.equals("DNS"))
    		{
    		listOfNodes = getNodeNameListFromCSHanlderWithFilter(NodeType.DNS.getNeType());
    		}
    		if(viewName.equals("DSC"))
    		{
    		listOfNodes = getNodeNameListFromCSHanlderWithFilter(NodeType.DSC.getNeType());
    		}
    		if(viewName.equals("SASN"))
    		{
    		listOfNodes = getNodeNameListFromCSHanlderWithFilter(NodeType.SASN.getNeType());
    		}
    		if(viewName.equals("PSPOOL"))
    		{
    		listOfNodes = getNodeNameListFromCSHanlderWithoutFilter("PsPool");
    		}
    		if(viewName.equals("SN_TRACKINGAREA"))
    		{
    		listOfNodes = getNodeNameListFromCSHanlderWithoutFilter("TrackingArea");
    		}
    		if(viewName.equals("APN"))
    		{
    		listOfNodes	= new ArrayList<String>();	
    		List<String> apnList=null;
    		apnList = getNodeFdnListFromCSHanlderWithoutFilter("APN");
    		//System.out.println("arnav --> Nodes From Cshandler"+apnList);
    		List<String> parentListOfNodes=null;
    		parentListOfNodes = getNodeFdnListFromCSHanlderWithFilter(NodeType.GGSN.getNeType());
    		parentListOfNodes.addAll(getNodeFdnListFromCSHanlderWithFilter(NodeType.EPG_JUNIPER.getNeType()));
    		for (String apnMo : apnList){
    			//System.out.println("arnav --> Nodes From Cshandler"+apnMo);
    			String meContextfdn = getMeContextFDN(apnMo);
    			if (parentListOfNodes.contains(meContextfdn)){
    				listOfNodes.add(apnMo);
    			}
    		}
    		}
    		if(viewName.equals("BSC"))
    		{
    		listOfNodes = getNodeNameListFromCSHanlderWithoutFilter("Bsc");
    		}
    		if(viewName.equals("RNC"))
    		{
    		listOfNodes = getNodeNameListFromCSHanlderWithoutFilter("RNC");
    		listOfNodes.addAll(getNodeNameListFromCSHanlderWithoutFilter("Rnc"));
    		}
    		if(viewName.equals("PIU"))
    		{
    			listOfNodes = getNodeNameListFromCSHanlderWithoutFilter("PIU");
    			List<String> listOfPiuSGSNNodes = getNodeNameListFromCSHanlderWithoutFilter("Piu");
           		listOfNodes.addAll(listOfPiuSGSNNodes);
    		}
    		if(viewName.equals("PicSlot"))
    		{
    		listOfNodes = getNodeNameListFromCSHanlderWithoutFilter("PicSlot");
    		}
    		if(viewName.equals("MscMO"))
    		{
    		listOfNodes = getNodeNameListFromCSHanlderWithoutFilter("Msc");
    		}	
    		if(viewName.equals("MscPool"))
    		{
    		listOfNodes = getNodeNameListFromCSHanlderWithoutFilter("MscPool");
    		}	
    		if(viewName.equals("RA") || viewName.equals("Ra"))
            {
            listOfNodes = getNodeNameListFromCSHanlderWithoutFilter("RA");
            listOfNodes.addAll(getNodeNameListFromCSHanlderWithoutFilter("Ra"));
            }

		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return listOfNodes;
	}	
	
	private String getMeContextFDN(String apnMo) {
		// TODO Auto-generated method stub
		return apnMo.substring(0, apnMo.indexOf(",ManagedElement="));
	}

	public String invokeGroovyMethodOnArgs(final String className, final String method, final String... args) {

		String respVal = null;
		respVal = apiClient.invoke(className, method, args).getValue();
		System.out.println("respVal :"+respVal);
		//log.info(String.format("Invoking %1$s: %2$s", method, respVal));
		return respVal;
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
		/*log.info("ViewOperatorImpl.executeCommand --------->");
		log.info("ViewOperatorImpl.executeCommand--------->");
		String response= "";
		try{
		Host host=HelperUtility.getHost();//DataHandler.getHostByName("ossmaster");
		log.info("going to initialize shell--->");
		initializeShell(host);
		//String command = getCommand(commandRef);
		log.info("going to write command on shell--->");
		shell.writeln(command);
		sleepFor(10000);
		response = shell.read();
		log.info("Response on reading the shell is "+ response);
		log.info("ViewOperatorImpl.executeCommand <---------");
		
		}
		catch(Exception e)
		{
			log.error("Exception occured...", e);
		}
		return response;*/
		
	
		
	}
	
	
	public String executeCommandpcp( String command) {
		log.info("PCP Command : " + command);
		final String stdout = handler.simpleExec(command);
		final int exitCode = handler.getCommandExitValue();

		if (exitCode != 0) {
			log.error("Error comes while writing to the shell "+stdout);
		} else {
			
			log.info("PCP Command result: " + stdout);
		}
		return stdout;
		/*final Host host = HostGroup.getOssmaster();
		final User user = host.getUsers(UserType.OPER).get(0);
		initializeShellpcp(host,user);
		shell.writeln(command);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String response = shell.read();
		return response;*/
	}
	
	public String getCommand(String commandRef) {
		String command = (String)DataHandler.getAttribute(commandRef);
		return command;
	}

	
	/*private Shell initializeShell(Host host){
		log.info("ViewOperatorImpl.initializeShell--------->");
		try{
		User user=HelperUtility.getNmsadmUser();	
		cli = new CLI(host, user);	
		if(shell == null){
			log.info("Creating new shell instance---->");
			shell = cli.openShell();
			log.info("Created new shell instance<----");
			
		}
		log.info("ViewOperatorImpl.initializeShell <---------");
		
		}catch(Exception e)
		{
			log.error("Exception Occured-->", e);
		}
		return shell;
	}*/
	
	/*private Shell initializeShellpcp(Host host,User user){
		cli = new CLI(host,user);
		if(shell == null){
			log.debug("Creating new shell instance");
			shell = cli.openShell();
		}else 
		{
			shell.disconnect();
			shell = cli.openShell();
		}
		return shell;
	}*/
	@Override
	public String checkPoolAddition(final String PoolName) {
		String testResult="";

	try {
			String command1="/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS cm SubNetwork=ONRM_ROOT_MO_R,Cn=1,PsPool="+PoolName;
			executeCommand(command1);
			TimeUnit.SECONDS.sleep(10);
			testResult=invokeGroovyMethodOnArgs("NSDSGSNStatusviewCheck", "getMoList","PSPOOL");
			System.out.println("from groovy :"+testResult);
			final String[] nodeNames=testResult.substring(1, testResult.length()-1).split(",");
			for (int i = 0; i < nodeNames.length; i++) {
				if(nodeNames[i].trim().equalsIgnoreCase(PoolName)){
					return "OK";
				}
			}
			return "FAIL";
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "FAIL";
		}
		
		
	}

	@Override
	public String checkPoolDeletion(final String PoolName) {
		String testResult="";

		try {
			String command1="/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s Seg_masterservice_CS dm SubNetwork=ONRM_ROOT_MO_R,Cn=1,PsPool="+PoolName;
			executeCommand(command1);
			TimeUnit.SECONDS.sleep(10);
			testResult=invokeGroovyMethodOnArgs("NSDSGSNStatusviewCheck", "getMoList","PSPOOL");
			final String[] nodeNames=testResult.substring(1, testResult.length()-1).split(",");
			List<String> nodeNameList=Arrays.asList(nodeNames);
			if(!nodeNameList.contains(PoolName)){
				return "OK";
			}
			else
				return "FAIL";
		}
		catch(Exception e)
	{
			e.printStackTrace();return "FAIL";
		}
	
		
	}
	
	
	@Override
	public String executeCommandStatus(String McName, String command) {
		log.info("ViewOperatorImpl.executeCommandStatus--------->");
		final String checkMcCommand = command + " \"" + McName + "\"";
		log.info("--------------" + checkMcCommand);
		String result = executeCommand(checkMcCommand);//oss_ns_ps star
		String[] parseOutput=result.split("\n");
		log.info("ViewOperatorImpl.executeCommandStatus<---------");
			
		return parseOutput[0].split(" ")[2];
		
		
	}

	@Override
	public String executeCommandoffline(String MCName, String commandRef) {
		String checkMCs = null;
		final String checkMcCommand = commandRef + " \"" + MCName + "\" "
				+ "-reason=\"other\" -reasontext=\"test\"";
		executeCommand(checkMcCommand);
		log.info("Going to sleep for 2 minute so that MC come in started state");
		try {
			TimeUnit.SECONDS.sleep(300);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		final String command = "/opt/ericsson/nms_cif_sm/bin/smtool ";
		final String option = "-list ";
	
		final String checkMcCommand1 = command + option + "\"" + MCName + "\"";
		
	
			checkMCs =executeCommand(checkMcCommand1);
			
			String[] parseOutput=checkMCs.split("\n");
			return parseOutput[0].split(" ")[2];
			
			
		//return checkMCs;
	}

	@Override
	public String executeCommandOnline(String MCName, String commandRef) {
		String checkMCs = null;
		final String checkMcCommand = commandRef + " \"" + MCName + "\"";
		System.out.println("--------------" + checkMcCommand);

		executeCommand(checkMcCommand);
		try {
			TimeUnit.SECONDS.sleep(120);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		final String command = "/opt/ericsson/nms_cif_sm/bin/smtool ";
		final String option = "-list ";
		
		final String checkMcCommand1 = command + option + "\"" + MCName + "\"";
			checkMCs = executeCommand(checkMcCommand1);
			String[] parseOutput=checkMCs.split("\n");
			return parseOutput[0].split(" ")[2];
			
		/*return checkMCs;*/
	}


	@Override
	public String delProfile(final String profName) {
		creProfile("Dummy");
		try {
			Thread.sleep(40000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return invokeGroovyMethodOnArgs("NsdProfilePCP", "deleteProfileByName","Dummy");

		/*try {
                                    String command1="/opt/ericsson/nms_pcp_cli/bin/cprofiles.sh findall";
                                    String response2=executeCommand(command1);
                                    String [] values = response2.split(" ");
                                    String check1= values[7];
                                    String [] values1=check1.split(",");
                                    String check2=values1[1];
                                    System.out.println("outputsid"+check2);
                                    if(check2 != null)
                                    {
                                                    String command3="/opt/ericsson/nms_pcp_cli/bin/cprofiles.sh delete -n "+ check2;
                                                    executeCommand(command3);
                                                    return "OK";
                                    }else{
                                                    return "FAIL";
                                    }

                    }
                    catch(Exception e)
                    {
                                    e.printStackTrace();
                                    return "FAIL";
                    }

		 */

	}

	@Override
	public String compareProfile(final String prfName1,final String prfName2,final String PlanName) {

		try {
					String commandPCADel="/opt/ericsson/ddc/util/bin/pacli -delete "+ PlanName;
					String outputPlanDelete = executeCommandpcp(commandPCADel);	
					log.error("Output For command: $ " + commandPCADel + " $ is: " + outputPlanDelete);
					String commandPCA="/opt/ericsson/ddc/util/bin/pacli -create "+ PlanName + "=\"" + "This is a test planned area\"";
					String outputForPlanCreation = executeCommandpcp(commandPCA);
					log.error("Output for command: $ " + commandPCA + " $ is: " + outputForPlanCreation);
					String command="/opt/ericsson/nms_pcp_cli/bin/cprofiles.sh compare -n "+ prfName1 + ","+prfName2+ " -f /home/nmsadm/ProfileCompare.profile -p "+ PlanName;
					String output=executeCommandpcp(command);
					log.error("Output For command: $ " + command + " $ is: " + output);
					
				if(output.contains("Operation Completed"))
				{
						return "OK";
				}
				else
				{
					System.out.println("No profile is present to compare. Output received for the command: " + output);
					String outputProfCompare = "";
					String commandGetProfiles = "/opt/ericsson/nms_pcp_cli/bin/cprofiles.sh findall";
					String outputGetProfiles = executeCommandpcp(commandGetProfiles);
					
					log.error("Output For command: $ " + commandGetProfiles + " $ is: " + outputGetProfiles);
					if(outputGetProfiles.contains(prfName1) && outputGetProfiles.contains(prfName2))
					{
						log.error("Both the profiles are present on the server... Retrying again for the profile comparison...");
						String commandProfCompare="/opt/ericsson/nms_pcp_cli/bin/cprofiles.sh compare -n "+ prfName1 + ","+prfName2+ " -f /home/nmsadm/ProfileCompare.profile -p "+ PlanName;
						outputProfCompare = executeCommandpcp(commandProfCompare);
						log.error("Output for command: $ " + commandProfCompare + " $ is: " + outputProfCompare);
						
						if(outputProfCompare.contains("Operation Completed"))
						{
							return "OK";
						}else
						{
							return "FAIL";
						}
						
					}else
					{
						log.error("The profiles that needs to be compared do not exist on the server...");
						return "FAIL";
					}
				}	
				}catch(Exception e)
				{
					e.printStackTrace();
					return "FAIL";
				}
	}
	@Override
	public String profilePropertyUpdate(String prfName,String mimModel,String moc,String attrName,String attrValue)
	{
		return invokeGroovyMethodOnArgs("NsdProfilePCP", "updateProfileProperty",prfName,mimModel,moc,attrName,attrValue);
	}
	
	@Override
	public String profilePropertyDelete(String prfName)
	{
		return invokeGroovyMethodOnArgs("NsdProfilePCP", "deleteProfileProperty",prfName);
	}

	@Override
	public String exportProfile(final String profName) {

		try {
			String command3="/opt/ericsson/nms_pcp_cli/bin/cprofiles.sh export -n "+ profName + " -f /home/nmsadm/CI_PCP.profile";
			String out=executeCommandpcp(command3);
			System.out.println(out);
			if(out.contains(" successfully exported")){
				return "OK";
			}else{
				return "FAIL";
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "FAIL";
		}
	//return	invokeGroovyMethodOnArgs("NsdProfilePCP", "exportProfileByName",profName,"/home/nmsadm/exported.profile");
	//System.out.println("----->>  "+tmpResu);
		
		//newProfileName,exportFileName,OCP.getUserId()    
	}

	@Override
	public String importProfile(final String profName) {

		try {
			String command3="/opt/ericsson/nms_pcp_cli/bin/cprofiles.sh import -f /home/nmsadm/CI_PCP.profile -o "+profName;
			String out=executeCommandpcp(command3);
			if(out.contains("Import successful")){
				return "OK";
			}
			else
			{
				System.out.println("no profile is present to export");
				return "FAIL";
			}


		}
		catch(Exception e)
		{
			e.printStackTrace();
			return "FAIL";
		}

		
	//	return invokeGroovyMethodOnArgs("NsdProfilePCP", "importProfileByName",profName,"true");

	}

	@Override
	public String creProfile(final String profName) {
		//NsdProfilePCP

		return invokeGroovyMethodOnArgs("NsdProfilePCP", "createProfile",profName,"PCP TEST");


	}
	
	@Override
	public String verProfile(final String profName) {
		//NsdProfilePCP

		return invokeGroovyMethodOnArgs("NsdProfilePCP", "verifyProfile",profName,profName);


	}

	@Override
	public String updateProfile(String originalName) {

		return   HelperUtility.invokeGroovyMethodOnArgs(getApiClient(), "NsdProfilePCP", "updateProfile",originalName);
	}


	@Override
	public String synchronizeNode(String moType) {
		log.error("ViewOperatorImpl.synchronizeNode -------> " );
		String actualOutput = StaticConstants.FAILED;
		
		final String synchNotificationStringWithNotification = StaticConstants.MIRRO_MIB_SYNCH_STATUS_ATTRIBUTE +
				StaticConstants.COLON + StaticConstants.SPACE + StaticConstants.SYNCH_VALUE_WITH_NOTIFICATION;
		log.error("ViewOperatorImpl.synchronizeNode: synchNotificationStringWithNotification" + synchNotificationStringWithNotification);
		
		final String synchNotificationStringWithoutNotification = StaticConstants.MIRRO_MIB_SYNCH_STATUS_ATTRIBUTE +
				StaticConstants.COLON + StaticConstants.SPACE + StaticConstants.SYNCH_VALUE_WITHOUT_NITIFICATION;
		log.error("ViewOperatorImpl.synchronizeNode: synchNotificationStringWithoutNotification" + synchNotificationStringWithoutNotification);
		
		log.error("ViewOperatorImpl.synchronizeNode: Calling Groovy");
		final String moFdn = HelperUtility.invokeGroovyMethodOnArgs(getApiClient(), "NsdMoTopologyGroovy", "getMoFdnForNodeType", moType);
		log.error("ViewOperatorImpl.synchronizeNode: NsdMoTopologyGroovy returned the response: " + moFdn);
		
		if(moFdn != null && !moFdn.trim().isEmpty())
		{
			final String responseFromGroovy = HelperUtility.invokeGroovyMethodOnArgs(getApiClient(), "SynchronizeNodeVerificationGroovy", "verifySynchFunctionality", moFdn);
			log.error("ViewOperatorImpl.synchronizeNode: responseFromSynchronizeNodeVerificationGroovy: " + responseFromGroovy);
			if(responseFromGroovy.contains(synchNotificationStringWithoutNotification) || 
							responseFromGroovy.contains(synchNotificationStringWithNotification))
			{
				actualOutput = StaticConstants.PASSED;
			}
		}else
		{
			log.error("ViewOperatorImpl.synchronizeNode: Skipping the TC as no node is synched on the server!!!");
		}
		
		log.error("ViewOperatorImpl.synchronizeNode <------- " );
		return actualOutput;
	}
	
	 @SuppressWarnings("unused")
	public String checkPropertiesViewData(final String nodeType, final String propertiesToFetch) {
		int numOfPropertiesMismatch = 0;
		
		Set<String> propertyMismatchSet = new HashSet<String>();
		String result = "FAILED";
		String moFdn = null;
		String attributeValuefromGroovy = null;
		String attributeValuefromCS = null;
		log.info("ViewOperatorImpl. checkPropertiesViewData ---->");

		try {
			mapOfAttributes = HelperUtility.createDomainMowithAttributes(
					nodeType, propertiesToFetch);
			if (mapOfAttributes != null && mapOfAttributes.size() != 0) {
				moFdn = mapOfAttributes.get("moFdn");				
			Fdn	fdnObject = new Fdn(moFdn);
			String[] propertiesList = propertiesToFetch.split(":");
			for(String property : propertiesList){
				if(property.equals("sourceType")){
					String managedElementFdn = HelperUtility.getManagedElementFdn(moFdn);
					log.info("Managed Element Fdn --->"+managedElementFdn);
					attributeValuefromCS = HelperUtility.getAttributeValueFromFDN(new Fdn(managedElementFdn), property);
				}else{
					attributeValuefromCS = HelperUtility.getAttributeValueFromFDN(fdnObject, property);
				}
								
				attributeValuefromGroovy = mapOfAttributes.get(property);
				
				if(property.equalsIgnoreCase("connectionStatus")){
					if(attributeValuefromGroovy.equals("2") || attributeValuefromGroovy.equals("CONNECTED")){
						attributeValuefromGroovy = ConnectionStatus.CONNECTED.getConnectionStatus();
					}
					if(attributeValuefromGroovy.equals("3") || attributeValuefromGroovy.equals("DISCONNECTED")){
						attributeValuefromGroovy = ConnectionStatus.DISCONNECTED.getConnectionStatus();
					}
					
				}
				if(property.equalsIgnoreCase("mirrorMIBsynchStatus")){
					if(attributeValuefromGroovy.equals("3") || attributeValuefromGroovy.equals("SYNCHRONIZED")){
						attributeValuefromGroovy = MirrorMIBsynchStatus.SYNCHRONIZED.getMirrorMIBsynchStatus();
					}
					if(attributeValuefromGroovy.equals("4") || attributeValuefromGroovy.equals("UNSYNCHRONIZED")){
						attributeValuefromGroovy = MirrorMIBsynchStatus.UNSYNCHRONIZED.getMirrorMIBsynchStatus();
					}
					if(attributeValuefromGroovy.equals("5") || attributeValuefromGroovy.equals("DISCOVERED")){
						attributeValuefromGroovy = MirrorMIBsynchStatus.DISCOVERED.getMirrorMIBsynchStatus();
					}
					
				}
				log.info("Property : "+property+" value from Seg cs : "+attributeValuefromCS);
				log.info("Property : "+property+" value from Groovy : "+attributeValuefromGroovy);
				if(!attributeValuefromGroovy.equalsIgnoreCase(attributeValuefromCS))
				{
					numOfPropertiesMismatch++;
					propertyMismatchSet.add(property);
					
				}
			}	
			
			if(numOfPropertiesMismatch >0)
			{
				log.info("List of properties mismatched "+ propertyMismatchSet);
			}
			else 
			{
				result="OK";
			}

			}
			
			
		} catch (Exception e) {
			log.error(
					"ViewOperatorImpl. checkPropertiesViewData.. Exception occuered--> ",e);
		}
		return result;
	}
	 
	 public List<String> getFDNListFromCS(final String viewName){
		List<String> listOfNodes=null;
		try {
    		if(viewName.equals("SGSN"))
    		{
		   		listOfNodes = getFDNListFromCSHanlderWithFilter(NodeType.SGSN_NEW.getNeType());
		   		List<String> listOfSGSNNodes = getFDNListFromCSHanlderWithFilter(NodeType.SGSN_OLD.getNeType());
		   		listOfNodes.addAll(listOfSGSNNodes);
    		}
    		if(viewName.equals("GGSN"))
    		{
    			listOfNodes = getFDNListFromCSHanlderWithFilter(NodeType.GGSN.getNeType());
    		}
    		if(viewName.equals("MSC"))
    		{
    			listOfNodes = getFDNListFromCSHanlderWithFilter(NodeType.MSC.getNeType());
    			List<String> listOfMSCBCNodes = getFDNListFromCSHanlderWithFilter(NodeType.MSCBC.getNeType());
		   		listOfNodes.addAll(listOfMSCBCNodes);
    		}
    		if(viewName.equals("CPG"))
    		{
    			listOfNodes = getFDNListFromCSHanlderWithFilter(NodeType.CPG.getNeType());
    		}
    		if(viewName.equals("EPG"))
    		{
    			listOfNodes = getFDNListFromCSHanlderWithFilter(NodeType.EPG_JUNIPER.getNeType());// can use EPG_SSR also as both have same neType = 17
    		}
    		if(viewName.equals("SASN"))
    		{
    			listOfNodes = getFDNListFromCSHanlderWithFilter(NodeType.SASN.getNeType());
    		}
    		if(viewName.equals("PsPoolArea"))
    		{
    			listOfNodes = getFDNListFromCSHanlderWithoutFilter("PsPoolArea");
    		}
    		if(viewName.equals("PLMN"))
    		{
    			listOfNodes = getFDNListFromCSHanlderWithoutFilter("PLMN");
    		}
		}catch(Exception e)
		{
			log.error("Exception Occured in ViewOperatorImpl.getFDNListFromCS",e);
		}
		return listOfNodes;
	}
 	@SuppressWarnings("unchecked")
	private List<String> getFDNListFromCSHanlderWithFilter(final int neType)
	{
		final Filter filter = SimpleFilterBuilder.builder().attr("neType").equalTo(neType).build();
		final List<Fdn> listOfNodes = csHandler.getByType("MeContext", new Filter(filter.toString()));
		List<String> nodeNameList = new ArrayList<String>();
		for(Fdn fdnObj : listOfNodes){
			   nodeNameList.add(fdnObj.getFdn());
		}
		return nodeNameList;	
	}
	@SuppressWarnings("unchecked")
	private List<String> getFDNListFromCSHanlderWithoutFilter(final String CSinput)
	{	
		final List<Fdn> listOfNodes= csHandler.getByType(CSinput);
		List<String> nodeNameList = new ArrayList<String>();
		for(Fdn fdnObj : listOfNodes){
			   nodeNameList.add(fdnObj.toString());
		}
		return nodeNameList;	
	}
	
	private static void sleepFor(final long sleepTime) { 
        try { 
            Thread.sleep(sleepTime); 
        } catch (final InterruptedException e) { 
            log.error("Exception occured while sleep",e); 
        } 
    }

}

