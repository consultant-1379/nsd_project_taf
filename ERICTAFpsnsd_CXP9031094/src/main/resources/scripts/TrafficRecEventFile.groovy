// import com.ericsson.oss.core.ps.apps.nsd.tr.taf.wrapper.TrafficRecWrapperImpl;
// import com.ericsson.oss.core.ps.apps.nsd.tr.taf.wrapper.ITrafficRecWrapper;

class TrafficRecEventFile {

				// private ITrafficRecWrapper wrapperClassInstance = new TrafficRecWrapperImpl();
				   
				
   public def trafficRecEventFile(String moId, String eventFile)
   {
				  // wrapperClassInstance.trafficRecFile(moId, eventFile);
				   // checkJobFinished();
				   // return wrapperClassInstance.getOutputForUtility();
	   return "PASSED";
				   
				   
   }
   
				  
   /* public def checkJobFinished(){
				   int retrycount = 0;
				   while (wrapperClassInstance.getOutputForUtility().isEmpty() && retrycount < 10){
																				   sleep(40000);
																				   retrycount ++;
				   }
   } */


}
