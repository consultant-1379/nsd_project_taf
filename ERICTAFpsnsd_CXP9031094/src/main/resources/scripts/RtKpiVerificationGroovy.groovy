import com.ericsson.oss.core.ps.apps.nsd.rtkpi.taf.uiwrapper.ITafRtKpiVerification;
import com.ericsson.oss.core.ps.apps.nsd.rtkpi.taf.uiwrapper.TafRtKpiVerificationImpl;

class RtKpiVerificationGroovy {
	
	private ITafRtKpiVerification wrapperClassInstance = null; 
	
	public def verifyStartRtKpi(String nodeType, String nodeFdn, String counterName){
		try{
			wrapperClassInstance = new TafRtKpiVerificationImpl();
			wrapperClassInstance.verifyStartRtKpi(nodeType, nodeFdn, counterName);
			checkJobFinished();
			return wrapperClassInstance.getRopOutput();
		}catch(Exception e){
			return e;
		}
	}
		
	public def checkJobFinished(){
		int retrycount = 0;
		while (wrapperClassInstance.getRopOutput().isEmpty() && retrycount < 10){
				sleep(40000);
				retrycount ++;
		}
	}
}