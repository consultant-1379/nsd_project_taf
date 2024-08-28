/**
 * 
 */
package com.ericsson.nsd.taf.test.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ericsson.nsd.taf.test.constants.StaticConstants;

/**
 * @author xchashr
 *
 */
public class SavePluginsFromNexusToServerUtility {
	
	private static final Logger log = Logger.getLogger(SavePluginsFromNexusToServerUtility.class);

	private final SavePluginsFromNexusToServerUtility singleton_intance = new SavePluginsFromNexusToServerUtility();
	
	private static final List<String> pluginsToRemoveFromServerList = new ArrayList<String>();
	
	public SavePluginsFromNexusToServerUtility getInstance() {
		return singleton_intance;
	}
	
	public static void copyPluginsToServer() {
		final List<String> listOfCommandsToBeExecuted = new ArrayList<>();
		try {
			String artifactId = "";
			final String nsdRStateInstalledOnServer = HelperUtility.getInstalledNsdPackageVersion();
			final String pathWhereToCopyThePluginCommand = StaticConstants.CD_COMMAND + StaticConstants.SPACE + StaticConstants.NSD_CLIENT_PLUGIN_PATH;
			log.debug("SavePluginsFromNexusToServerUtility.copyPluginsToServer: pathWhereToCopyThePlugin: " + pathWhereToCopyThePluginCommand);
//			/listOfCommandsToBeExecuted.add(pathWhereToCopyThePluginCommand);
			//Reading the pluginsToDownloadFromNexus.txt file to get the list of all the plugins to download from the NEXUS.
			//final String pluginsToDownloadFilePath = HelperUtility.getAnyFileFilePath(StaticConstants.PLUGINS_NAME_FILE, StaticConstants.PLUGINS_FILE_FOLDER_PATH);	
			System.out.println("Testing!!! : " + SavePluginsFromNexusToServerUtility.class.getClassLoader().
					getResource(StaticConstants.PLUGINS_FILE_FOLDER_PATH + StaticConstants.PLUGINS_NAME_FILE).getPath());
			InputStreamReader inputStreamReader = new InputStreamReader(SavePluginsFromNexusToServerUtility.class.getClassLoader().
					getResourceAsStream(StaticConstants.PLUGINS_FILE_FOLDER_PATH + StaticConstants.PLUGINS_NAME_FILE));
			BufferedReader bufferReader = new BufferedReader(inputStreamReader);
			while ((artifactId = bufferReader.readLine()) != null) {
				if(!artifactId.trim().isEmpty()){
					final String pluginNameToDownloadFromNexus = artifactId + StaticConstants.DELIMITER_HYPHEN + 
							nsdRStateInstalledOnServer + StaticConstants.DELIMITER_DOT + StaticConstants.EXTENSION_TYPE;
					log.debug("SavePluginsFromNexusToServerUtility.copyPluginsToServer: nsdRStateInstalledOnServer: " + nsdRStateInstalledOnServer);
					log.debug("SavePluginsFromNexusToServerUtility.copyPluginsToServer: pluginNameToDownloadFromNexus: " + pluginNameToDownloadFromNexus);
					
					final String pluginNameToBeSavedOnServer = pluginNameToDownloadFromNexus.replace("-", ".");
					log.debug("\nSavePluginsFromNexusToServerUtility.copyPluginsToServer: pluginNameToBeSavedOnServer: " + pluginNameToBeSavedOnServer);
							
					final String pluginDownloadCommandFromNexus = StaticConstants.WGET_COMMAND + StaticConstants.SPACE +
							StaticConstants.NEXUS_URL + StaticConstants.NEXUS_REPO_PATH + artifactId + StaticConstants.DELIMITER_BACK_SLASH
							+ nsdRStateInstalledOnServer + StaticConstants.DELIMITER_BACK_SLASH + pluginNameToDownloadFromNexus 
							+ StaticConstants.SPACE + StaticConstants.WGET_PARAMETER + StaticConstants.SPACE  
							+ StaticConstants.NSD_CLIENT_PLUGIN_PATH + StaticConstants.DELIMITER_BACK_SLASH + pluginNameToBeSavedOnServer;
					
					log.debug("SavePluginsFromNexusToServerUtility.copyPluginsToServer: jarDownloadCommandFromNexus: " + pluginDownloadCommandFromNexus);
					
					final String changePermissionOfThePluginCommand = StaticConstants.CHMOD_COMMAND_WITH_PERMISSIONS + StaticConstants.SPACE 
							+ StaticConstants.NSD_CLIENT_PLUGIN_PATH + StaticConstants.DELIMITER_BACK_SLASH + pluginNameToBeSavedOnServer ;		
					
					listOfCommandsToBeExecuted.add(pluginDownloadCommandFromNexus);
					listOfCommandsToBeExecuted.add(changePermissionOfThePluginCommand);
					
					pluginsToRemoveFromServerList.add(pluginNameToBeSavedOnServer);
				}else
				{
					log.error("SavePluginsFromNexusToServerUtility.copyPluginsToServer: artifactId is empty ");
				}
			}
			
			log.info("SavePluginsFromNexusToServerUtility.copyPluginsToServer: listOfCommandsToBeExecuted: " +listOfCommandsToBeExecuted);
			log.info("SavePluginsFromNexusToServerUtility.copyPluginsToServer: pluginsToRemoveFromServerList: " +pluginsToRemoveFromServerList);
			final Map<String, String> commandResponse = HelperUtility.executeBulkCommands(listOfCommandsToBeExecuted);
		} catch (Exception e) {
			log.error("SavePluginsFromNexusToServerUtility.copyPluginsToServer: Exception while copying the plugins from Nexus to the Server... Exception occurred!!! \n", e);
		}
	}
	
	public static void removePluginsFromTheServer() {
		
		log.info("SavePluginsFromNexusToServerUtility.removePluginsFromTheServer: removePluginCommand: -------------->" );
		if(pluginsToRemoveFromServerList != null && !pluginsToRemoveFromServerList.isEmpty())
		{
			final List<String> commandToExecuteList = new ArrayList<String>();
			final String pathFromWhereToDeleteThePluginCommand = StaticConstants.CD_COMMAND + StaticConstants.SPACE + StaticConstants.NSD_CLIENT_PLUGIN_PATH;
		//	commandToExecuteList.add(pathFromWhereToDeleteThePluginCommand);
			for (String pluginNameToRemoveFromServer : pluginsToRemoveFromServerList) {
				commandToExecuteList.add(StaticConstants.REMOVE_COMMAND + StaticConstants.SPACE + StaticConstants.NSD_CLIENT_PLUGIN_PATH + StaticConstants.DELIMITER_BACK_SLASH + pluginNameToRemoveFromServer);
			}
			log.info("SavePluginsFromNexusToServerUtility.removePluginsFromTheServer: commandToExecuteList: " +commandToExecuteList); 
			HelperUtility.executeBulkCommands(commandToExecuteList);
			log.info("SavePluginsFromNexusToServerUtility.removePluginsFromTheServer: removePluginCommand: <-------------- ");
		}else
		{
			log.error("SavePluginsFromNexusToServerUtility.removePluginsFromTheServer: pluginsToRemoveFromServerList is null or is Empty !!!");
		}
	
	}
}
