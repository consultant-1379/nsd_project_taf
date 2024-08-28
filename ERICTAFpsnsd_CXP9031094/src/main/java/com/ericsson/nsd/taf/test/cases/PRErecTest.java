package com.ericsson.nsd.taf.test.cases;



import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.Test;

import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.cifwk.taf.annotations.TestId;
import com.ericsson.cifwk.taf.osgi.client.ContainerNotReadyException;
import com.ericsson.nsd.taf.test.constants.StaticConstants;
import com.ericsson.nsd.taf.test.operators.AlarmOperatorImpl;
import com.ericsson.nsd.taf.test.operators.IAlarmOperator;
import com.ericsson.nsd.taf.test.operators.ILogOperator;
import com.ericsson.nsd.taf.test.operators.IRtKpiOperator;
import com.ericsson.nsd.taf.test.operators.ITrafficRecEventFile;
import com.ericsson.nsd.taf.test.operators.IViewOperator;
import com.ericsson.nsd.taf.test.operators.PsNsdOperator;
import com.ericsson.nsd.taf.test.operators.RtKpiOperatorImpl;
import com.ericsson.nsd.taf.test.operators.TrafficRecEventFileImpl;
import com.ericsson.nsd.taf.test.operators.ViewOperatorImpl;
import com.ericsson.nsd.taf.test.operators.LogOperatorImpl;
import com.ericsson.nsd.taf.test.operators.ILogOperator;
import com.ericsson.nsd.taf.test.robustness.PostGuiLaunchCheck;
import com.ericsson.nsd.taf.test.util.HelperUtility;
import com.ericsson.nsd.taf.test.util.SavePluginsFromNexusToServerUtility;
import com.ericsson.oss.taf.hostconfigurator.HostGroup;


public class PRErecTest extends TorTestCaseHelper implements TestCase{

	private static final Logger log = Logger.getLogger(PRErecTest.class);
	
	private static PsNsdOperator psNsdOperator;
	
	private static PRErecTest instance;
	private static Object lock = new Object();
	private static int counter = 1;
	
	public static PRErecTest getInstance() throws Exception {

		if (instance == null) {
			synchronized (lock) {
				if (instance == null) {
					instance = new PRErecTest();
				}
			}
		}

		return instance;
	}
	
	//This has been updated as a part of TR HT99869.
	@TestId(id = "OSS-78771_Func_1", title = "Verify Client Launch")
	@Test
	public void launchClient()  {
		log.info("PRErecTest.launchClient ---->");
		try {
		 	log.info("PRErecTest.launchClient: Going to copy the plugins from the NEXUS to the cloud server -------> ");
		 	SavePluginsFromNexusToServerUtility.copyPluginsToServer();
			log.error("PRErecTest.launchClient: copyIniFilesToLaunchGuiWithJava7 -----> ");
			HelperUtility.copyIniFilesToLaunchGuiWithJava7(StaticConstants.NSD_CONFIG_INI_FILE_NAME_JDK7);
			log.error("PRErecTest.launchClient: copyIniFilesToLaunchGuiWithJava7 <----- ");
			TimeUnit.MINUTES.sleep(10);
			callingCexToUpdatePort();
			
		} catch (Exception e) {
			log.error("Exception occured in PRErecTest.launchClient " , e);
		} catch (Throwable e) {
			log.error("Throwable occured in PRErecTest.launchClient " , e);
		}
	}
	
	private void callingCexToUpdatePort() {
		try {
			psNsdOperator = new PsNsdOperator(HostGroup.getOssmaster());
			psNsdOperator.prepareCex();
			psNsdOperator.setClient(psNsdOperator.getOsgiClient());
			//Intentionally done the same
			final IViewOperator operator = new ViewOperatorImpl();
			operator.setApiClient(psNsdOperator.getOsgiClient());
			
			final IRtKpiOperator rtKpiOperator = new RtKpiOperatorImpl();
			rtKpiOperator.setApiClient(psNsdOperator.getOsgiClient());
			
			final ITrafficRecEventFile eventFile = new TrafficRecEventFileImpl();
			eventFile.setApiClient(psNsdOperator.getOsgiClient());
		
			final ILogOperator logOperator = new LogOperatorImpl();
			logOperator.setApiClient(psNsdOperator.getOsgiClient());
			
			final IAlarmOperator alarmOperator = new AlarmOperatorImpl();
			alarmOperator.setApiClient(psNsdOperator.getOsgiClient());
			
			setTestStep("PRErecTest.launchClient <----");
			log.info("PRErecTest.launchClient <----");
		} catch(Exception e) {
			log.error("Exception occured in updatingFile " + counter + " time");
			if(counter <= 5) {
				counter++;
				try {				
					log.info("Going to sleep for 2 minute for unlocking ini file--->");
				TimeUnit.MINUTES.sleep(2);
				log.info("Come back from sleep <---");
				} catch(Exception e1) {
					log.error("Exception occured while sleep",e1);
				}
				callingCexToUpdatePort();
			} else {
				log.error("Container not Ready Exception occured in PRErecTest.launchClient " , e);
				e.printStackTrace();
			}
		} catch (Throwable e) {
			log.error("Throwable occured in PRErecTest.callingCexToUpdatePort " , e);
			e.printStackTrace();
		}
	}
	
	
	
	
	@AfterSuite
	@TestId(id = "OSS-78774_Func_1", title = "Verify Client close")
	public void closeClient()  {

		try {
			log.info("PRErecTest.closeClient ---->");
			setTestStep("PRErecTest.closeClient ---->");
			psNsdOperator.stopApplication();
			SavePluginsFromNexusToServerUtility.removePluginsFromTheServer();
			//This has been introduced as a part of TR HT99869.
			HelperUtility.copyIniFilesToLaunchGuiWithJava7(StaticConstants.NSD_CONFIG_INI_FILE_NAME_ORIGINAL);
			setTestStep("PRErecTest.closeClient <----");
			log.info("PRErecTest.closeClient <----");
		} catch (Exception e) {
			log.error("Exception occurred in PRErecTest.closeClient while closing the application!!! " , e);
		}
	}

	
}
