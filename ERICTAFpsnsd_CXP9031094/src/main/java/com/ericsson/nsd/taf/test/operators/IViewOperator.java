package com.ericsson.nsd.taf.test.operators;

import java.util.List;

import com.ericsson.cifwk.taf.osgi.client.ApiClient;

public interface IViewOperator {

	 public String checkViewEnabled(final String viewName);
	 /*public boolean prepareNsd() throws ContainerNotReadyException;
	 public boolean registerOSGIRemoteParts();*/
	 public String checkPropertiesViewData(final String nodeType, final String propertiesToFetch);
	 public String checkViewData(final String viewName);
	 public String 	checkPoolAddition(final String PoolName);
	 public String 	checkPoolDeletion(final String PoolName);
	public String executeCommandStatus(final String McName, final String command);
	public String executeCommandoffline(final String MCName, final String commandRef);
	public String executeCommandOnline(final String MCName, final String commandRef);
	public String invokeGroovyMethodOnArgs(final String className, final String method, final String... args);
	public List<String> getNodeNameListFromCS(final String viewName);
	//public String checkAddition(String Moname, String commandRef);
	//public String checkDeletion(String Moname, String commandRef);
	public String executeCommand(final String command);
	/*public void stopApplication();*/
	public String delProfile(final String prfName);
	public String compareProfile(final String prfName1,final String prfName2,final String PlanName);
	public String exportProfile(final String prfName);
	public String importProfile(final String prfName);
	public String creProfile(final String prfName);
	public String updateProfile(final String prfName);
	public String profilePropertyUpdate(String prfName,String mimModel,String moc,String attrName,String attrValue);
	public String profilePropertyDelete(String prfName);
	public void setApiClient(final ApiClient apiClient);
	public String verProfile(final String prfName);
	public ApiClient getApiClient();
	public String synchronizeNode(final String moType);
	
	public String checkTopologyData(String viewName);

}