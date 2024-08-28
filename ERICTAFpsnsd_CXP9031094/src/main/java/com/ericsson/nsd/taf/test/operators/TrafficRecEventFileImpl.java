package com.ericsson.nsd.taf.test.operators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.osgi.client.ApiClient;
import com.ericsson.nsd.taf.test.util.ConfigureServer;
//import com.ericsson.nsd.taf.test.util.ConfigureServer;
import com.ericsson.nsd.taf.test.util.HelperUtility;

@Operator(context = Context.CLI)
public class TrafficRecEventFileImpl implements ITrafficRecEventFile {
	
	private static final Logger logger = Logger.getLogger(TrafficRecEventFileImpl.class);
	private static ApiClient apiClient;
	private boolean isInitialized = false;
	
	@Override
	public void initializeSDManager() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isInitialized() {
		return isInitialized;
	}

	@Override
	public void setApiClient(ApiClient apiClient) {
		this.apiClient = apiClient;
	}

	@Override
	public ApiClient getApiClient() {
		return apiClient;
	}

	@Override
	public String trafficRecEventFile(String nodeType, String eventFile) throws Exception {
		String result = "FAILED";
		try {
			
            logger.info("*************trafficRecEventFile---> "+nodeType );
            final Map<String, String> mapOfAttributes = HelperUtility.getMapofattributes();
			if (mapOfAttributes != null && mapOfAttributes.size() != 0) {

				String moFdn = mapOfAttributes.get("moFdn");
				String moId = mapOfAttributes.get("moId");
				String moIpAddress = mapOfAttributes.get("moIpAddress");
				logger.info("Data passed to the UI Wrapper class  FDN: " + moFdn +" " + moId +" " + eventFile);
				result = invokeGroovyMethodOnArgs("TrafficRecEventFile", "trafficRecEventFile", moId, eventFile);
				logger.info("Result for trafficRecEventFile: " + result);
			}else
            {
            	logger.info("No node is synched .. So we are skipping the testcase");
            }

		} catch (Exception e) {
			logger.error("EXception Occured: " , e);
		}catch(Throwable t)
		{
			logger.error("Throwable exception occured: " , t);
		}
		return result;
	}
	
	
	public String invokeGroovyMethodOnArgs(final String className, final String method, final String... args) {

		String respVal = null;
		logger.info("*********************invokeGroovyMethodOnArgs---> "+className +" " + method + " " +args);
		respVal = apiClient.invoke(className, method, args).getValue();
		return respVal;
	}

}