package com.ericsson.nsd.taf.test.cases ;

import java.util.concurrent.TimeUnit;

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

public class NSDMC_CDB_FunctionalTest extends TorTestCaseHelper implements TestCase {
	
	Logger logger = Logger.getLogger(NSDMC_CDB_FunctionalTest.class);
	
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
    @TestId(id = "OSS-35855_Func_1", title = "NSD MC Restart Test")
    @Context(context = {Context.CLI})
    @Test(groups={"KGB"})
    @DataDriven(name = "NSDMC_CDB_RestartTest")
    public void mCRestartTest(@Input("MCName") String mcName,@Input("commandRef") String comm,@Output("expectedOut") String expectedOut) {

      //  NSDMCOperator nSDMCOperator = nSDMCProvider.provide(NSDMCOperator.class);
    	/*ViewOperator nSDMCOperator = nSDMCProvider.provide(ViewOperator.class);*/
    	logger.info("NSDMC_CDB_FunctionalTest.mCRestartTest ---->");
	 	setTestStep("NSDMC_CDB_FunctionalTest.mCRestartTest ---->");
	 	
	 	final String commandForOnlineStatus="/opt/ericsson/bin/smtool -list";
    	final String commandForOnline="/opt/ericsson/bin/smtool -online ";
      //  NSDMCOperator nSDMCOperator = nSDMCProvider.provide(NSDMCOperator.class);
    	IViewOperator nSDMCOperator = viewOperator.provide(IViewOperator.class);
    	setTestInfo("NSD MC Status Check");

    	String resultOfMCStateOnlineStatus= nSDMCOperator.executeCommandStatus(mcName, commandForOnlineStatus);
    	System.out.println("NSDMC_CDB_FunctionalTest Class -->mCRestartTest Method: MC State is: "+resultOfMCStateOnlineStatus);
    	if(resultOfMCStateOnlineStatus.contains("failed")||resultOfMCStateOnlineStatus.contains("offline")){
    		System.out.println("The MC was in: " + resultOfMCStateOnlineStatus + " so onlining the MC" );
    		final String onlineStatus = nSDMCOperator.executeCommandOnline(mcName, commandForOnline);
    		System.out.println("Online status: "+ onlineStatus);
    		//wait 6 minutes that cache is updated.
    		try {
				TimeUnit.MINUTES.sleep(6);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    	}
    	
    	
        String result= nSDMCOperator.executeCommandoffline(mcName,comm);
        assertTrue(result.contains(expectedOut));
        setTestStep("NSDMC_CDB_FunctionalTest.mCRestartTest <----");
        logger.info("NSDMC_CDB_FunctionalTest.mCRestartTest <----");
    } 
    
}