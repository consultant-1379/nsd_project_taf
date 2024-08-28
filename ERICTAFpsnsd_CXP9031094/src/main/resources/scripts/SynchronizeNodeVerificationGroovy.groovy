import com.ericsson.oss.core.ps.nsd.domain.taf.sync.ITafVerifySynchFunctionality;
import com.ericsson.oss.core.ps.nsd.domain.taf.sync.TafVerifySynchFunctionalityImpl;
 
class SynchronizeNodeVerificationGroovy {
	 
	 private ITafVerifySynchFunctionality wrapperClassInstance = null;
	 
	 public def verifySynchFunctionality(String nodeFdn){
		 try{
			 wrapperClassInstance = new TafVerifySynchFunctionalityImpl();
			 wrapperClassInstance.verifySynchFunctionality(nodeFdn);
			 checkJobFinished();
			 return wrapperClassInstance.getResult().trim();
		 }catch(Exception e){
			 return e;
		 }
	 }
		 
	 public def checkJobFinished(){
		 int retrycount = 0;
		 while (wrapperClassInstance.getResult().trim().isEmpty() && retrycount < 10){
				 sleep(40000);
				 retrycount ++;
		 }
	 }
 }