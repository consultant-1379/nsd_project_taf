import com.ericsson.oss.core.ps.nsd.logs.taf.ITafLogUtilities;
import com.ericsson.oss.core.ps.nsd.logs.taf.TafLogUtilitiesImpl;

class LogsUtility {

	private ITafLogUtilities wrapperClassInstance = new TafLogUtilitiesImpl();
	   
	
   public def verifyLogFunctionality(String fdnName, String logFileType)
   {
	   wrapperClassInstance.validateLogItems(fdnName, logFileType);
	   checkJobFinished();
	   return wrapperClassInstance.getOutputForUtility();
	   
	   
   }
   
	  
   public def checkJobFinished(){
	   int retrycount = 0;
	   while (wrapperClassInstance.getOutputForUtility().isEmpty() && retrycount < 10){
					   sleep(40000);
					   retrycount ++;
	   }
   }


}
