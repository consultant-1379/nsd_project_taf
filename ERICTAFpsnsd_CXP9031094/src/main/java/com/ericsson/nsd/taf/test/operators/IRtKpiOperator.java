/**
 * 
 */
package com.ericsson.nsd.taf.test.operators;

import com.ericsson.cifwk.taf.osgi.client.ApiClient;

/**
 * @author xchashr
 *
 */
public interface IRtKpiOperator {

	String verifyStartRtkpi(final String nodeType, final String counterName);
	
	String verifyStopRtKpi(final String moFdn);
	
	void setApiClient(final ApiClient apiClient);
	
	ApiClient getApiClient();
	
	
}
