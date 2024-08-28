/**
 * 
 */
package com.ericsson.nsd.taf.test.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.ericsson.nsd.taf.test.cases.PRErecTest;
import com.ericsson.nsd.taf.test.constants.StaticConstants;

/**
 * @author xchashr
 *
 */
public class PropertyFileCreator {

	private static final Logger log = Logger.getLogger(PropertyFileCreator.class);
	/**
	 * @param args
	 */
	public static void createPropertyFileAtRuntime(final String osMachineType) {
		
		log.info("PropertyFileCreator:createPropertyFileAtRuntime ------> ");
		Properties prop = new Properties();
		OutputStream output = null;
		FileInputStream fileInputStream = null;
	 
		try {
			output = new FileOutputStream(StaticConstants.PROPERTY_FILE_CREATION_PATH_WITH_PROPERTY_FILE_NAME);
			fileInputStream = new FileInputStream(StaticConstants.PROPERTY_FILE_CREATION_PATH_WITH_PROPERTY_FILE_NAME);
			
			// set the properties value
			prop.setProperty(StaticConstants.CEX_BUNDLE_HEARTBEAT_PROPERTY_NAME, StaticConstants.CEX_BUNDLE_HEARTBEAT_PROPERTY_VALUE);
			if(osMachineType.contains(StaticConstants.I386_OS_MACHINE_TYPE)){
				prop.setProperty(StaticConstants.CEX_CONFIG_PROPERTY_NAME, StaticConstants.I386_CONFIG_INI_FILE);
			}else if (osMachineType.contains(StaticConstants.SPARC_OS_MACHINE_TYPE)){
				prop.setProperty(StaticConstants.CEX_CONFIG_PROPERTY_NAME, StaticConstants.SPARC_CONFIG_INI_FILE);
			}
			prop.setProperty(StaticConstants.CEX_SCRIPT_PROPERTY_NAME, StaticConstants.NSD_EPC_LAUNCHER_FILE);
	 
			// save properties to project root folder
			prop.store(output, null);
			
			//Load the properties at runtime
			prop.load(fileInputStream);
		
			log.info("PropertyFileCreator:createPropertyFileAtRuntime <------ "+ "\n prop: " );
			prop.list(System.out);
	 
		} catch (IOException io) {
			log.error("PropertyFileCreator:createPropertyFileAtRuntime: Exception Occured: ", io);
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					log.error("PropertyFileCreator:createPropertyFileAtRuntime: Exception Occured While Closing OutputStream: ", e);
				}
			}
	 
		}
	  

	}

}
