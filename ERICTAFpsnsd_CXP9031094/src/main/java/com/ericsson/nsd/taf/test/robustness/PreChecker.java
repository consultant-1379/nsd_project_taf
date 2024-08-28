
/**
 * 
 */
package com.ericsson.nsd.taf.test.robustness;

import java.util.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.ericsson.oss.taf.cshandler.CSDatabase;
import com.ericsson.oss.taf.cshandler.model.Fdn;
import com.ericsson.cifwk.taf.assertions.TafAsserts;
import com.ericsson.nsd.taf.test.constants.NodeType;
import com.ericsson.nsd.taf.test.constants.StaticConstants;
import com.ericsson.nsd.taf.test.getters.NSDMCGetter;
import com.ericsson.nsd.taf.test.util.HelperUtility;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.handlers.netsim.CommandOutput;
import com.ericsson.cifwk.taf.handlers.netsim.NetSimCommandHandler;
import com.ericsson.cifwk.taf.handlers.netsim.NetSimContext;
import com.ericsson.cifwk.taf.handlers.netsim.NetSimResult;
import com.ericsson.cifwk.taf.handlers.netsim.commands.NetSimCommands;






//new add
import org.apache.log4j.Logger;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;

import com.ericsson.nsd.taf.test.robustness.ForNodeStart;

import com.ericsson.cifwk.taf.annotations.DataDriven;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.Output;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.oss.taf.hostconfigurator.HostGroup;
import com.esotericsoftware.minlog.Log;
import com.ericsson.cifwk.taf.tools.cli.CLICommandHelper;
import com.ericsson.cifwk.taf.data.DataHandler;

import static org.junit.Assert.*;

/**
 * @author xchashr
 *
 */
@Test
public class PreChecker {
	
	private static final Logger log = Logger.getLogger(PreChecker.class);
	
	

    private static final String TRIGGER_PATH = "/netsim/inst/POC/";
	private static final String FileName = "simulationTextFile.txt";
	private String networkElement = null;
	private ForNodeStart csHandler = null;
	
	
	@BeforeSuite
	public void restartNetsim(){
		Host netsimHost=NSDMCGetter.getNetSimHost();
		csHandler = null;
        csHandler = new ForNodeStart(netsimHost, CSDatabase.Segment, true);
        String restartCommand = "/netsim/inst/restart_netsim" ;
        log.info("Executing Netsim restart command --> " + restartCommand);
        String cmdResult = csHandler.restartNetsim(restartCommand);
        log.info("Netsim restart command fired. cmdResult --> " + cmdResult);
        assertTrue(true);
    }
	
	@BeforeSuite
	public void simulationTestStart(){

		final String startScript = "triggerStart.sh";
		networkElement = "WCG;DSC;CSCF;SGSN;EPG;UPG;ESAPC;ESASN;IPWORKS;SBG;SpitFire;MSC-S-APG43L R15A;MSC-S-APG R15A;MSC-S-APG43L R17A;BSP 15A-CORE-V5;MSC-S-APG43L R15A;MSC-S-APG43L R16A;MSC-S-APG43L R14B;MSC-S-APG R14B;MSC R18A-APG43L;MSC-S-APG43L R18A;MSC R17A-APG43L;ECM R17;MSC-S-APG43L R18B;MSC-S-CP-APG43L R18B;MSC R18B-APG43L";
		assertTrue(simulationOperator(networkElement, startScript));
	}
	
