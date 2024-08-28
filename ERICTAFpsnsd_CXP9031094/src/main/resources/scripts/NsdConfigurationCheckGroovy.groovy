import com.ericsson.oss.core.ps.nsd.domain.taf.checkNsdConfiguration.ITafVerifyNsdConfiguration;
import com.ericsson.oss.core.ps.nsd.domain.taf.checkNsdConfiguration.TafVerifyNsdConfigurationImpl;
 
class NsdConfigurationCheckGroovy{
	 
	 private ITafVerifyNsdConfiguration wrapperClassInstance = null;
	 
	 public def verifyRequiredOsgiServices(){
		 try{
			 wrapperClassInstance = new TafVerifyNsdConfigurationImpl();
			 return wrapperClassInstance.verifyRequiredOsgiServices();
		 }catch(Exception e){
			 return e;
		 }
	 }
		 
	 /*public def checkJobFinished(){
		 int retrycount = 0;
		 while (wrapperClassInstance.getResult().trim().isEmpty() && retrycount < 10){
				 sleep(40000);
				 retrycount ++;
		 }
	 }*/
 }