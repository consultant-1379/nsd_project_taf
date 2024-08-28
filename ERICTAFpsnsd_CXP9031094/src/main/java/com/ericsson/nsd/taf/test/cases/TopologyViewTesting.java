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
import com.ericsson.nsd.taf.test.operators.ViewOperatorImpl;

public class TopologyViewTesting extends TorTestCaseHelper implements TestCase {
	Logger logger = Logger.getLogger(TopologyViewTesting.class);

	@Inject
	private OperatorRegistry<IViewOperator> viewOperator;
	
	/**
	 * @DESCRIPTION Select ONRM Root in Subnetwork and SASN tab selected in
	 *              topology view and validate SASN present in Topology view with
	 *              seg_cs
	 * @PRE EPC-NSD MC is online. NSD client has been launched and the Content
	 *      View page for the desired object is open. Nodes are connected and
	 *      synched. License is installed.
	 * @PRIORITY HIGH
	 */
	@Context(context = { Context.CLI })
	@Test(groups = { "KGB" })
	@DataDriven(name = "TopologyViewTesting")
	@TestId(id = "OSS-83692", title = "Verify Topology View")
	public void TopologyViewTesting(@Input("Id") String Id,@Input("Title") String Title,@Input("viewName") String viewName, @Output("expectedOut") String expected){
		logger.info("TopologyViewTesting.TopologyViewTesting ---->");
		IViewOperator nSDMCOperator = viewOperator.provide(IViewOperator.class);
		String actualValue = nSDMCOperator.checkTopologyData(viewName);
		assertEquals(expected, actualValue);
		logger.info("TopologyViewTesting.TopologyViewTesting <----");
	}
}
