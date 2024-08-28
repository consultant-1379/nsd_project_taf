package com.ericsson.nsd.taf.test.operators;

import com.ericsson.cifwk.taf.annotations.Input;
import com.ericsson.cifwk.taf.osgi.client.ApiClient;


/**
 * @author Neeraj
 *
 */

public interface ILogOperator {
	
	void initializeSDManager();
	boolean isInitialized();
	void setApiClient(final ApiClient apiClient);
	ApiClient getApiClient();
	
	String verifyLogFunctionality(String nodeName, String logFileType) throws Exception;
	
}

