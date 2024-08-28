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



public class PCPProfileTest extends TorTestCaseHelper implements TestCase {

	Logger logger = Logger.getLogger(PCPProfileTest.class);

	@Inject
	private OperatorRegistry<IViewOperator> viewOperator/*nSDMCProvider*/;



	@TestId(id = "OSS-29157_Func_5", title = "PCP Profile Create")
	@Context(context = {Context.CLI})
	@Test(groups = {"KGB", "CDB"})
	@DataDriven(name = "PCP_PROFILE")
	public void PCPProfileCreate(@Input("profileName") String prfName,@Output("expectedOut") String expectedOut){
		logger.info("PCPProfileTest.PCPProfileCreate ---->");
	 	setTestStep("PCPProfileTest.PCPProfileCreate ---->");
		IViewOperator nSDMCOperator = viewOperator.provide(IViewOperator.class);/*nSDMCProvider.provide(ViewOperator.class);*/
		String result= nSDMCOperator.creProfile(prfName);
		assertTrue(result.contains("OK"));
		setTestStep("PCPProfileTest.PCPProfileCreate <----");
        logger.info("PCPProfileTest.PCPProfileCreate <----");
	}

	@TestId(id = "OSS-63654", title = "PCP Profile Property Update")
	@Context(context = {Context.CLI})
	@Test(groups = {"KGB"})
	@DataDriven(name = "PCP_PROFILE_PROPERTY_UPDATE")
	public void PCPProfilePropertyUpdate(@Input("profileName") String prfName,@Input("mimModel") String mimModel,@Input("moc") String moc,@Input("attrName") String attrName,@Input("attrValue") String attrValue,@Output("expectedOut") String expectedOut){
		logger.info("PCPProfileTest.PCPProfilePropertyUpdate ---->");
	 	setTestStep("PCPProfileTest.PCPProfilePropertyUpdate ---->");
		IViewOperator nSDMCOperator = viewOperator.provide(IViewOperator.class);/*nSDMCProvider.provide(ViewOperator.class);*/
		String result= nSDMCOperator.profilePropertyUpdate(prfName,mimModel,moc,attrName,attrValue);
		assertTrue(result.contains("PASSED"));
		setTestStep("PCPProfileTest.PCPProfilePropertyUpdate <----");
        logger.info("PCPProfileTest.PCPProfilePropertyUpdate <----");
	}
	@TestId(id = "OSS-63654", title = "PCP Profile Property Delete")
	@Context(context = {Context.CLI})
	@Test(groups = {"KGB"})
	@DataDriven(name = "PCP_PROFILE_PROPERTY_DELETE")
	public void PCPProfilePropertyDelete(@Input("profileName") String prfName,@Output("expectedOut") String expectedOut){
		logger.info("PCPProfileTest.PCPProfilePropertyDelete ---->");
	 	setTestStep("PCPProfileTest.PCPProfilePropertyDelete ---->");
		IViewOperator nSDMCOperator = viewOperator.provide(IViewOperator.class);/*nSDMCProvider.provide(ViewOperator.class);*/
		String result= nSDMCOperator.profilePropertyDelete(prfName);
		assertTrue(result.contains("PASSED"));
		setTestStep("PCPProfileTest.PCPProfilePropertyDelete <----");
        logger.info("PCPProfileTest.PCPProfilePropertyDelete <----");
	}
	
	@TestId(id = "OSS-29157_Func_2", title = "PCP Profile Compare")
	@Context(context = {Context.CLI})
	@Test(groups={"KGB", "CDB"})
	@DataDriven(name = "PCP_ProfCompare")
	public void PCPProfileCompare(@Input("profileName1") String prfName,@Input("profileName2") String prfName2,@Input("planName") String PlanName,@Output("expectedOut") String expectedOut){
		/*ViewOperator nSDMCOperator = nSDMCProvider.provide(ViewOperator.class);*/
		logger.info("PCPProfileTest.PCPProfileCompare ---->");
	 	setTestStep("PCPProfileTest.PCPProfileCompare ---->");
		IViewOperator nSDMCOperator = viewOperator.provide(IViewOperator.class);
		String result= nSDMCOperator.compareProfile(prfName,prfName2,PlanName);
		assertTrue(result.contains("OK"));
		setTestStep("PCPProfileTest.PCPProfileCompare <----");
        logger.info("PCPProfileTest.PCPProfileCompare <----");
	}

