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
import com.ericsson.nsd.taf.test.util.HelperUtility;


@Operator(context = Context.CLI)
public class LogOperatorImpl implements ILogOperator {
	
	private static final Logger logger = Logger.getLogger(LogOperatorImpl.class);
	private static ApiClient apiClient;
	private boolean isInitialized = false;
	
	@Override
	public void initializeSDManager() {
		
		
	}
	
	@Override
	public boolean isInitialized() {
		return isInitialized;
	}

	
	
	public void setApiClient(final ApiClient apiClient) {
		this.apiClient = apiClient;
		
	}
	
	public ApiClient getApiClient() {
		return apiClient;
	}

	
	@Override
	public String verifyLogFunctionality(final String nodeType, final String logFileType) throws Exception {
		String result = "FAILED";
		try {
			
			logger.info("*********************verifyLogFunctionality---> "+nodeType );
			final Map<String, String> mapOfAttributes = HelperUtility.getMapofattributes();
			
			if(mapOfAttributes != null && mapOfAttributes.size()!=0){
					
			String moFdn = mapOfAttributes.get("moFdn");
			String moId = mapOfAttributes.get("moId");
			String moIpAddress = mapOfAttributes.get("moIpAddress");
			
			
			
			
			
			logger.info("Data passed to the UI Wrapper class: " + moFdn + " : "+moId+" : "+moIpAddress);
			logger.info("Data passed to the UI Wrapper class  FDN: " + moFdn + logFileType);
			
			
			result = invokeGroovyMethodOnArgs("LogsUtility", "verifyLogFunctionality",moFdn, logFileType);
			
		//	result="OK";
			logger.info("Result for verifyLogFunctionality: " + result);
			
			//return result;
			}else
			{
				logger.info("No node is synched .. So we are skipping the testcase");
			}
				
		} catch (Exception e) {
			logger.error("EXception Occured: " , e);
			e.printStackTrace();
		}catch(Throwable t)
		{
			logger.error("Throwable exception occured: " , t);
			t.printStackTrace();
		}
		return result;
	}
	
	
	
	public String invokeGroovyMethodOnArgs(final String className, final String method, final String... args) {

		String respVal = null;
		logger.info("*********************invokeGroovyMethodOnArgs---> ");
		respVal = apiClient.invoke(className, method, args).getValue();
		return respVal;
	}

}
