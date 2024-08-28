package com.ericsson.nsd.taf.test.cases;

import com.ericsson.nsd.taf.test.robustness.PostGuiLaunchCheck;
import com.ericsson.nsd.taf.test.util.ConfigureServer;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.TestCase;


public class VerifyConfigurationAfterGuiLaunch extends TorTestCaseHelper implements TestCase{
	private static final Logger log = Logger.getLogger(VerifyConfigurationAfterGuiLaunch.class);
	
	@Test
	public void verifyConfigurationAfterGuiLaunch() {
	log.info("VerifyConfigurationAfterGuiLaunch.verifyConfigurationAfterGuiLaunch ----------> ");
	//Run the PostGuiLaunch Checks to further verify the configuration on the server
	log.info("verifyConfigurationAfterGuiLaunch.launchClient going to load log files on server");
    ConfigureServer.startLogsOnServer();
    log.info("verifyConfigurationAfterGuiLaunch.launchClient The required log files are loaded on the server");

	boolean areAllOsgiServicesUp = PostGuiLaunchCheck.verifyRequiredOsgiServices();
	log.info("VerifyConfigurationAfterGuiLaunch.verifyConfigurationAfterGuiLaunch: areAllOsgiServicesUp: " + areAllOsgiServicesUp);
	if(areAllOsgiServicesUp)
	{
		log.info("VerifyConfigurationAfterGuiLaunch.verifyConfigurationAfterGuiLaunch: All Osgi Services Are Up And Running !!! Proceeding to next check !!!");
		areAllOsgiServicesUp = verifyServerSideCache();
	}else
	{
		/**
		 * Explicitly closing the client as one of the post Gui Launch check got failed!!!
		 * So, it is required to close the client before stopping the running of the suite.
		 */
		try{
			PRErecTest.getInstance().closeClient();
		}catch(Exception e){
			log.error(" Exception occured while getting instance of PRERecTest class--->", e);
		}
		assertTrue(areAllOsgiServicesUp);
	}
	log.info("PRErecTest.verifyConfigurationAfterGuiLaunch <---------- ");
}
	
	private boolean verifyServerSideCache()
    {
                   
                    log.info("VerifyConfigurationAfterGuiLaunch.verifyServerSideCache ----------> ");
                    boolean isServerCacheVerified = PostGuiLaunchCheck.verifyServerSideCache();
                    log.info("VerifyConfigurationAfterGuiLaunch.verifyServerSideCache: ServerCache properly up --> " + isServerCacheVerified);
                    if(isServerCacheVerified)
                    {
                                    log.info("PRErecTest.verifyServerSideCache: ServerCache is up !!! Proceeding to next check !!!");
                    }else
                    {
                                    log.info("PRErecTest.verifyServerSideCache: ServerCache is not properly up !!!");
                                    try{
                                                    PRErecTest.getInstance().closeClient();
                                    }catch(Exception e){
                                                    log.error(" Exception occured while getting instance of PRERecTest class--->", e);
                                    }
                                  
                    }
                    log.info("PRErecTest.verifyServerSideCache <---------- ");
                    return isServerCacheVerified;
    }


}
