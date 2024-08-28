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
import com.ericsson.nsd.taf.test.operators.ITopologyOperator;
//import com.ericsson.nsd.taf.test.operators.SGSNviewAPIOperator;

public class ToplogyArneviewTesting extends TorTestCaseHelper implements TestCase {
	Logger logger = Logger.getLogger(ToplogyArneviewTesting.class);

	@Inject
	private OperatorRegistry<ITopologyOperator> topologyOperator;
	
	
/*@TestId(id = "OSS-28475_Func_1", title = "OSS-28475_Func_1: Launch Status View with SGSN selected in "
			+ "EPC topology view and check that SGSN view is enabled")
	@Context(context = { Context.CLI })
	@Test(groups = { "KGB","CDB" })
	@DataDriven(name = "ViewTest")
	public void SgsnViewTesting(@Input("viewName") String viewName,@Output("expectedOut") String expected) {
		ViewOperator obj = psNsdOperator.provide(ViewOperator.class);
		try {
			boolean nsdStatus = obj.prepareNsd();
			logger.debug("NSD_TAF: NSD prepared" + nsdStatus);
		} catch (ContainerNotReadyException e) {
			e.printStackTrace();
		}
		String actualValue = obj.checkViewEnabled(viewName);
		setTestInfo("Seg CS command");
		logger.debug("NSD_TAF: sgsnView Testing result is checkSGSNviewenabled "+ actualValue);
		assertEquals(expected, actualValue);

	}*/
	 @TestId(id = "OSS-30824_Func_1", title = "MC Status Test")
	@Context(context = { Context.CLI })
	@Test(groups = { "KGB","CDB" })
	@DataDriven(name = "TopologyViewTest")
	public void TopologyViewTesting(@Input("moName") String moName, @Output("expectedOut") String expected){
		logger.info("ToplogyArneviewTesting.TopologyViewTesting ---->");
		setTestStep("ToplogyArneviewTesting.TopologyViewTesting ---->");
		ITopologyOperator topologyOperatorObj = topologyOperator.provide(ITopologyOperator.class);
		String actualValue = topologyOperatorObj.checkViewData(moName);
		assertEquals(expected, actualValue);
		setTestStep("ToplogyArneviewTesting.TopologyViewTesting <----");
        logger.info("ToplogyArneviewTesting.TopologyViewTesting <----");

	}

}
