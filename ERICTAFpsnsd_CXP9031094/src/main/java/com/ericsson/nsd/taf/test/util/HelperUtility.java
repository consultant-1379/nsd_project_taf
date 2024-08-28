/**
 * 
 */
package com.ericsson.nsd.taf.test.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.data.User;
import com.ericsson.cifwk.taf.data.UserType;
import com.ericsson.cifwk.taf.handlers.RemoteFileHandler;
import com.ericsson.cifwk.taf.tools.cli.handlers.impl.RemoteObjectHandler;
import com.ericsson.cifwk.taf.osgi.client.ApiClient;
import com.ericsson.cifwk.taf.tools.cli.CLI;
import com.ericsson.cifwk.taf.tools.cli.CLICommandHelper;
import com.ericsson.cifwk.taf.tools.cli.Shell;
import com.ericsson.cifwk.taf.utils.FileFinder;
//import com.ericsson.cifwk.taf.utils.FileUtils;
import com.ericsson.nsd.taf.test.constants.NodeType;
import com.ericsson.nsd.taf.test.constants.StaticConstants;
import com.ericsson.oss.taf.hostconfigurator.HostGroup;
import com.ericsson.oss.taf.cshandler.model.Fdn;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.oss.taf.cshandler.model.Filter;
import com.ericsson.oss.taf.cshandler.SimpleFilterBuilder;
import com.ericsson.oss.taf.cshandler.CSHandler;
import com.ericsson.oss.taf.cshandler.CSTestHandler;
import com.ericsson.oss.taf.cshandler.CSDatabase;
import com.ericsson.cifwk.taf.tools.cli.CLICommandHelper;




/**
 * @author xchashr
 *
 */
public class HelperUtility {
	
	private final HelperUtility singleton_instance = new HelperUtility();
	
	private static final Logger log = Logger.getLogger(HelperUtility.class);
	
	private static ApiClient apiClient;
	private static Host netsimHost;
	private static Map<String, String> mapOfAttributes= null;
	private static String NETSIMHOST = "netsim";
	private static final CSHandler csHandler1 = new CSTestHandler(
			HostGroup.getOssmaster(), CSDatabase.Segment);
	 private static final CLICommandHelper handler = new CLICommandHelper(HostGroup.getOssmaster(), HostGroup.getOssmaster().getNmsadmUser());
	public static ApiClient getApiClient() {
		return HelperUtility.apiClient;
	}

	public static void setApiClient(ApiClient apiClient) {
		System.out.println("Setting client in helper utility !!!");
		HelperUtility.apiClient = apiClient;
	}

	public static Host getHost() {
		return HostGroup.getOssmaster();
	}
	
	public static User getUser() {
		return getHost().getUsers(UserType.OPER).get(0);
	}
	 
	public static User getNmsadmUser() {
	    return HostGroup.getOssmaster().getNmsadmUser();
	    
	  }

	
	public static Map<String, String> getMapofattributes() {
        return mapOfAttributes;
        }
	
	public static void setMapOfAttributes(Map<String, String> mapofAttributes) {
         mapOfAttributes= mapofAttributes;
		}
	public static String executeCommand(final String command) {
		log.info("Command : " + command);
		final String stdout = handler.simpleExec(command);
		final int exitCode = handler.getCommandExitValue();

		if (exitCode != 0) {
			log.error("Error comes while writing to the shell "+stdout);
		} else {
			
			log.info("Command result: " + stdout);
		}
		return stdout;
		/*final Shell shell = initializeShell(getHost(), getUser());
		log.debug("command: " + command);
		shell.writeln(command);
		sleepFor(10000);
		final String responseOfCommand = shell.read();
		log.debug("responseOfCommand: " + responseOfCommand);
		return responseOfCommand;*/
		
	}
	//FOR NODE SYNCH 
	
	public static String execute(final String command) {
		final Shell shell = initializeShell(getHost(), getUser());
		log.debug("HelperUtility.executeCommand: command: " + command);
		log.info("Going to write command on Shell --->");
		shell.writeln(command);
		log.info("Execution of command on Shell done <---");
		log.info("Going to sleep for 5 sec --->");
        sleepFor(10000);
        log.info("Back from sleep <---");
        log.info("Going to read response from shell ---->");   
		final String responseOfCommand = shell.read();
		log.debug("HelperUtility.executeCommand: responseOfCommand: "
				+ responseOfCommand);
		return responseOfCommand;
		/*
		 * String[] parseOutput=responseOfCommand.split("\n"); return
		 * parseOutput[1].split(" ")[2];
		 */
	} 
	
