package com.ericsson.nsd.taf.test.cases;

import javax.inject.Inject;

import com.ericsson.cifwk.taf.TestCase;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.DataDriven;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.Output;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.nsd.taf.test.operators.IAlarmOperator;

public class AlarmViewTesting extends TorTestCaseHelper implements TestCase {

	@Inject
	private OperatorRegistry<IAlarmOperator> alarmOperatorRegistry;

	Logger logger = Logger.getLogger(AlarmViewTesting.class);

	@Context(context = { Context.CLI })
	@Test(groups = { "KGB" })
	@DataDriven(name = "AlarmVerification")
	@TestId(id = "OSS-83690", title = "Alarm View Verification")
	public void verifyAlarmView(@Input("Id") String Id,@Input("Node_Type") String nodeType,
			@Output("Expected_Output") String expected) {

		try {
			
			logger.info("AlarmViewTesting.verifyAlarmView ---->");
			IAlarmOperator alarmOperator = alarmOperatorRegistry.provide(IAlarmOperator.class);			
			String actualValue = alarmOperator.verifyAlarmView(nodeType);
			assertTrue(actualValue.contains(expected));
			logger.info("AlarmViewTesting.verifyAlarmView <----");
			
		} catch (Exception e) {
			logger.error("Exception occured while verifying Alarm View---> ", e);
		}

	}

}