    public boolean simulationOperator(String networkElement, String script){

    	Host Netsimhost=NSDMCGetter.getNetSimHost();  
    	//netsimHost= HostGroup.getAllNetsims().get(0);
		String neList[] = networkElement.split(";");
		String initialCommand = "rm -rf " + TRIGGER_PATH + FileName + " | touch " + TRIGGER_PATH + FileName;
		log.info("Creating file on the path -> " +  TRIGGER_PATH);
		csHandler = null;
        csHandler = new ForNodeStart(Netsimhost, CSDatabase.Segment, true);
        csHandler.executetest(initialCommand);

		for(String ne : neList ) {
			String command1 = "echo " + ne + " >> " + TRIGGER_PATH + FileName;
			//logger.info("Command1 to execute -> "+command1);
			csHandler.executetest(command1);
		}
		
		log.info("Waiting for the script to execute....");
		String command = ". " + TRIGGER_PATH + script + " " + TRIGGER_PATH + FileName;
		log.info("Command to execute to start the nodes from simulationOperator-> "+command);
		String result =  csHandler.executetest(command);
		
		log.info("Result from the script = " + result);
		
		if(result.contains("0")){
			
			return true;
		}
		return false;
	}
	
    
    public void mcRestart() {
    	try{
    		int mcofflineCount = 0;
    	
    		ArrayList<String> offlineMcList = new ArrayList<String>();
    		ArrayList<String> mcNameList = new ArrayList<String>();
    		ArrayList<String> mcStatusList = new ArrayList<String>();
    			final String commandToCheckNmaMc = "/opt/ericsson/bin/smtool -list | grep nma";
    			String mcListResponse = HelperUtility
    					.execute(commandToCheckNmaMc);
    			String splittedmcListResponse[] = mcListResponse.split("[\\n]");
    			
    			for (String s : splittedmcListResponse) {
    				
    				if (!s.isEmpty()) {
    					Pattern p = Pattern.compile("(^nma\\d*)(.*)");
    					Matcher m = p.matcher(s.trim());				
    				if (m.matches()) {					
    					log.info("NMA MC -->" + m.group(1));
    					mcNameList.add(m.group(1));
    					mcStatusList.add(m.group(2));
    				}
    			}
    		}
    			log.info("McList Size --->"+mcNameList.size());
    		for (int i = 0; i < mcNameList.size(); i++) {
    			String mcName = mcNameList.get(i);
    			String status = mcStatusList.get(i);
    			if(status.contains("started")){
    				log.info(" Going to restart MC --->" + mcNameList.get(i));
    				String mcRestartCommand = "/opt/ericsson/bin/smtool -coldrestart "
    						+ mcName + " -reason=\"other\" -reasontext=\"test\"";
    				log.info("mcRestartCommand -->"+ mcRestartCommand);
    				String restartResponse = HelperUtility
    						.execute(mcRestartCommand);
    				log.info("Restart response --->"+restartResponse);
    			}else if(status.contains("offline")){
    				log.info(" Going to online MC --->" + mcNameList.get(i));
    				String mcOnlineCommand = "/opt/ericsson/bin/smtool -online "
    						+ mcName;
    				log.info("mcOnlineCommand -->"+ mcOnlineCommand);
    				String onlineResponse = HelperUtility
    						.execute(mcOnlineCommand);
    				log.info("Restart response --->"+onlineResponse);
    			}
    			log.info(" Going to sleep for 3 min to get NMA MC's started properly --->");
    			TimeUnit.MINUTES.sleep(3);
    			log.info(" Come back from sleep <---");
    			String mcStatusCheckCommand = "/opt/ericsson/bin/smtool -list "
    					+ mcName;
    			log.info("Going to checkMC status--->" +mcStatusCheckCommand);
    			String statusResponse = HelperUtility
    					.execute(mcStatusCheckCommand);
    			log.info("Status Response--->" +statusResponse);
    			if(statusResponse.contains("started")){
    				log.info("MC started properly ---->"+mcName);
    			}else{
    				log.error("MC Not started properly ---->"+mcName);
    				mcofflineCount++;
    			}
    		}		
    		if(mcofflineCount == 0){
    			log.info("MC restarted so Going to sleep for 10 minutes as all nodes need to sync");
    			TimeUnit.MINUTES.sleep(10);
    			//validateMcStatus();
    		}else{
    			log.info("All MC not starte -->");
    			log.error("MC name not started properly ---->");
    			for(int i=0; i<offlineMcList.size(); i++){
    				log.error(offlineMcList.get(i));
    				
    			}
    		}
    	}catch(Exception e){
    		log.error("PreCheck.mcRestart...Exception occured--->" , e);
    	}
    	}
    