	public static Shell initializeShell(final Host host, final User userName) {
		Shell shell = null;
		final CLI cli = new CLI(host, userName);
		if (shell == null) {
			shell = cli.openShell();
		}
		return shell;
	}
		//NEW NODE SYNCH FUNCTIONS END
	
	public static Map<String, String> executeBulkCommands(final List<String> commandsList) {
		final Map<String, String> commandResponseMap = new HashMap<String, String>();
		for (String command : commandsList) {
		log.info("Command : " + command);
		final String stdout = handler.simpleExec(command);
		final int exitCode = handler.getCommandExitValue();

		if (exitCode != 0) {
			log.error("Error comes while writing to the shell "+stdout);
		} else {
			log.info("Command result: " + stdout);
			log.info("Put Command  and result in map");
			commandResponseMap.put(command, stdout);
			
		}
		}
		return commandResponseMap;
		
		
	/*	log.info("HelperUtility.executeBulkCommands------->");
		final Map<String, String> commandResponseMap = new HashMap<String, String>();
		final Shell shell = initializeShell(getHost(), getUser());
		for (String command : commandsList) {
			log.debug("\n\ncommand: " + command);
			shell.writeln(command);
			sleepFor(10000);
			final String responseOfCommand = shell.read();
			commandResponseMap.put(command, responseOfCommand);
			log.debug("\nresponseOfCommand: " + responseOfCommand);
		}
		log.info("HelperUtility.executeBulkCommands <-------");
		return commandResponseMap;*/
	}
	
/*	public static Shell initializeShell(final Host host, final User userName){
		Shell shell = null;
		final CLI cli = new CLI(host, userName);
		if(shell == null){
			//log.debug("Creating new shell instance");
			shell = cli.openShell();
		}
		return shell;
	}
	*/
	public static String invokeGroovyMethodOnArgs(final ApiClient apiClient, final String className, final String method, final String... args) {

		String respVal = null;
		respVal = apiClient.invoke(className, method, args).getValue();
		log.debug("respVal :"+respVal);
		//log.info(String.format("Invoking %1$s: %2$s", method, respVal));
		return respVal;
	}
	
	public static String getMachineType () {
		final String osMachineType = executeCommand(StaticConstants.FETCH_OS_MACHINE_TYPE_COMMAND);
		return osMachineType;
	}
	
	//This has been updated as a part of TR HT99869.
	public static void copyIniFilesToLaunchGuiWithJava7(final String sourceFileName) {
		log.debug("copyIniFilesToLaunchGuiWithJava7 ------> ");
		try {
			log.debug("copyIniFilesToLaunchGuiWithJava7: sourceFileName: " + sourceFileName);
			final List<String>  sourceFilePathList= getAnyFileFilePath(sourceFileName, StaticConstants.TAF_DIRECTORY_PATH_WHERE_CONFIG_FILES_PRESENT);
			//PropertyFileCreator.createPropertyFileAtRuntime(osMachineType);
			if(sourceFilePathList != null && !sourceFilePathList.isEmpty())
			{
				log.debug("copyIniFilesToLaunchGuiWithJava7: sourceFilePathList is not null or empty !!!");
				for (String sourceFilePath : sourceFilePathList) {
					log.info("copyIniFilesToLaunchGuiWithJava7: Initiating copying of file: " + sourceFilePath);
					copyFilesFromLocalToRemoteServer(sourceFilePath, StaticConstants.NSD_EPC_INI_FILE);
					log.info("copyIniFilesToLaunchGuiWithJava7: Successful copying of file: " + sourceFilePath);
				}
			}else
			{
				log.error("copyIniFilesToLaunchGuiWithJava7: Exception occurred while copying the ini files: The sourceFilePath is either null or empty");
			}
			log.debug("copyIniFilesToLaunchGuiWithJava7 <------ ");
		} catch (Exception e) {
			log.error("copyIniFilesToLaunchGuiWithJava7: Exception occurred while copying the ini files: " , e);
		}
	}
	
