package com.ericsson.nsd.taf.test.operators;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.osgi.client.ApiClient;
import com.ericsson.nsd.taf.test.constants.StaticConstants;
import com.ericsson.nsd.taf.test.util.HelperUtility;

@Operator(context = Context.CLI)
public class AlarmOperatorImpl implements IAlarmOperator {

	private static final Logger logger = Logger
			.getLogger(AlarmOperatorImpl.class);

	private static ApiClient apiClient;

	public void setApiClient(final ApiClient apiClient) {
		this.apiClient = apiClient;

	}

	public ApiClient getApiClient() {
		return apiClient;
	}

	@Override
	public String verifyAlarmView(String nodeType) {
		String responseFromGroovy = "FAILED";
		int alarmCountfromSybase = 0;
		int alarmCountfromGroovy = 0;
		String response = "";
		try {
			logger.info("AlarmViewOperatorImpl.verifyAlarmView --->");
			final Map<String, String> moDetails = HelperUtility
					.createDomainMo(nodeType);
			logger.info("MoDetails pass to Wrapper Class : " + moDetails);
			String moId = moDetails.get("moId");
			String moFdn = moDetails.get("moFdn");
			List<String> sourceFileList = HelperUtility.getAnyFileFilePath(
					StaticConstants.INPUT_QUERY_FILE,
					StaticConstants.MISCELLENOUS_DIRECTORY_PATH);
			String sourceFile = sourceFileList.get(0);
			logger.info("AlarmViewOperatorImpl.verifyAlarmView. source file name --->"
					+ sourceFile);
			writeContentToFile(sourceFile,moId);
			logger.info("Going to copy query file from "+sourceFile+" location to "+StaticConstants.QUERY_FILE_DESTINATION+" ---->");
			HelperUtility.copyFilesFromLocalToRemoteServer(sourceFile, StaticConstants.QUERY_FILE_DESTINATION);
			logger.info("Query file  copied from "+sourceFile+" location to "+StaticConstants.QUERY_FILE_DESTINATION+" <----");
			alarmCountfromSybase = alarmCountfromSybase(StaticConstants.QUERY, StaticConstants.QUERY_FILE_DESTINATION);
			logger.info("alarm count from Sybase --->"+alarmCountfromSybase);
			clearContentfromFile(sourceFile);
			response = HelperUtility.invokeGroovyMethodOnArgs(apiClient, "AlarmViewVerification", "getAlarmCountforFdn", moFdn);
			logger.info("Response from Groovy ---"+response);
			alarmCountfromGroovy = Integer.parseInt(response);
			if(alarmCountfromSybase == alarmCountfromGroovy){
				responseFromGroovy = "PASSED";
				logger.info("AlarmViewOperatorImpl.verifyAlarmView <---");
			}
			else{
				logger.error("AlarmViewOperatorImpl.verifyAlarmView... Alarm not match <---");
			}
			return responseFromGroovy;
			
		} catch (Exception e) {
			logger.error(
					"Exception occured in AlarmViewOperatorImpl.verifyAlarmView -->",
					e);
		}

		return responseFromGroovy;
	}

	private void writeContentToFile(String sourceFile, String moId) {
		String content = "select count(distinct(Alarm_id)) from FMA_alarm_list where Rec_count=0 and Object_of_reference='SubNetwork=ONRM_ROOT_MO,SubNetwork=SGSN,ManagedElement="+moId+"'";
		try {
			File file = new File(sourceFile);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.newLine();
			bw.write("go");
			bw.close();
			
		} catch (IOException e) {

			logger.error("Exception occur while writting to sourcefile in AlarmViewOperatorImpl", e);
		}

	}
	
	private int alarmCountfromSybase(String fileName, String fileLocation){
		int count = 0;
		
		String query = StaticConstants.QUERY+StaticConstants.SPACE+StaticConstants.QUERY_FILE_DESTINATION+StaticConstants.DELIMITER_BACK_SLASH+StaticConstants.INPUT_QUERY_FILE;
		List<String> commandList = new ArrayList<String>();
		commandList.add("tcsh");
		commandList.add(query);
		Map<String,String> response = HelperUtility.executeBulkCommands(commandList);
		//Set<String> keySet = response.keySet();
		//logger.info("Key set size--->"+keySet.size());
	/*	for(String key : keySet){
			logger.info("Key -->"+key);
			logger.info("value --->"+response.get(key));
			if(key.contains(query)){*/
				 String[] res = response.get(query).split(StaticConstants.NEW_LINE);
				if(res != null && res.length > 0){
				int size = res.length;
				logger.info("Size -->"+size);
				for(int i=0;i<size;i++){
					logger.info("Res "+i+"-->"+res[i]);
				}
				
				count = Integer.parseInt(res[3].trim());
				}
			/*}
			
		}*/
		logger.info("Alarm ount from sybase --->"+count);
		
		return count;
	}
	
	private void clearContentfromFile(String sourceFile){
		try{
			File file = new File(sourceFile);
			FileOutputStream fos = new FileOutputStream(file);
			fos.write((new String()).getBytes());
			fos.close();
		}catch(Exception e){
			logger.error("Exception occured while removing content of Query file",e);
		}
	}
}
