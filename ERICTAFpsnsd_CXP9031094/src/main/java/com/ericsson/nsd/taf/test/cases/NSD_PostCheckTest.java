package com.ericsson.nsd.taf.test.cases;


import org.apache.log4j.Logger;
import org.testng.annotations.Test;
import com.ericsson.cifwk.taf.TestCase;
import com.ericsson.cifwk.taf.TorTestCaseHelper;
import com.ericsson.nsd.taf.test.util.HelperUtility;
import com.ericsson.nsd.taf.test.robustness.PreChecker;

public class NSD_PostCheckTest extends TorTestCaseHelper implements TestCase {
	Logger logger = Logger.getLogger(NSD_PostCheckTest.class);

	public String neStop="WCG;DSC;CSCF;SGSN;EPG;UPG;ESAPC;ESASN;IPWORKS;SBG;SpitFire;MSC-S-APG43L R15A;MSC-S-APG R15A;MSC-S-APG43L R17A;BSP 15A-CORE-V5;MSC-S-APG43L R15A;MSC-S-APG43L R16A;MSC-S-APG43L R14B;MSC-S-APG R14B;MSC R18A-APG43L;MSC-S-APG43L R18A;MSC R17A-APG43L;ECM R17;MSC-S-APG43L R18B;MSC-S-CP-APG43L R18B";
	public String stopScript = "triggerStop.sh";
	boolean resultStop = false;
	
	@Test
    public void nodeStartStop (){
    try{
    
    	PreChecker pre  = new PreChecker();
    	
    	boolean resultStop = pre.simulationOperator(neStop,stopScript);
    	if (resultStop== true){
    		logger.info("Nodes stopped successfully");
    	}else{
    		logger.info("Error while stopping nodes");
    	}
    	}
    catch(Exception e){
    logger.info("Exception occured while stopping nodes");
    }
	}
    
	@Test
	public void validatePostChecks() {
		boolean isHeapDump = true;
		boolean isCoreDump = true;
		boolean PostcheckSatisfiedStatus = false;
		logger.info("SD_PostCheckTest.validatePostCheck--->");
		try {
			if (!HelperUtility.checkHeapDump()) {
				//Heap Dump Generated
				isHeapDump = false;
				
			} 
			if(!HelperUtility.checkCoreDump()) {
					// Core dump generated
				isCoreDump = false;
				}
			if(!isHeapDump && !isCoreDump) {
				PostcheckSatisfiedStatus= true;
				}
			
			boolean result = PostcheckSatisfiedStatus;
			logger.info("NSD_PostCheckTest.validatePostChecks <----" + result);
			assertTrue(result);
		} catch (Exception e) {
			logger.error("NSD_PostCheckTest.validatePostChecks Exception Occured ----->" + e);;
		}
	}
}