	public static void copyFilesFromLocalToRemoteServer(final String sourceFilePath, final String destinationPath) {
		log.debug("HelperUtility.copyFilesFromLocalToRemoteServer ----------> ");
		try {
			final RemoteObjectHandler remoteObjectHandler = new RemoteObjectHandler(getHost(),HelperUtility.getNmsadmUser());
			remoteObjectHandler.copyLocalFileToRemote(sourceFilePath, destinationPath);
			log.debug("HelperUtility.copyFilesFromLocalToRemoteServer <---------- ");
		} catch (Exception e) {
			log.error("copyFilesFromLocalToRemoteServer: Exception occured: " , e);
		}
		
	}
	
	public static final String getInstalledNsdPackageVersion()
	{
		String rStateInstalled = "";
		final String commandResponse = executeCommand(StaticConstants.PKGINFO_COMMAND);
		log.debug("HelperUtility.executeCommand: commandResponse:\n" + commandResponse);
		final String[] linesInResponse = commandResponse.split("\n");
		for (String line : linesInResponse) {
			if(line.contains(StaticConstants.VERSION_STRING))
			{
				rStateInstalled = line.substring(line.lastIndexOf(":")+1, line.length()).trim();
				break;
			}
		}
		log.debug("HelperUtility.executeCommand: Returning rState: " + rStateInstalled);
		return rStateInstalled; 	
	}
	
