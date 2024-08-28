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
import com.ericsson.cifwk.taf.guice.OperatorRegistry;
import com.ericsson.nsd.taf.test.operators.ITrafficRecEventFile;


public class TrafficRecordingEventFileTest extends TorTestCaseHelper implements TestCase {

	Logger logger = Logger.getLogger(TrafficRecordingEventFileTest.class);
	
	@Inject
	private OperatorRegistry<ITrafficRecEventFile> registry;
	
	@Context(context = {Context.CLI})
	@Test(groups={"KGB"})
	@DataDriven(name = "TRAFFIC_REC_EVENT_FILE")
	public void eventFileVerify(@Input("nodeType") String nodeType,@Input("logFileType") String logFileType, @Output("expectedOut") String expectedOut) throws Exception{
		
		try {
			logger.info("TrafficRecordingEventFileTest.eventFileVerify ---->"+nodeType + " " + logFileType + " " + expectedOut);
			ITrafficRecEventFile eventFile = registry.provide(ITrafficRecEventFile.class);
			String result = eventFile.trafficRecEventFile(nodeType, logFileType);
			logger.info("TrafficRecordingEventFileTest ---->" + result);
			assertTrue(result.contains("PASSED"));
			logger.info("TrafficRecordingEventFileTest.eventFileVerify <----");
		} catch (Exception e) {
			logger.error("Exception while running TC "+e);
		} 
	}

}