package com.ericsson.nsd.taf.test.cases;

import com.ericsson.nsd.taf.test.operators.ILogOperator;


import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.DataDriven;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.Output;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;

public class LogsUtilityTest extends TorTestCaseHelper implements TestCase {
	Logger logger = Logger.getLogger(LogsUtilityTest.class);
	
	@Inject
	private OperatorRegistry<ILogOperator> viewOperator;
	
//	@TestId(id = "OSS-63778", title = "Log Functionality Verify")
	@Context(context = {Context.CLI})
	@Test(groups={"KGB"})
	@DataDriven(name = "LOG_FUNCTIONALITY_VERIFY")
	public void logFunctionalityVerify(@Input("nodeType") String nodeType,@Input("logFileType") String logFileType,@Output("expectedOut") String expectedOut) {
		try{
				
		logger.info("LogsUtilityTest.logFunctionalityVerify ----> "+nodeType+ " " + logFileType);
	 	setTestStep("LogsUtilityTest.logFunctionalityVerify ---->");
	 	ILogOperator nSDMCOperator = viewOperator.provide(ILogOperator.class);
	 	String result= nSDMCOperator.verifyLogFunctionality(nodeType, logFileType);
		assertTrue(result.contains("PASSED"));
		setTestStep("LogsUtilityTest.logFunctionalityVerify <----");
        logger.info("LogsUtilityTest.logFunctionalityVerify <----");
		}
		catch (Exception e) {
 			logger.error("LogsUtilityTest.logFunctionalityVerify<---- Exception Occured: ",  e);
 			setTestInfo("LogsUtilityTest.logFunctionalityVerify<---- " + e.getMessage());
	}
	}
}