	public static List<String> getAnyFileFilePath(final String fileName, final String folderPath) {
		List<String> files = null;
	    //String filePath = null;
	    try {
	    	
	    	files = FileFinder.findFile(fileName);
	    	if(files.size() > 0){
	    		log.info("HelperUtility.getAnyFileFilePath: Got the file Path as : " + files);
	    	}
	    	/*String classAbsolutePath =  HelperUtility.class.getResource("HelperUtility.class").getPath();
	    	InputStreamReader isr = new InputStreamReader(HelperUtility.class.getResourceAsStream(folderPath + fileName));;
	    	BufferedReader reader = new BufferedReader(isr);
	    	while ((filePath = reader.readLine()) != null) {
				System.out.println(filePath);
			}
	    	log.info("HelperUtility.getFilePath: classAbsolutePath: " + classAbsolutePath);
	    	if(classAbsolutePath.contains("target"))
	    	{
	    		log.debug("HelperUtility.getFilePath: classAbsolutePath contains string \"target\"");
	    		classAbsolutePath = classAbsolutePath.substring(0, classAbsolutePath.lastIndexOf("target")+6);
	    		log.info("HelperUtility.getFilePath: classAbsolutePath: " + classAbsolutePath);
	    	}
	    	if(classAbsolutePath.contains("file:"))
			{
	    		log.debug("HelperUtility.getFilePath: classAbsolutePath contains string \"file:\"");
	    		classAbsolutePath = classAbsolutePath.substring(classAbsolutePath.lastIndexOf("file:")+5, classAbsolutePath.length());
	    		log.info("HelperUtility.getFilePath: classAbsolutePath: " + classAbsolutePath);
			}
	    	if(classAbsolutePath.contains(".jar!"))
	    	{
	    		log.debug("HelperUtility.getFilePath: classAbsolutePath contains string \"\\.jar!\"");
	    		classAbsolutePath = classAbsolutePath.substring(0, classAbsolutePath.lastIndexOf(".jar!")+5);
	    		log.info("HelperUtility.getFilePath: classAbsolutePath: " + classAbsolutePath);
	    		final File jarFile = new File(classAbsolutePath);
		    	if(jarFile.isFile()) {  // Run with JAR file
		    		log.info("HelperUtility.getFilePath: It is a Jar File !!!");
		    	    final JarFile jar = new JarFile(jarFile);
		    	    final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
		    	    while(entries.hasMoreElements()) {
		    	    	final JarEntry nextElement = entries.nextElement();
		    	      //  final String name = entries.nextElement().getName();
		    	    	log.debug("HelperUtility.getFilePath: jarElementName: " + nextElement.getName().trim());
		    	        if (nextElement.getName().trim().contains(fileName)) { //filter according to the path
		    	            filePath = classAbsolutePath + nextElement.getName().trim();
		    	            log.debug("HelperUtility.getFilePath: Got the absolute Path: " + filePath);
		    	        }
		    	    }
		    	    jar.close();
		    	}
	    	}else
	    	{
				log.info("HelperUtility.getFilePath: classAbsolutePath: " + classAbsolutePath);
				final String folderNameThatContainsTheFile = classAbsolutePath + folderPath;
				log.info("HelperUtility.getFilePath: folderNameThatContainsTheFile: " + folderNameThatContainsTheFile);
				file = new File(folderNameThatContainsTheFile);
				if (file.isDirectory()) {
					log.info("HelperUtility.getFilePath: It is a directory");
					File[] allFiles = file.listFiles();
					if (allFiles.length != 0) {
						log.info("HelperUtility.getFilePath: Hurray found the file!!!");
						file = allFiles[0];
						for (File fileObj : allFiles) {
							if (fileObj.getName().trim().equals(fileName)) {
								filePath = fileObj.getAbsolutePath().trim();
							}
						}
						log.info("HelperUtility.getFilePath: file.getAbsolutePath: " + file.getAbsolutePath());
					}
				} else {
					log.error("HelperUtility.getFilePath: Unable to find the path: " + folderNameThatContainsTheFile);
				}
			}*/
	    } catch(Exception e){
	    	log.error("Exception occured while retrieving the path of the sourceFile that needs to be copied on the server: ", e);
	    }
	    
	    return files;
	}
	public static Host getNetsimHost() {
		if (netsimHost == null) {
			netsimHost = HostGroup.getAllNetsims().get(0);//DataHandler.getHostByName(NETSIMHOST);
		}
		return netsimHost;
	}
	public static boolean checkHeapDump() {
		boolean isHeapDump = false;
		String resultCmd = HelperUtility
				.executeCommand(StaticConstants.PROCESS_ID);
		String[] str = resultCmd.split("[\\r\\n]");
		int pid = 0;
		for (String s : str) {
			if (!s.isEmpty() && s.contains("-Ds=nsd_cn_ps")) {
				Pattern p = Pattern.compile("(\\w+\\s*)(\\d+)(.*)");
				Matcher m = p.matcher(s.trim());
				if (m.matches()) {
					pid = Integer.parseInt(m.group(2));
					log.info("pid is " + pid);

				}
			}
		}
		String heapDumpFiles = HelperUtility
				.executeCommand(StaticConstants.HEAPDUMP_CMD);
		// String[] heapFiles = heapDumpFiles.split("[\\n\\r]");
		if (heapDumpFiles.contains("" + pid)) {
			log.error("Heap dump generated at path /ossrc/upgrade/JREheapdumps/ with processid : "
					+ pid);
			isHeapDump = true;

		}
		return isHeapDump;

	}
	public static boolean checkCoreDump() {
		boolean isCoreDump = false;
		String coreDumpFiles = HelperUtility
				.executeCommand(StaticConstants.COREDUMP_CMD);
		if (coreDumpFiles.contains("nsd4epc")) {
			log.error("Core dump generated at path /ossrc/upgrade/core/ with key nsd4epc");
			isCoreDump = true;
		}
		return isCoreDump;
	}
	
	public static List<String> getNodeNameListFromCSHanlderWithFilter(
			final int neType, final String neMIMName) {

		final Filter filter = SimpleFilterBuilder.builder().attr("neType")
				.equalTo(neType).and().attr("neMIMName").equalTo(neMIMName)
				.build();
		log.error("Output from Filter:$" + filter + "$");
		final List<Fdn> listOfNodes = csHandler1.getByType("MeContext",
				new Filter(filter.toString()));
		List<String> nodeNameList = new ArrayList<String>();
		for (Fdn fdnObj : listOfNodes) {
			nodeNameList.add(fdnObj.getFdn());
		}
		return nodeNameList;
	}
	public static String getAttributeValueFromFDN(Fdn fdn, String attribute) {
		String attributeValue = csHandler1.getAttributeValue(fdn, attribute);
		return attributeValue;

	}
	
