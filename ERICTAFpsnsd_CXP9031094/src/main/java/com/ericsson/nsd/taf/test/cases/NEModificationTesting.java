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
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.nsd.taf.test.constants.StaticConstants;
import com.ericsson.nsd.taf.test.operators.INeModificationOperator;
import com.ericsson.nsd.taf.test.operators.NeModificationOperatorCLI;

public class NEModificationTesting extends TorTestCaseHelper implements TestCase{

	Logger logger = Logger.getLogger(NEModificationTesting.class);

	@Inject
	private OperatorRegistry<INeModificationOperator> neModificationOperator;
	
	
	
	/*@Context(context = { Context.CLI })
	@Test(groups = { "KGB","CDB" })
	@DataDriven(name = "NeAddTest")
	public void copyFileTesting(){
		
		copyFiles();
		assertEquals("ok", "ok");
		 	
	}*/
	
 @TestId(id = "OSS-29142_Func_1", title = "MO Addition in ONRM")
	@Context(context = { Context.CLI })
	@Test(groups = { "KGB","CDB" })
	@DataDriven(name = "NeAddTest")
	public void MOAdditionViewTesting(@Input("viewName") String viewName, @Output("expectedOut") String expected){
	 	
	 	logger.info("NEModificationTesting.MOAdditionViewTesting ---->" + " For view: " + viewName);
	 	setTestStep("NEModificationTesting.MOAdditionViewTesting ---->" + " For view: " + viewName);
		INeModificationOperator obj = neModificationOperator.provide(INeModificationOperator.class);
		String command = null;		
		if(viewName.equalsIgnoreCase("SGSN")){
			command=DataHandler.getAttribute(StaticConstants.SGSNNetsimCommandADD).toString();
		}else if(viewName.equalsIgnoreCase("GGSN")){
			command=DataHandler.getAttribute(StaticConstants.GGSNNetsimCommandADD).toString();
		}else if(viewName.equalsIgnoreCase("MSCS")){
			command=DataHandler.getAttribute(StaticConstants.MSCSNetsimCommandADD).toString();
		}else if(viewName.equalsIgnoreCase("CPG")){
			command=DataHandler.getAttribute(StaticConstants.CPGNetsimCommandADD).toString();
		}else if(viewName.equalsIgnoreCase("EPG")){
			command=DataHandler.getAttribute(StaticConstants.EPGNetsimCommandADD).toString();
		}
		String actualValue;
		try {
			actualValue = obj.checkAddition(viewName, command);
			assertEquals(expected, actualValue);
		} catch (Exception e) {
			logger.error("NEModificationTesting.MOAdditionViewTesting: Exception occured while checking Addition " + " For view: " + viewName , e);
			e.printStackTrace();
		}
		setTestStep("NEModificationTesting.MOAdditionViewTesting <----" + " For view: " + viewName);
        logger.info("NEModificationTesting.MOAdditionViewTesting <----" + " For view: " + viewName);
		
	}
	
 @TestId(id = "OSS-29142_Func_1", title = "MO Deletion in ONRM")
	@Context(context = { Context.CLI })
	@Test(groups = { "KGB","CDB" })
	@DataDriven(name = "NeAddTest")
	public void MODeletionViewTesting(@Input("viewName") String viewName, @Output("expectedOut") String expected){
	 
	 	logger.info("NEModificationTesting.MODeletionViewTesting ---->" + " For view: " + viewName);
	 	setTestStep("NEModificationTesting.MODeletionViewTesting ---->" + " For view: " + viewName);
		INeModificationOperator obj = neModificationOperator.provide(INeModificationOperator.class);
		String command = null;
		
		if(viewName.equalsIgnoreCase("SGSN")){
			command=DataHandler.getAttribute(StaticConstants.SGSNNetsimCommandDEL).toString();
		}else if(viewName.equalsIgnoreCase("GGSN")){
			command=DataHandler.getAttribute(StaticConstants.GGSNNetsimCommandDEL).toString();
		}else if(viewName.equalsIgnoreCase("MSCS")){
			command=DataHandler.getAttribute(StaticConstants.MSCSNetsimCommandDEL).toString();
		}else if(viewName.equalsIgnoreCase("CPG")){
			command=DataHandler.getAttribute(StaticConstants.CPGNetsimCommandDEL).toString();
		}else if(viewName.equalsIgnoreCase("EPG")){
			command=DataHandler.getAttribute(StaticConstants.EPGNetsimCommandDEL).toString();
		}

		String actualValue;
		try {
			actualValue = obj.checkDeletion(viewName, command);
			assertEquals(expected, actualValue);
		} catch (Exception e) {
			logger.error("NEModificationTesting.MODeletionViewTesting: Exception occured while checking Deletion " + " For view: " + viewName , e);
			e.printStackTrace();
		}
		setTestStep("NEModificationTesting.MODeletionViewTesting <----" + " For view: " + viewName);
        logger.info("NEModificationTesting.MODeletionViewTesting <----" + " For view: " + viewName);
        
	}


	/*@Context(context = { Context.CLI })
	@Test(groups = { "KGB","CDB" })
	@DataDriven(name = "MOAddTest")
	public void moModificationTest(@Input("viewName") String viewName, @Output("expectedOut") String expected){
		NeModificationOperator obj = neModificationOperator.provide(NeModificationOperator.class);
		String command = null;
		if(viewName.equalsIgnoreCase("RoutingArea")){
			command=DataHandler.getAttribute(PsNsdOperator.RoutiongAreaNetsimCommand).toString();
		}else if(viewName.equalsIgnoreCase("TrackingArea")){
			command=DataHandler.getAttribute(PsNsdOperator.TrackingAreaNetsimCommand).toString();
		}
		String actualValue;
		try {
			actualValue = obj.checkAddition(viewName, command);
			logger.debug("NSD_TAF: sgsnView Testing result is checkSGSNviewenabled "+ actualValue);
			assertEquals(expected, actualValue);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/

	
	
}
