package com.ericsson.nsd.taf.test.operators;

import com.ericsson.cifwk.taf.osgi.client.ApiClient;

public interface IAlarmOperator {
	
	void setApiClient(final ApiClient apiClient);
	
	ApiClient getApiClient();
	
	String verifyAlarmView(final String nodeType);

}