	@BeforeSuite(groups = "init")
	private void validatePreChecks(){
		//Pre-Check 1: Validate package is installed or not?
		boolean isStatusOk = false;
		/*try {*/
			isStatusOk = validatePreCheck1();
			TafAsserts.assertTrue(isStatusOk);
	}
	
	/**
	 * The below pre-check will check if the package is installed on the server or not.
	 * @throws PreCheckFailedException 
	 */
	@Test(groups = "init")
	private boolean validatePreCheck1() {
		log.info("PreCheck1: Check Package is installed or not ---------->");
		boolean isStatusOk = false;
		final String rStateOfNsdPkg = HelperUtility.getInstalledNsdPackageVersion();
		if( rStateOfNsdPkg != null && !rStateOfNsdPkg.trim().isEmpty()){
			log.info("PreCheck1: Status: OK");
			log.info("PreCheck1: Check Package is installed or not <----------");
			isStatusOk = validatePreCheck2();
			
		}else
		{
			log.info("PreCheck1: Status: FAILED");
			log.error("PreCheck1 gets FAILED !!! ERICcpsnsd package is not installed on the server !!! Skipping the other pre-checks !!!");
		}
		return isStatusOk;
	}
	
	/**
	 * The below pre-check will check the status of the product MC and also its dependent MC's such as
	 * Seg_masterservice_CS
	 * ONRM_CS
	 * Region_CS
	 * oss_nsd_ps
	 * @return
	 * @throws PreCheckFailedException 
	 */
	private boolean validatePreCheck2() /*throws PreCheckFailedException */{
		log.info("PreCheck2: Check Product and Its Dependent MC's status ---------->");
		boolean isStatusOk = false;
		String mcName = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferReader = null;
		log.info("PreCheck2: Check Product and Its Dependent MC's status ---------->");
		try {
			inputStreamReader = new InputStreamReader(PreChecker.class.getClassLoader().
					getResourceAsStream(StaticConstants.MISCELLENOUS_DIRECTORY_PATH + StaticConstants.MC_NAMES_TO_CHECK_FILE));
			bufferReader = new BufferedReader(inputStreamReader);
			while ((mcName = bufferReader.readLine()) != null) {
				final String mcStatus = HelperUtility.executeCommand(StaticConstants.SMTOOL_LIST_COMMAND + StaticConstants.SPACE + mcName);
				String mcStatusCheck = null;
				if(!mcStatus.contains(StaticConstants.STARTED))
				{
					try{
						
						for(int i = 0; i<3;i++){
							log.error(" one of the dependent MC " + mcName + " was not online/started... trying to start MC attempt "+ i+1); 
							HelperUtility.executeCommand(StaticConstants.SMTOOL_ONLINE_COMMAND + StaticConstants.SPACE + mcName);
							TimeUnit.SECONDS.sleep(240);
							mcStatusCheck = HelperUtility.executeCommand(StaticConstants.SMTOOL_LIST_COMMAND + StaticConstants.SPACE + mcName);
							if(!mcStatusCheck.contains(StaticConstants.STARTED)){
								log.info("MC is still not up trying again ");
							}else{
								break;
							}
						}if(mcStatusCheck.contains(StaticConstants.STARTED)){
						log.info("PreCheck3: mc: " + mcName + " Status: OK");
					}
					else{
					log.info("PreCheck2: Status: FAILED");
					log.error("PreCheck2 gets FAILED !!! As one of the dependent MC " + mcName + " was not online/started... So, Skipping the other checks !!!");
					return false;		
					}}catch(InterruptedException e){
						e.printStackTrace();
						}}else
						{
					log.info("PreCheck3: mc: " + mcName + " Status: OK");
				}
			}
			log.info("PreCheck2: Status: OK");
			log.info("PreCheck2: Check Product and Its Dependent MC's status <----------");
		 isStatusOk = validateFreeMemory();
		} catch (FileNotFoundException e) {
			log.error("PreChecker.validatePreCheck2: FileNotFoundException Occurred... " , e);
		} catch (IOException e) {
			log.error("PreChecker.validatePreCheck2: IOException Occurred... " , e);
		}catch (Exception e) {
			log.error("PreChecker.validatePreCheck2: Exception Occurred... " , e);
		}finally{
			try {
				if(bufferReader != null)
				{
					bufferReader.close();
				}
				if(inputStreamReader != null)
				{
					inputStreamReader.close();
				}
			} catch (IOException e) {
				log.error("PreChecker.validatePreCheck2: IOException occured in finally block !!!");
			}
			
		}
		return isStatusOk;
	}
	