	public static boolean checkUserPermission() {
		boolean isUserPermitted = false;
		String response=HelperUtility.executeCommand(StaticConstants.USER_PERMISSION);
		log.debug("Memory Utilization---->"+response);
		if(response.contains("nmsadm"))
		{
			isUserPermitted=true;
		}
		return isUserPermitted;
}
	public static boolean checkMemoryUtilization() {
		boolean isCPUIdle = false;
		boolean isFreeMem = false;
		boolean isCheckPass =false;
		String response=HelperUtility.executeCommand(StaticConstants.MEMORY_UTILIZATION);
		log.debug("Memory Utilization---->"+response);
		String splittedResponse[]= response.split("[\\r\\n]");
		for(String s: splittedResponse)
		{
			if(!s.isEmpty() && s.contains("idle") ){
				
	            Pattern p = Pattern.compile("(\\w*\\s*\\w*):\\s*(\\S*)(%.*)");
	            Matcher m = p.matcher(s.trim());
	            log.info("Checking for CPU Utilization---->");
	            if (m.matches()) {
	            
	            			Float cpuIdle = Float.parseFloat(m.group(2));
	            			
	        					if(cpuIdle>=15)
	        					{
	        						isCPUIdle=true;
	        						log.info("CPU utilization is less than 85 per");
	        					}
	        					

	            	}
			}
			if(!s.isEmpty() && s.contains("free mem") ){
				
	            Pattern p = Pattern.compile("(\\w*:\\s\\S*\\s\\w*\\s\\w*,\\s)(\\w*)(\\s.*)");
	            Matcher m = p.matcher(s.trim());
	            log.info("Checking for free memory --->");
	            if (m.matches()) {
	            			String idleMemm= m.group(2);
	            			if(idleMemm.endsWith("M"))
	            			{
	            				String s1 =idleMemm.substring(0,idleMemm.length()-1);
	            				Float freeMem = Float.parseFloat(s1);
	            				log.info("free mem -------->" + freeMem+ "M");
	            				if(freeMem>=1024)
	            				{
	            					isFreeMem=true;
	            					log.info("Free memory is more than 1GB");
	            				}
	            			}
	            			if(idleMemm.endsWith("G"))
	            			{
	            				String s2 =idleMemm.substring(0,idleMemm.length()-1);
	            				Float freeMem = Float.parseFloat(s2);
	            				log.info("free mem -------->" + freeMem+ "G");
	            				if(freeMem>=1)
	            				{
	            					isFreeMem=true;
	            					log.info("Free memory is more than 1GB");
	            				}
	            			}
	        				
	            	}
			}

			if(isFreeMem && isCPUIdle)
				isCheckPass=true;
			
		}
		
		return isCheckPass;
	}
	
	public static List<String> getMoListFromCs(String moType){
		List<String> listofNodes = null;
		try{
			if(moType.equals("SGSN")){
		
			listofNodes = getNodeNameListFromCSHanlderWithFilter(NodeType.SGSN_OLD.getNeType(),NodeType.SGSN_OLD.getNeMIMName());
			List<String> listofNodes1 = getNodeNameListFromCSHanlderWithFilter(NodeType.SGSN_NEW.getNeType(),NodeType.SGSN_NEW.getNeMIMName());
			listofNodes.addAll(listofNodes1);
		}
		if(moType.equals("EPG")){
			listofNodes = getNodeNameListFromCSHanlderWithFilter(NodeType.EPG_JUNIPER.getNeType(),NodeType.EPG_JUNIPER.getNeMIMName());
			List<String> listofNodes1 = getNodeNameListFromCSHanlderWithFilter(NodeType.EPG_SSR.getNeType(),NodeType.EPG_SSR.getNeMIMName());
			listofNodes.addAll(listofNodes1);
		}
		if(moType.equals("GGSN"))
		{
			listofNodes = getNodeNameListFromCSHanlderWithFilter(NodeType.GGSN.getNeType(),NodeType.GGSN.getNeMIMName());
		}
		if(moType.equals("MSC"))
		{
			listofNodes = getNodeNameListFromCSHanlderWithFilter(NodeType.MSC.getNeType(),NodeType.MSC.getNeMIMName());
			List<String> listofNodes1 = getNodeNameListFromCSHanlderWithFilter(NodeType.MSCBC.getNeType(),NodeType.MSCBC.getNeMIMName());
			listofNodes.addAll(listofNodes1);
		}
		
		if(moType.equals("CPG"))
		{
			listofNodes = getNodeNameListFromCSHanlderWithFilter(NodeType.CPG.getNeType(),NodeType.CPG.getNeMIMName());
		}
		if(moType.equals("DNS"))
		{
			listofNodes = getNodeNameListFromCSHanlderWithFilter(NodeType.DNS.getNeType(),NodeType.DNS.getNeMIMName());
		}
		if(moType.equals("DSC"))
		{
			listofNodes = getNodeNameListFromCSHanlderWithFilter(NodeType.DSC.getNeType(),NodeType.DSC.getNeMIMName());
		}
		if(moType.equals("SASN"))
		{
			listofNodes = getNodeNameListFromCSHanlderWithFilter(NodeType.SASN.getNeType(),NodeType.SASN.getNeMIMName());
		}
		}
	catch(Exception e){
		log.error("HelperUtility.getMoListFromCs Exception Occured ", e);
		
	}
		
		return listofNodes;
	}
	
