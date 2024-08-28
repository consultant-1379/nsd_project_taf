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

public class PropertiesViewTesting extends TorTestCaseHelper implements TestCase {
	Logger logger = Logger.getLogger(SGSNviewTesting.class);

	@Inject
	private OperatorRegistry<IViewOperator> viewOperator;
	
	
	@Context(context = { Context.CLI })
	@Test(groups = { "KGB" })
	@DataDriven(name = "PropertiesViewData")
	@TestId(id = "OSS-81206", title = "Status and Property view testing")
	public void PropertiesTesting(@Input("id") String id,@Input("nodeType") String nodeType,@Input("propertiesToFetch") String propertiesToFetch, @Output("expectedOutput") String expected){
		
		
		logger.info("PropertiesViewTesting.PropertiesTesting ---->");
		setTestStep("PropertiesViewTesting.PropertiesTesting ---->");
		IViewOperator nSDMCOperator = viewOperator.provide(IViewOperator.class);
		String actualValue = nSDMCOperator.checkPropertiesViewData(nodeType,propertiesToFetch);
		assertEquals(expected, actualValue);
		logger.info("PropertiesViewTesting.PropertiesTesting <----");
		setTestStep("PropertiesViewTesting.PropertiesTesting <----");

	}
	
	
	
}
