package com.ericsson.nsd.taf.test.cases;


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

public class PoolModificationTesting extends TorTestCaseHelper implements TestCase{

	Logger logger = Logger.getLogger(PoolModificationTesting.class);
	
	@Inject
    private OperatorRegistry<IViewOperator> /*PsNsdOperator*/ viewOperator;
	
	 @TestId(id = "OSS-29142_Func_7", title = "MC Status Test")
	    @Context(context = {Context.CLI})
	    @Test(groups={"KGB"})
	    @DataDriven(name = "PoolModification")
	    public void poolAddition(@Input("POOLName") String PoolName,@Output("expectedOut") String expectedOut) {
	       // NSDMCOperator nSDMCOperator = nSDMCProvider.provide(NSDMCOperator.class);
		 	logger.info("PoolModificationTesting.poolAddition ---->");
		 	setTestStep("PoolModificationTesting.poolAddition ---->");
	    	IViewOperator poolModificationOper = viewOperator.provide(IViewOperator.class);/*PsNsdOperator.provide(ViewOperator.class);*/
	        String result= poolModificationOper.checkPoolAddition(PoolName);
	        assertTrue(result.contains(expectedOut));
	        setTestStep("PoolModificationTesting.poolAddition <----");
	        logger.info("PoolModificationTesting.poolAddition <----");
	    }
	 
	 @TestId(id = "OSS-29142_Func_8", title = "MC Status Test")
	    @Context(context = {Context.CLI})
	    @Test(groups={"KGB"})
	    @DataDriven(name = "PoolModification")
	    public void poolDeletion(@Input("POOLName") String PoolName,@Output("expectedOut") String expectedOut) {

	       // NSDMCOperator nSDMCOperator = nSDMCProvider.provide(NSDMCOperator.class);
		 	logger.info("PoolModificationTesting.poolAddition ---->");
		 	setTestStep("PoolModificationTesting.poolAddition ---->");
	    	IViewOperator poolModificationOper = viewOperator.provide(IViewOperator.class);/*PsNsdOperator.provide(ViewOperator.class);*/
	        String result= poolModificationOper.checkPoolDeletion(PoolName);
	        assertTrue(result.contains(expectedOut));
	        setTestStep("PoolModificationTesting.poolAddition <----");
	        logger.info("PoolModificationTesting.poolAddition <----");
	    }
}
