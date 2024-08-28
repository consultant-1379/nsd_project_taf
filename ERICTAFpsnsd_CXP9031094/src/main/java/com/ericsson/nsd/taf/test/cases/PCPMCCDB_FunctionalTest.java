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

public class PCPMCCDB_FunctionalTest extends TorTestCaseHelper implements TestCase {
	
	Logger logger = Logger.getLogger(PCPMCCDB_FunctionalTest.class);
	
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
    @TestId(id = "OSS-35855_Func_8", title = "PCP MC Restart Test")
    @Context(context = {Context.CLI})
    @Test(groups={"KGB"})
    @DataDriven(name = "PCPMCCDB_RestartTest")
    public void mCRestartTest(@Input("MCName") String mcName,@Input("commandRef") String comm,@Output("expectedOut") String expectedOut) {

      //  NSDMCOperator nSDMCOperator = nSDMCProvider.provide(NSDMCOperator.class);
    	logger.info("PCPMCCDB_FunctionalTest.mCRestartTest ---->");
	 	setTestStep("PCPMCCDB_FunctionalTest.mCRestartTest ---->");
    	IViewOperator nSDMCOperator = viewOperator.provide(IViewOperator.class);
        String result= nSDMCOperator.executeCommandoffline(mcName,comm);
        assertTrue(result.contains(expectedOut));
        setTestStep("PCPMCCDB_FunctionalTest.mCRestartTest <----");
        logger.info("PCPMCCDB_FunctionalTest.mCRestartTest <----");
       
    }
    
    
  	
}