package com.ericsson.nsd.taf.test.cases;


import java.util.concurrent.TimeUnit;

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
import com.ericsson.nsd.taf.test.robustness.PreChecker;
import com.ericsson.nsd.taf.test.util.HelperUtility;
import com.ericsson.oss.taf.cshandler.CSHandler;

import org.apache.log4j.Logger;

public class StartUpLogsOnMCRestart extends TorTestCaseHelper implements TestCase
{
	Logger logger = Logger.getLogger(SGSNviewTesting.class);
	private OperatorRegistry<IViewOperator> viewOperator;
	
	@SuppressWarnings("deprecation")
	@Context(context = { Context.CLI })
	@Test(groups = { "KGB" })
//	@DataDriven(name = "StatusViewTest")
	@TestId(title = "To verify NSD start_up logs do not override ")
	public void checkStartUpLogs()
	{
		String command = "ls -lrt /var/opt/ericsson/nms_psnsd/ | grep -i start";
		String date = "date";
	//	NSDMC_CDB_FunctionalTest mcrestart = new NSDMC_CDB_FunctionalTest();
	    String mcRestart = "/opt/ericsson/nms_cif_sm/bin/smtool -coldrestart oss_nsd_ps -reason=upgrade -reasontext=\"new pkg\" ";
	    String restart= HelperUtility.executeCommand(mcRestart);
		String currDate = HelperUtility.executeCommand(date);
		logger.info("MC restarted at the server ===> "+currDate);
	//	String file1 = startLogs.split("\n")[0];
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String startLogs = HelperUtility.executeCommand(command);
		String file2 = startLogs.split("\n")[1];
	//	logger.info("first file =====>> " +file1);
		logger.info("second file =====>> "+file2);
		int index = currDate.lastIndexOf(":");
		String timeOnServer = currDate.substring(index-5, index);
		int index1 = file2.indexOf(":");
		String timeOfFileCreated = file2.substring(index1-2, index1+3);
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertEquals(timeOnServer,timeOfFileCreated);
	}
	
	@SuppressWarnings("deprecation")
	@Context(context = { Context.CLI })
	@Test(groups = { "KGB" })
	@TestId(title = "To check PAS values ")
	public void pASParameterValue()
	{
		String pASCommand = "cd /opt/ericsson/nms_cif_pas/bin/ && ./pastool -get \"com.ericsson.oss.ps.nsd\"";
		String pasValue = HelperUtility.executeCommand(pASCommand);
		logger.info("The value of PAS variables ===> " +pasValue);
		assertTrue(true);
	}
}