	private boolean validateFreeMemory() {
		boolean moveToNextCheck = false;
		log.info("Check for CPU and resource utilization ---------->");
		log.info("PreCheck : validateFreeMemory ---->");
		boolean isFreeMemory = HelperUtility.checkMemoryUtilization();
		if (isFreeMemory == true) {
			log.info("PreCheck : validateFreeMemory <----" );
			moveToNextCheck= validateNetsimSimulation();
			
			return moveToNextCheck;
		}
		return moveToNextCheck;
	}
	
	private boolean validateNetsimSimulation() {
		log.info("Check for started nodes on Netsim ---------->");
		boolean moveToNextCheck = false;
		boolean isSGSNSimulationStarted = false;
		boolean isEPGSSRSimulationStarted = false;
		boolean isEPGJuniperSimulationStarted = false;
		boolean isSASNSimulationStarted = false;
		boolean isDSCSimulationStarted = false;
		boolean isMSCSimulationStarted = false;
		Host netsimHost = HelperUtility.getNetsimHost();

		NetSimContext context = NetSimCommandHandler.getContext(netsimHost);
		NetSimResult exec = context.exec(NetSimCommands.showStarted());

		CommandOutput commandOutput = exec.getOutput()[0];
		// logger.info("CommandOutput--->"+commandOutput);
		Map<String, List<Map<String, String>>> sections = commandOutput
				.asSections();
		log.info("CommandOutput as section ---->\n");
		log.info("Section Size : " + sections.size());
		log.debug("Sections ---->" + sections);
		if (sections.size() > 0) {
			Collection<List<Map<String, String>>> sectionsList = sections
					.values();
			for (List<Map<String, String>> section : sectionsList) {

				for (Map<String, String> rowMap : section) {
					String simulation = rowMap.get("Simulation/Commands").toLowerCase();
					if (simulation.contains("sgsn")) {
						isSGSNSimulationStarted = true;
						log.info("Started Simulation --->" + simulation);
						break;
					}
					if (simulation.contains("ssr")) {
						isEPGSSRSimulationStarted = true;
						log.info("Started Simulation --->" + simulation);
						break;
					}
					if (simulation.contains("juniper")) {
						isEPGJuniperSimulationStarted = true;
						log.info("Started Simulation --->" + simulation);
						break;
					}
					if (simulation.contains("sasn")) {
						isSASNSimulationStarted = true;
						log.info("Started Simulation --->" + simulation);
						break;
					}
					if (simulation.contains("dsc")) {
						isDSCSimulationStarted = true;
						log.info("Started Simulation --->" + simulation);
						break;
					}if (simulation.contains("msc")) {
						isMSCSimulationStarted = true;
						log.info("Started Simulation --->" + simulation);
						break;
					}
					
				}

			}

		}
		if (isSGSNSimulationStarted && isEPGSSRSimulationStarted
				&& isEPGJuniperSimulationStarted && isDSCSimulationStarted && isSASNSimulationStarted && isMSCSimulationStarted) {
			log.info("SGSN, EPG Juniper, EPG SSR, DSC, SASN , MSC Nodes are started on Netsim <---- ");
			moveToNextCheck = validateUserPermission();
		} else {
			if (!isSGSNSimulationStarted)
				log.error("PreCheck.validateNetsimSimulation --> No SGSN node started on netsim");
			if (!isEPGSSRSimulationStarted)
				log.error("PreCheck.validateNetsimSimulation --> No EPG SSR node started on netsim");
			if (!isEPGJuniperSimulationStarted)
				log.error("PreCheck.validateNetsimSimulation --> No EPG JUNIPER node started on netsim");
			if (!isDSCSimulationStarted)
				log.error("PreCheck.validateNetsimSimulation --> No DSC node started on netsim");
			if (!isSASNSimulationStarted)
				log.error("PreCheck.validateNetsimSimulation --> No SASN node started on netsim");
			if (!isMSCSimulationStarted)
				log.error("PreCheck.validateNetsimSimulation --> No MSC node started on netsim");
			
		}
		return moveToNextCheck;
	}
	

