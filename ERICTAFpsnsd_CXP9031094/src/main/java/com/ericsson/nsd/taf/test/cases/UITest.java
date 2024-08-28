package com.ericsson.nsd.taf.test.cases;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.nsd.taf.test.operators.IUIOperator;


public class UITest extends TorTestCaseHelper implements TestCase {
	Logger logger = Logger.getLogger(UITest.class);

	@Inject
/*	private NSDMCGetter nSDMCGetter;*/
	/*private IViewOperator viewOperator;*/
	private OperatorRegistry<IUIOperator> uiOperator;
	
	/**
	 * @DESCRIPTION Launch SSH Shell on SGSN node and Telnet Shell on EPG node from EPC GUI
	 *  (FTOL TC-53 NSD-EPC10 )
	 * @PRE 
	 * @PRIORITY HIGH
	 */

	@Context(context = { Context.UI })
	@Test(groups = { "KGB","CDB" })
	//@DataDriven(name = "StatusViewTest")
	public void StatusViewTesting(/*@Input("Id") String Id,@Input("Title") String Title,@Input("viewName") String viewName, @Output("expectedOut") String expected*/){
		
	//	setTestCase(Id,Title);
		logger.info("UITesting.SSHShellView  ---->");
		//setTestStep("UITesting.SSHShellView ---->");
		IUIOperator nSDMCOperator = uiOperator.provide(IUIOperator.class);
		logger.info("UITesting.SSHShellView 111 ---->");
		//viewOperator = new ViewOperatorImpl();
		/*viewObject=PsNsdOperator.getInstance();*/
		//UIOperator u=new 
		boolean actualValue = nSDMCOperator.validadeUILaunchSSHShell();
		assertEquals(actualValue,true);
		//logger.info("UITesting.SSHShellView <----");
		//setTestStep("UITesting.SSHShellView <----");

	}
}
