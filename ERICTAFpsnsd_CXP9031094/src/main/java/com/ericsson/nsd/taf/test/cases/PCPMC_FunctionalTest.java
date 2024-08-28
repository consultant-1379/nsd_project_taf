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

public class PCPMC_FunctionalTest extends TorTestCaseHelper implements TestCase {
	
	Logger logger = Logger.getLogger(PCPMC_FunctionalTest.class);
	
	/* @Inject
    private OperatorRegistry<NSDMCOperator> nSDMCProvider; */
    @Inject
    private OperatorRegistry<IViewOperator> viewOperator;
    
    /*@Inject
    private NSDMCGetter nSDMCGetter;*/
  
    /**
     * @DESCRIPTION This will check whether MC is online or not
     * @PRE ERICcpsnsd should be insatlled  on OSS
     * @PRIORITY HIGH
     */
    @TestId(id = "OSS-35855_Func_8", title = "MC Status Test")
    @Context(context = {Context.CLI})
    @Test(groups={"KGB"})
    @DataDriven(name = "PCPMC_FunctionalTest")
    public void mCStatusTest(@Input("MCName") String mcName,@Input("commandRef") String comm,@Output("expectedOut") String expectedOut) {

       // NSDMCOperator nSDMCOperator = nSDMCProvider.provide(NSDMCOperator.class);
    	/*ViewOperator nSDMCOperator = nSDMCProvider.provide(ViewOperator.class);*/
    	logger.info("PCPMC_FunctionalTest.mCStatusTest ---->");
	 	setTestStep("PCPMC_FunctionalTest.mCStatusTest ---->");
    	IViewOperator nSDMCOperator = viewOperator.provide(IViewOperator.class);
        String result= nSDMCOperator.executeCommandStatus(mcName,comm);
        assertTrue(result.contains(expectedOut));
        setTestStep("PCPMC_FunctionalTest.mCStatusTest <----");
        logger.info("PCPMC_FunctionalTest.mCStatusTest <----");
    }
    
    
    /**
     * @DESCRIPTION This will check whether MC is online or not
     * @PRE ERICcpsnsd should be insatlled  on OSS
     * @PRIORITY HIGH
     */
    @TestId(id = "OSS-35855_Func_8", title = "MC Offline Test")
    @Context(context = {Context.CLI})
    @Test(groups={"KGB"})
    @DataDriven(name = "PCPMC_OfflineTest")
    public void mCOfflineTest(@Input("MCName") String mcName,@Input("commandRef") String comm,@Output("expectedOut") String expectedOut) {

      //  NSDMCOperator nSDMCOperator = nSDMCProvider.provide(NSDMCOperator.class);
    	/*ViewOperator nSDMCOperator = nSDMCProvider.provide(ViewOperator.class);*/
    	logger.info("PCPMC_FunctionalTest.mCOfflineTest ---->");
	 	setTestStep("PCPMC_FunctionalTest.mCOfflineTest ---->");
    	IViewOperator nSDMCOperator = viewOperator.provide(IViewOperator.class);
        String result= nSDMCOperator.executeCommandoffline(mcName,comm);
        assertTrue(result.contains(expectedOut));
        setTestStep("PCPMC_FunctionalTest.mCOfflineTest <----");
        logger.info("PCPMC_FunctionalTest.mCOfflineTest <----");
    }
    
    
    /**
     * @DESCRIPTION This will check whether MC is online or not
     * @PRE ERICcpsnsd should be insatlled  on OSS
     * @PRIORITY HIGH
     */
    @TestId(id = "OSS-35855_Func_8", title = "MC Online Test")
    @Context(context = {Context.CLI})
    @Test(groups={"KGB"})
    @DataDriven(name = "PCPMC_OnlineTest")
    public void mCOnlineTest(@Input("MCName") String mcName,@Input("commandRef") String comm,@Output("expectedOut") String expectedOut) {

       // NSDMCOperator nSDMCOperator = nSDMCProvider.provide(NSDMCOperator.class);
    	/*ViewOperator nSDMCOperator = nSDMCProvider.provide(ViewOperator.class);*/
    	logger.info("PCPMC_FunctionalTest.mCOnlineTest ---->");
	 	setTestStep("PCPMC_FunctionalTest.mCOnlineTest ---->");
    	IViewOperator nSDMCOperator = viewOperator.provide(IViewOperator.class);
        String result= nSDMCOperator.executeCommandOnline(mcName,comm);
        assertTrue(result.contains(expectedOut));
        setTestStep("PCPMC_FunctionalTest.mCOnlineTest <----");
        logger.info("PCPMC_FunctionalTest.mCOnlineTest <----");
    }

	
	
}