	private boolean validateUserPermission() {
		boolean moveToNextCheck = false;
		log.info("PreCheck4: Check whether user is permitted or not---------->");
		log.info("PreCheck : validateUserPermission ---->");
		boolean isUserPermitted = HelperUtility.checkUserPermission();
		if (isUserPermitted == true) {
			log.info("PreCheck : validateUserPermission <----");
			moveToNextCheck = validateRMIServices();
			return moveToNextCheck;
		}
		return moveToNextCheck;
	}
	
	
	
	/*private boolean validateNodeSyncStatus() {
		log.info("PreCheck : Checking for the node sync status---->");
		boolean moveToNextCheck = false;
		// get list of SGSN Nodes
		List<String> listOfSGSNNodes = HelperUtility
				.getNodeNameListFromCSHanlderWithFilter(
						NodeType.SGSN_OLD.getNeType(),
						NodeType.SGSN_OLD.getNeMIMName());
		// get list of SGSN-MME Nodes
		List<String> listOfECIMNodes = HelperUtility
				.getNodeNameListFromCSHanlderWithFilter(
						NodeType.SGSN_NEW.getNeType(),
						NodeType.SGSN_NEW.getNeMIMName());
		listOfSGSNNodes.addAll(listOfECIMNodes);
		// get list of EPG-SSR Nodes
		List<String> listOfEPGSSRNodes = HelperUtility
				.getNodeNameListFromCSHanlderWithFilter(
						NodeType.EPG_SSR.getNeType(),
						NodeType.EPG_SSR.getNeMIMName());
		// get list of EPG-Juniper Nodes
				List<String> listOfEPGJuniperNodes = HelperUtility
						.getNodeNameListFromCSHanlderWithFilter(
								NodeType.EPG_JUNIPER.getNeType(),
								NodeType.EPG_JUNIPER.getNeMIMName());
				// get list of EPG-Juniper Nodes
				List<String> listOfSASNNodes = HelperUtility
						.getNodeNameListFromCSHanlderWithFilter(
								NodeType.SASN.getNeType(),
								NodeType.SASN.getNeMIMName());
				// get list of EPG-Juniper Nodes
				List<String> listOfDSCNodes = HelperUtility
						.getNodeNameListFromCSHanlderWithFilter(
								NodeType.DSC.getNeType(),
								NodeType.DSC.getNeMIMName());
				// get list of EPG-Juniper Nodes
				List<String> listOfMSCNodes = HelperUtility
						.getNodeNameListFromCSHanlderWithFilter(
								NodeType.MSCBC.getNeType(),
								NodeType.MSCBC.getNeMIMName());
				
		boolean SGSNSyncStatus = false;
		boolean EPGJuniperSyncStatus = false;
		boolean EPGSSRSyncStatus = false;
		boolean DSCSyncStatus = false;
		boolean SASNSyncStatus = false;
		boolean MSCSyncStatus = false;
		if (!(listOfSGSNNodes == null) && !(listOfSGSNNodes.isEmpty())) {

			for (String fdn1 : listOfSGSNNodes) {
				// get value of mirrorMIBsynchStatus for each node
				String moAttributeValue = HelperUtility
						.getAttributeValueFromFDN(new Fdn(fdn1),
								"mirrorMIBsynchStatus");
				if (moAttributeValue.equals("3")
						|| moAttributeValue.equals("5")) {
					log.info("PreCheck.validateNodeSyncStatus.... " + fdn1
							+ "  Node is in sync");
					SGSNSyncStatus = true;
					break;
				}
			}
		} else
			log.info("PreCheck.validateNodeSyncStatus----------------No SGSN node found");
		if (!(listOfEPGSSRNodes == null) && !(listOfEPGSSRNodes.isEmpty())) {
			for (String fdn2 : listOfEPGSSRNodes) {
				// get value of mirrorMIBsynchStatus for each node
				String moAttributeValue = HelperUtility
						.getAttributeValueFromFDN(new Fdn(fdn2),
								"mirrorMIBsynchStatus");
				if (moAttributeValue.equals("3")
						|| moAttributeValue.equals("5")) {
					log.info("PreCheck.validateNodeSyncStatus.... " + fdn2
							+ "  Node is in sync");
					EPGSSRSyncStatus = true;
					break;
				}
			}
		} else
			log.info("PreCheck.validateNodeSyncStatus----------------No EPG-SSR node found");
		if (!(listOfEPGJuniperNodes == null)
				&& !(listOfEPGJuniperNodes.isEmpty())) {
			for (String fdn3 : listOfEPGJuniperNodes) {
				// get value of mirrorMIBsynchStatus for each node
				String moAttributeValue = HelperUtility
						.getAttributeValueFromFDN(new Fdn(fdn3),
								"mirrorMIBsynchStatus");
				if (moAttributeValue.equals("5")) {
					log.info("PreCheck.validateNodeSyncStatus.... " + fdn3
							+ "  Node is in sync");
					EPGJuniperSyncStatus = true;
					break;
				}
			}
		} else
			log.info("PreCheck.validateNodeSyncStatus----------------No EPG-Juniper node found");
		if (!(listOfSASNNodes == null)
				&& !(listOfSASNNodes.isEmpty())) {
			for (String fdn4 : listOfSASNNodes) {
				// get value of mirrorMIBsynchStatus for each node
				String moAttributeValue = HelperUtility
						.getAttributeValueFromFDN(new Fdn(fdn4),
								"mirrorMIBsynchStatus");
				if (moAttributeValue.equals("3") || moAttributeValue.equals("5")) {
					log.info("PreCheck.validateNodeSyncStatus.... " + fdn4
							+ "  Node is in sync");
					SASNSyncStatus = true;
					break;
				}
			}
		} else
			log.info("PreCheck.validateNodeSyncStatus----------------No SASN node found");
		if (!(listOfDSCNodes == null)
				&& !(listOfDSCNodes.isEmpty())) {
			for (String fdn5 : listOfDSCNodes) {
				// get value of mirrorMIBsynchStatus for each node
				String moAttributeValue = HelperUtility
						.getAttributeValueFromFDN(new Fdn(fdn5),
								"mirrorMIBsynchStatus");
				if (moAttributeValue.equals("3") || (moAttributeValue.equals("5"))) {
					log.info("PreCheck.validateNodeSyncStatus.... " + fdn5
							+ "  Node is in sync");
					DSCSyncStatus = true;
					break;
				}
			}
		} else
			log.info("PreCheck.validateNodeSyncStatus----------------No DSC node found");
		if (!(listOfMSCNodes == null)
				&& !(listOfMSCNodes.isEmpty())) {
			for (String fdn6 : listOfMSCNodes) {
				// get value of mirrorMIBsynchStatus for each node
				String moAttributeValue = HelperUtility
						.getAttributeValueFromFDN(new Fdn(fdn6),
								"mirrorMIBsynchStatus");
				if (moAttributeValue.equals("3") || (moAttributeValue.equals("5"))) {
					log.info("PreCheck.validateNodeSyncStatus.... " + fdn6
							+ "  Node is in sync");
					MSCSyncStatus = true;
					break;
				}
			}
		} else
			log.info("PreCheck.validateNodeSyncStatus----------------No MSC node found");

		if (!SGSNSyncStatus) {
			log.error("PreCheck.validateNodeSyncStatus----------------No SGSN Node is sync");
		}
		if (!EPGSSRSyncStatus) {
			log.error("PreCheck.validateNodeSyncStatus----------------No EPGSSR Node is sync");
		}
		if (!EPGJuniperSyncStatus) {
			log.error("PreCheck.validateNodeSyncStatus----------------No EPGJuniper Node is sync");
		}if (!SASNSyncStatus) {
			log.error("PreCheck.validateNodeSyncStatus----------------No SASN Node is sync");
		}
		if (!DSCSyncStatus) {
			log.error("PreCheck.validateNodeSyncStatus----------------No DSC Node is sync");
		}
		if (!MSCSyncStatus) {
			log.error("PreCheck.validateNodeSyncStatus----------------No MSC Node is sync");
		}
		if (SGSNSyncStatus && EPGSSRSyncStatus && EPGJuniperSyncStatus && SASNSyncStatus && DSCSyncStatus && MSCSyncStatus) {
			log.info("PreCheck : Nodes are sync <-----");
			moveToNextCheck = validateRMIServices();
		}
		return moveToNextCheck;

	}

	*/
	
	


