/**
 * 
 */
package com.ericsson.nsd.taf.test.robustness;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.ericsson.nsd.taf.test.constants.StaticConstants;
import com.ericsson.nsd.taf.test.util.HelperUtility;


/**
 * @author xchashr
 *
 */
public class PostGuiLaunchCheck {
	
	private static final Logger log = Logger.getLogger(PostGuiLaunchCheck.class);
	
	public static boolean verifyRequiredOsgiServices() {
		log.info("PostGuiLaunchCheck.verifyRequiredOsgiServices -----------> ");
		boolean areAllServicesUp = false;
		final String responseFromGroovy = HelperUtility.invokeGroovyMethodOnArgs(HelperUtility.getApiClient(),
				"NsdConfigurationCheckGroovy", "verifyRequiredOsgiServices");
		areAllServicesUp = Boolean.parseBoolean(responseFromGroovy.trim());
		log.info("PostGuiLaunchCheck.verifyRequiredOsgiServices <----------- ");
		return areAllServicesUp;
	}
	
	public static boolean verifyServerSideCache() {
        InputStreamReader inputStreamReader = null;
        BufferedReader bufferReader = null;
        String moType = null;
        int moCount = 0;
        int moListSizefromGroovy = 0;
        int moListSizefromCS = 0;
        boolean isServerCacheVerified = true;
        List<String> moListNotUp = new ArrayList<String>();
        log.info("PostGuiLaunchCheck.verifyServerSideCache -----------> ");

        try {
        	
                        inputStreamReader = new InputStreamReader(PostGuiLaunchCheck.class.getClassLoader().
                                                        getResourceAsStream(StaticConstants.MISCELLENOUS_DIRECTORY_PATH + StaticConstants.MO_TYPE_SUPPORT));
                       
                       
                        		bufferReader = new BufferedReader(inputStreamReader);
                        while ((moType = bufferReader.readLine()) != null) {
                        				moListSizefromCS = 0;
                                        String responseFromGroovy = HelperUtility.invokeGroovyMethodOnArgs(HelperUtility.getApiClient(),"NsdMoTopologyGroovy", "getMoListSize", moType);
                                        moListSizefromGroovy = Integer.parseInt(responseFromGroovy);           
                                       log.error("List Size for MO  "+moType+" from Groovy is "+moListSizefromGroovy);
                                        List<String> responseFromCS= HelperUtility.getMoListFromCs(moType);
                                        if(!(responseFromCS == null)){
                                        	moListSizefromCS = responseFromCS.size();
                                        }
                                        
                                        log.error("List Size of MO "+moType+"  from CS is "+moListSizefromCS);
                                        if(!(moListSizefromCS == moListSizefromGroovy))
                                        {
                                        	
                                                        moCount ++;
                                                        moListNotUp.add(moType);                                                       
                                                                                        
                                        }
                                        
                        }
                        if(moCount > 0){
                        				isServerCacheVerified = false;
                                        log.error(" Server cache is not properly up for "+ moCount +".....");
                                        log.error("Mo list for which cache not properly up --->");
                                        Iterator iterator = moListNotUp.iterator();
                                        while(iterator.hasNext()){
                                                        log.error(iterator.next());
                                        }
                        }
                                        
                        
                        
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
//    isServerCacheVerified = Boolean.parseBoolean(responseFromGroovy.trim());
        log.info("PostGuiLaunchCheck.verifyServerSideCache <----------- ");
       
}
        return isServerCacheVerified;

}

}
