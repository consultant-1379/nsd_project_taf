/**
 * 
 */
package com.ericsson.nsd.taf.test.operators;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.osgi.client.ApiClient;

/**
 * @author xchashr
 *
 */
@Operator(context = Context.CLI)
public class RtKpiOperatorImpl implements IRtKpiOperator {
	
	private static final Logger logger = Logger.getLogger(RtKpiOperatorImpl.class);
	
	private static ApiClient apiClient;
	
	public void setApiClient(final ApiClient apiClient) {
		this.apiClient = apiClient;
		
	}
	
	public ApiClient getApiClient() {
		return apiClient;
	}

	/* (non-Javadoc)
	 * @see com.ericsson.nsd.taf.test.operators.IRtKpiOperator#verifyStartRtkpi()
	 */
	@Override
	public String verifyStartRtkpi(final String nodeType, final String counterName) {
		String responseFromGroovy = "FAILED";
		try {
			final Map<String, String> moDetailsMap = createDomainMo(nodeType);
			logger.error("MoDetails to be passes to wrapper class: " + moDetailsMap);
			responseFromGroovy = invokeGroovyMethodOnArgs("RtKpiVerificationGroovy", "verifyStartRtKpi", nodeType, 
					moDetailsMap.get("moFdn"), counterName);
			logger.error("Response received for the startKpi: " + responseFromGroovy);
		} catch (Exception e) {
			logger.error("Exception occured in RtKpiOperatorImpl.verifyStartRtkpi()---> ", e);
		}
		
		
		return responseFromGroovy;
	}

	/* (non-Javadoc)
	 * @see com.ericsson.nsd.taf.test.operators.IRtKpiOperator#verifyStopRtKpi()
	 */
	@Override
	public String verifyStopRtKpi(final String moFdn) {
		// TODO Auto-generated method stub
		return "";
	}
	
	private Map<String, String> createDomainMo(final String moType) throws Exception{
		final Map<String, String> mapOfAttributes = new HashMap<>();
		/**
		 * The moDetails contains the Attributes in the form of:
		 * MO_FDN:MO_ID:MO_IP_ADDRESS
		 */
		String moDetails=invokeGroovyMethodOnArgs("NsdMoTopologyGroovy", "getMoDetails", moType);
		logger.info("moDetails retrieved from grrovy---> " + moDetails);
		if(!moDetails.trim().isEmpty() && moDetails.contains(":"))
		{
			String[] moAttributes = moDetails.split(":");
			if(moAttributes.length != 3)
			{
				throw new Exception("The MO Details fetched from the Groovy is not proper...");
			}
			mapOfAttributes.put("moFdn", moAttributes[0]);
			mapOfAttributes.put("moId", moAttributes[1]);
			mapOfAttributes.put("moIpAddress", moAttributes[2]);
		}
		return mapOfAttributes;
	}
	
	private String invokeGroovyMethodOnArgs(final String className, final String method, final String... args) {

		String respVal = null;
		respVal = apiClient.invoke(className, method, args).getValue();
		//System.out.println("respVal : "+respVal);
		return respVal;
	}

}