	/**
	 * The below Pre-Check will check the RMI Services required for the product to work.
	 * @throws IOException 
	 */
	private boolean validateRMIServices() /*throws PreCheckFailedException*/{
		boolean isStatusOk = false;
		log.info("PreCheck3: Check Product and Its Dependent RMI Services ---------->");
		
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferReader = null;
		String rmiServiceName = "";
		int numOfRmiServicesNotActive = 0;
		Set<String> rmiServiceNotActiveSet = new HashSet<String>();
		try {
			final String responseOfCommand = HelperUtility.executeCommand(StaticConstants.SMTOOL_RMI_SERVICES_LIST_COMMAND);
			inputStreamReader = new InputStreamReader(PreChecker.class.getClassLoader().
					getResourceAsStream(StaticConstants.MISCELLENOUS_DIRECTORY_PATH + StaticConstants.RMI_SERVICES_NAME_FILE));
			bufferReader = new BufferedReader(inputStreamReader);
			while ((rmiServiceName = bufferReader.readLine()) != null) {
				if(!responseOfCommand.contains(rmiServiceName))
				{
					log.error("PreCheck3: rmiService: " + rmiServiceName + " is not enabled... So, Skipping the other checks !!!");
					numOfRmiServicesNotActive++;
					rmiServiceNotActiveSet.add(rmiServiceName);
				}else
				{
					log.info("PreCheck3: rmiService: " + rmiServiceName + " Status: OK");
				}
			}
			if(numOfRmiServicesNotActive > 0)
			{
				log.error("PreChecker.validatePreCheck3: The rmiServices " + rmiServiceNotActiveSet + " are not up... So, skipping the further Pre-Checks !!!");
				
			}else
			{
				log.info("PreCheck3: Check Product and Its Dependent RMI Services <----------");
				isStatusOk = validateHeapDump();
			}
		} catch (FileNotFoundException e) {
			log.error("PreChecker.validatePreCheck3: FileNotFoundException Occurred... " , e);
		} catch (IOException e) {
			log.error("PreChecker.validatePreCheck3: IOException Occurred... " , e);
		}catch (Exception e) {
			log.error("PreChecker.validatePreCheck3: Exception Occurred... " , e);
		}finally{
			try {
				if(bufferReader != null)
				{
					bufferReader.close();
				}
				if(inputStreamReader != null)
				{
					inputStreamReader.close();
				}
			} catch (IOException e) {
				log.error("PreChecker.validatePreCheck3: IOException Occurred in finally block... ", e);
			}
		}
		return isStatusOk;
		
	}
	
	
	private boolean validateHeapDump() {
		boolean moveToNextCheck = false;
		log.info("PreCheck4: Check whether there is any  Heap Dump---------->");
		log.info("PreCheck : validateHeapDump ---->");
		boolean isHeapDump = HelperUtility.checkHeapDump();
		if (isHeapDump == false) {
			moveToNextCheck=true;
			log.info("PreCheck : successfully validated HeapDump <----");
			return moveToNextCheck;
		}
		return moveToNextCheck;
	}
	
}
