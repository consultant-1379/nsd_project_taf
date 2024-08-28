package com.ericsson.nsd.taf.test.operators;

import com.ericsson.cifwk.taf.osgi.client.ApiClient;

public interface ITrafficRecEventFile {
	void initializeSDManager();
	boolean isInitialized();
	void setApiClient(final ApiClient apiClient);
	ApiClient getApiClient();
	String trafficRecEventFile(String nodeName, String logFileType) throws Exception;
}
