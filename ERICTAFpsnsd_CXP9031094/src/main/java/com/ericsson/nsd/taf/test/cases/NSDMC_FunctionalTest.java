package com.ericsson.nsd.taf.test.cases ;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.DataDriven;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.Output;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.nsd.taf.test.operators.IViewOperator;

public class NSDMC_FunctionalTest extends TorTestCaseHelper implements TestCase {
	
	Logger logger = Logger.getLogger(NSDMC_FunctionalTest.class);
	
	/* @Inject
    private OperatorRegistry<NSDMCOperator> nSDMCProvider; */
    @Inject
    private OperatorRegistry<IViewOperator> viewOperator;
    
    /*@Inject
    private NSDMCGetter nSDMCGetter;*/
  
    /**
     * @DESCRIPTION This will check whether MC is online or not
     * @PRE ERICcpsnsd should be installed  on OSS
     * @PRIORITY HIGH
     */
    @TestId(id = "OSS-35855_Func_1", title = "MC Status Test")
    @Context(context = {Context.CLI})
    @Test(groups={"KGB","CDB"})
    @DataDriven(name = "NSDMC_FunctionalTest")
    public void mCStatusTest(@Input("MCName") String mcName,@Input("commandRef") String comm,@Output("expectedOut") String expectedOut) {

       // NSDMCOperator nSDMCOperator = nSDMCProvider.provide(NSDMCOperator.class);
    	/*ViewOperator nSDMCOperator = nSDMCProvider.provide(ViewOperator.class);*/
    	logger.info("NSDMC_FunctionalTest.mCStatusTest ---->");
	 	setTestStep("NSDMC_FunctionalTest.mCStatusTest ---->");
    	IViewOperator nSDMCOperator = viewOperator.provide(IViewOperator.class);
        String result= nSDMCOperator.executeCommandStatus(mcName,comm);
        assertTrue(result.contains(expectedOut));
        setTestStep("NSDMC_FunctionalTest.mCStatusTest <----");
        logger.info("NSDMC_FunctionalTest.mCStatusTest <----");
    }
    
    
    /**
     * @DESCRIPTION This will check whether MC is online or not
     * @PRE ERICcpsnsd should be installed  on OSS
     * @PRIORITY HIGH
     */
    @TestId(id = "OSS-35855_Func_1", title = "MC Offline Test")
    @Context(context = {Context.CLI})
    @Test(groups={"KGB"})
    @DataDriven(name = "NSDMC_OfflineTest")
    public void mCOfflineTest(@Input("MCName") String mcName,@Input("commandRef") String comm,@Output("expectedOut") String expectedOut) {

      //  NSDMCOperator nSDMCOperator = nSDMCProvider.provide(NSDMCOperator.class);
    	/*ViewOperator nSDMCOperator = nSDMCProvider.provide(ViewOperator.class);*/
    	logger.info("NSDMC_FunctionalTest.mCOfflineTest ---->");
	 	setTestStep("NSDMC_FunctionalTest.mCOfflineTest ---->");
    	IViewOperator nSDMCOperator = viewOperator.provide(IViewOperator.class);
        String result= nSDMCOperator.executeCommandoffline(mcName,comm);
        assertTrue(result.contains(expectedOut));
        setTestStep("NSDMC_FunctionalTest.mCOfflineTest <----");
        logger.info("NSDMC_FunctionalTest.mCOfflineTest <----");
        //TODO VERIFY:RESPONSE As Expected
    }
    
    
    /**
     * @DESCRIPTION This will check whether MC is online or not
     * @PRE ERICcpsnsd should be installed  on OSS
     * @PRIORITY HIGH
     */
    @TestId(id = "OSS-35855_Func_1", title = "MC Online Test")
    @Context(context = {Context.CLI})
    @Test(groups={"KGB"})
    @DataDriven(name = "MC_OnlineTest")
    public void mCOnlineTest(@Input("MCName") String mcName,@Input("commandRef") String comm,@Output("expectedOut") String expectedOut) {

       // NSDMCOperator nSDMCOperator = nSDMCProvider.provide(NSDMCOperator.class);
    	/*ViewOperator nSDMCOperator = nSDMCProvider.provide(ViewOperator.class);*/
    	logger.info("NSDMC_FunctionalTest.mCOnlineTest ---->");
	 	setTestStep("NSDMC_FunctionalTest.mCOnlineTest ---->");
    	IViewOperator nSDMCOperator = viewOperator.provide(IViewOperator.class);
        String result= nSDMCOperator.executeCommandOnline(mcName,comm);
        assertTrue(result.contains(expectedOut));
        setTestStep("NSDMC_FunctionalTest.mCOnlineTest <----");
        logger.info("NSDMC_FunctionalTest.mCOnlineTest <----");
    }

	
	
}