	@TestId(id = "OSS-29157_Func_3", title = "PCP Profile Export")
	@Context(context = {Context.CLI})
	@Test(groups={"KGB", "CDB"})
	@DataDriven(name = "PCP_ImpExp")
	public void PCPProfileExport(@Input("profileName") String prfName,@Output("expectedOut") String expectedOut){
		logger.info("PCPProfileTest.PCPProfileExport ---->");
	 	setTestStep("PCPProfileTest.PCPProfileExport ---->");
		IViewOperator nSDMCOperator = viewOperator.provide(IViewOperator.class);
		String result= nSDMCOperator.exportProfile(prfName);
		assertTrue(result.contains("OK"));
		setTestStep("PCPProfileTest.PCPProfileExport <----");
        logger.info("PCPProfileTest.PCPProfileExport <----");
	}


	@TestId(id = "OSS-29157_Func_4", title = "PCP Profile Import")
	@Context(context = {Context.CLI})
	@Test(groups={"KGB", "CDB"})
	@DataDriven(name = "PCP_ImpExp")
	public void PCPProfileImport(@Input("profileName") String prfName,@Output("expectedOut") String expectedOut){
		logger.info("PCPProfileTest.PCPProfileImport ---->");
	 	setTestStep("PCPProfileTest.PCPProfileImport ---->");
		IViewOperator nSDMCOperator = viewOperator.provide(IViewOperator.class);
		String result= nSDMCOperator.importProfile(prfName);
		assertTrue(result.contains("OK"));
		setTestStep("PCPProfileTest.PCPProfileImport <----");
        logger.info("PCPProfileTest.PCPProfileImport <----");
	}

	@TestId(id = "OSS-29157_Func_6", title = "PCP Profile Update")
	@Context(context = {Context.CLI})
	@Test(groups={"KGB", "CDB"})
	@DataDriven(name = "PCP_PROFILE")
	public void PCPProfileUpdate(@Input("profileName") String prfName,@Output("expectedOut") String expectedOut){
		logger.info("PCPProfileTest.PCPProfileUpdate ---->");
	 	setTestStep("PCPProfileTest.PCPProfileUpdate ---->");
		IViewOperator nSDMCOperator = viewOperator.provide(IViewOperator.class);
		String result= nSDMCOperator.updateProfile(prfName);
		assertTrue(result.contains("OK"));
		setTestStep("PCPProfileTest.PCPProfileUpdate <----");
        logger.info("PCPProfileTest.PCPProfileUpdate <----");
	}

	@TestId(id = "OSS-29157_Func_1", title = "PCP Profile Delete")
	@Context(context = {Context.CLI})
	@Test(groups={"KGB", "CDB"})
	@DataDriven(name = "PCP_PROFILE")
	public void PCPProfileDelete(@Input("profileName") String prfName,@Output("expectedOut") String expectedOut){
		logger.info("PCPProfileTest.PCPProfileDelete ---->");
	 	setTestStep("PCPProfileTest.PCPProfileDelete ---->");
		IViewOperator nSDMCOperator = viewOperator.provide(IViewOperator.class);
		String result= nSDMCOperator.delProfile(prfName);
		assertTrue(result.contains("OK"));
		setTestStep("PCPProfileTest.PCPProfileDelete <----");
        logger.info("PCPProfileTest.PCPProfileDelete <----");
	}
	
	@TestId(id = "OSS-63633", title = "PCP Profile Verify")
	@Context(context = {Context.CLI})
	@Test(groups={"KGB", "CDB"})
	@DataDriven(name = "PCP_PROFILE_VERIFY")
	public void PCPProfileVerify(@Input("profileName") String prfName,@Output("expectedOut") String expectedOut){
		logger.info("PCPProfileTest.PCPProfileVerify ---->");
	 	setTestStep("PCPProfileTest.PCPProfileVerify ---->");
		IViewOperator nSDMCOperator = viewOperator.provide(IViewOperator.class);/*nSDMCProvider.provide(ViewOperator.class);*/
		String result= nSDMCOperator.verProfile(prfName);
		assertTrue(result.contains("OK"));
		setTestStep("PCPProfileTest.PCPProfileVerify <----");
        logger.info("PCPProfileTest.PCPProfileVerify <----");
	}
}
