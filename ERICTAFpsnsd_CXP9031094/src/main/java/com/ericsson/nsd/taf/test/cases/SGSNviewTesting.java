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
//import com.ericsson.nsd.taf.test.operators.SGSNviewAPIOperator;

public class SGSNviewTesting extends TorTestCaseHelper implements TestCase {
	Logger logger = Logger.getLogger(SGSNviewTesting.class);

	@Inject
/*	private NSDMCGetter nSDMCGetter;*/
	/*private IViewOperator viewOperator;*/
	private OperatorRegistry<IViewOperator> viewOperator;
	
	/**
	 * @DESCRIPTION Select ONRM Root in Subnetwork and SGSN tab selected in
	 *              status view and validate SGSN present in status view with
	 *              seg_cs
	 * @PRE EPC-NSD MC is online. NSD client has been launched and the Content
	 *      View page for the desired object is open. Nodes are connected and
	 *      synched. License is installed.
	 * @PRIORITY HIGH
	 */
	@Context(context = { Context.CLI })
	@Test(groups = { "KGB","CDB" })
	@DataDriven(name = "StatusViewTest")
	@TestId(id = "OSS-78789_Func_1", title = "Verify Status View")
	public void StatusViewTesting(@Input("Id") String Id,@Input("Title") String Title,@Input("viewName") String viewName, @Output("expectedOut") String expected){
		
		setTestCase(Id,Title);
		logger.info("SGSNviewTesting.StatusViewTesting ---->");
		setTestStep("SGSNviewTesting.StatusViewTesting ---->");
		IViewOperator nSDMCOperator = viewOperator.provide(IViewOperator.class);
		String actualValue = nSDMCOperator.checkViewData(viewName);
		assertEquals(expected, actualValue);
		logger.info("SGSNviewTesting.StatusViewTesting <----");
		setTestStep("SGSNviewTesting.StatusViewTesting <----");

	}
	
	/**
	 * @DESCRIPTION Initiate the synch for the motype mentioned in the csv.
	 * @PRE EPC-NSD MC is online. 
	 * 		NSD client has been launched.
	 * 		Nodes are connected and are synched. 
	 * 		License is installed.
	 * @PRIORITY HIGH
	 */
	/* @Context(context = { Context.CLI })
	@Test(groups = { "KGB" })
	@DataDriven(name = "SynchronizeNode")
	public void synchronizeNode(@Input("Id") String id, @Input("Title") String title, @Input("Mo_Type") String nodeType, @Output("Expected_Output") String expectedOutput){
		
		setTestCase(id,title);
		logger.info("SGSNviewTesting.synchronizeNode: MoType: " + nodeType + " ---->");
		setTestStep("SGSNviewTesting.synchronizeNode: MoType: " + nodeType + " ---->");
		IViewOperator nSDMCOperator = viewOperator.provide(IViewOperator.class);
		String actualValue = nSDMCOperator.synchronizeNode(nodeType);
		assertEquals(expectedOutput, actualValue);
		logger.info("SGSNviewTesting.StatusViewTesting: MoType: " + nodeType + "  <----");
		setTestStep("SGSNviewTesting.StatusViewTesting: MoType: " + nodeType + "  <----");

	}*/
	
}
