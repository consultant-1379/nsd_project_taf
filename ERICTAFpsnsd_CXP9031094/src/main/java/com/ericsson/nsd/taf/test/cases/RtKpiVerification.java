/**
 * 
 */
package com.ericsson.nsd.taf.test.cases;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.DataDriven;
import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.annotations.Output;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.nsd.taf.test.operators.IRtKpiOperator;

/**
 * @author xchashr
 *
 */
public class RtKpiVerification extends TorTestCaseHelper implements TestCase {
	
	@Inject
	private OperatorRegistry<IRtKpiOperator> rtKpiOperatorRegistry;
	
	Logger logger = Logger.getLogger(RtKpiVerification.class);
	
	@Context(context = { Context.CLI })
	@Test(groups = { "KGB"})
	@DataDriven(name = "RtKpiVerification")
	public void verifyStartRtKpi(@Input("Node_Type") String nodeType, @Input("Counter_Name") String counterName, @Output("Expected_Output") String expected){
		
		try {
			setTestCase("100_Test","verifyStartRtKpi");
			logger.info("RtKpiVerification.verifyStartRtKpi ---->");
			setTestStep("RtKpiVerification.verifyStartRtKpi ---->");
			IRtKpiOperator rtKpiOpearator = rtKpiOperatorRegistry.provide(IRtKpiOperator.class);
			String actualValue = rtKpiOpearator.verifyStartRtkpi(nodeType, counterName);
			assertTrue(actualValue.contains(expected));
			logger.info("RtKpiVerification.verifyStartRtKpi <----");
			setTestStep("RtKpiVerification.verifyStartRtKpi <----");
		} catch (Exception e) {
			logger.error("Exception occured while verifying startRtKpi---> " , e);
		}

	}

}