	public static Map<String, String> createDomainMo(final String moType) throws Exception{
		final Map<String, String> mapOfAttributes = new HashMap<>();
		/**
		 * The moDetails contains the Attributes in the form of:
		 * MO_FDN:MO_ID:MO_IP_ADDRESS
		 */
		log.info("*********************moDetails retrieved from grrovy---> "+moType );
		String moDetails=invokeGroovyMethodOnArgs(getApiClient(),"NsdMoTopologyGroovy", "getMoDetails", moType);
		log.info("moDetails retrieved from grrovy---> " + moDetails);
		if(!moDetails.trim().isEmpty() && moDetails.contains(":"))
		{
			String[] moAttributes = moDetails.split(":");
			if(moAttributes.length != 3)
			{
				throw new Exception("The MO Details fetched from the Groovy is not proper...");
			}
			mapOfAttributes.put("moFdn", moAttributes[0]);
			mapOfAttributes.put("moId", moAttributes[1]);
			mapOfAttributes.put("moIpAddress", moAttributes[2]);
		}
		return mapOfAttributes;
	}
	
	public static Map<String, String> createDomainMowithAttributes(final String moType, final String propertiesToFetch) throws Exception{
		final Map<String, String> mapOfAttributes = new HashMap<>();
		/**
		 * The moDetails contains the Attributes in the form of:
		 * MO_FDN:MO_ID:MO_IP_ADDRESS
		 */
		log.info("*********************moDetails retrieved from grrovy---> "+moType );
		String moDetails=invokeGroovyMethodOnArgs(getApiClient(),"NsdMoTopologyGroovy", "getMoDetailswithAttributes", moType,propertiesToFetch);
		log.info("moDetails retrieved from grrovy---> " + moDetails);
		if(!moDetails.trim().isEmpty() && moDetails.contains(";"))
		{	log.info("MoDetails --->"+moDetails);
	
			String[] moAttributes = moDetails.split(";");
			
			if(moAttributes==null)
			{
				throw new Exception("The MO Details fetched from the Groovy is not proper...");
			}
			log.info("moAttributes List Size :"+moAttributes.length);
			String[] propertiesList = propertiesToFetch.split(":");
			int size = propertiesList.length;
			
			for(int i=0;i<size;i++){
				mapOfAttributes.put(propertiesList[i], moAttributes[i]);
			}
			mapOfAttributes.put("moFdn", moAttributes[size]);
		}
		return mapOfAttributes;
	}
	
	public static String getManagedElementFdn(String moFdn){
		String managedElementFdn = "";
		List<Fdn> childMoList = csHandler1.getByType("ManagedElement");
		for(Fdn fdn : childMoList){
			if((fdn.getFdn()).contains(moFdn)){
				managedElementFdn = fdn.getFdn();
				break;
			}
		}
		return managedElementFdn;
	}
	
	private static void sleepFor(final long sleepTime) { 
        try { 
            Thread.sleep(sleepTime); 
        } catch (final InterruptedException e) { 
            log.error("Exception occured while sleep",e); 
        } 
    }


}
