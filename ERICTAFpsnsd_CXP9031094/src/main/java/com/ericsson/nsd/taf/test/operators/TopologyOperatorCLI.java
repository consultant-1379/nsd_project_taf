package com.ericsson.nsd.taf.test.operators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Singleton;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;


@Operator(context = Context.CLI)
@Singleton
public class TopologyOperatorCLI implements ITopologyOperator{
	
	private static final Logger log = Logger.getLogger(TopologyOperatorCLI.class);
	
	private static final IViewOperator viewOperator = new ViewOperatorImpl();
	/*public static final String SGSNListInCS="SgsnListInCS";
    public static final String GGSNListInCS="GgsnListInCS";
    public static final String CPGListInCS = "CPGListInCS";
	public static final String MSCListInCS = "MSCListInCS";
	public static final String PSPOOLListInCS = "PSPOOLListInCS";
	public static final String EPGListInCS = "EPGListInCS";
	public static final String APNListInCS = "ApnListInCS";
	public static final String TAListInCS = "TAListInCS";
	public static final String MSCPOOLListInCS = "MSCPOOLListInCS";
	public static final String BscListInCS = "BscListInCS";
	public static final String RncListInCS = "RncListInCS";
	public static final String SgsnBoardListInCS = "SgsnBoardListInCS";
	public static final String GgsnBoardListInCS = "GgsnBoardListInCS";
	public static final String MscMOListInCS = "MscMOListInCS";
	public static final String SASNMOListInCS = "SasnMOListInCS";
	public static final String DSCMOListInCS = "DscMOListInCS";
	public static final String DNSListInCS = "DNSListInCS";*/

	
private static ITopologyOperator topologyOperator;
	
	private static Object lock = new Object();

	public static ITopologyOperator getInstance() throws Exception {
		if (topologyOperator == null) {
			synchronized (lock) {
				if (topologyOperator == null) {
					topologyOperator = new TopologyOperatorCLI();
				}
			}
		}

		return topologyOperator;
	}

	@Override
	public String checkViewData(String viewName) {
		System.out.println("Mo name is $ "+viewName+" $");	
		try {
		String csInput=null;
                       if(viewName.equalsIgnoreCase("PsPool")){
   	   	   	       csInput="PSPOOL";
                               }
                       else{
  	                         csInput=viewName;
                           }

			List<String> nodeNameList= new ArrayList<String>();
		String testResult;
		
			testResult = viewOperator.invokeGroovyMethodOnArgs("NsdMoTopologyGroovy", "getMoList",viewName);
			if(!testResult.equals("[]")){
				
				final String[] nodeNames=testResult.substring(1, testResult.length()-1).split(",");
				nodeNameList=Arrays.asList(nodeNames);	
			}
			System.out.println("TOPOLOGYOPRATORCLI: From Groovy list is  :"+nodeNameList+ " and size is "+nodeNameList.size());

			List<String> listOfFDN = viewOperator.getNodeNameListFromCS(csInput);
			if(viewName.equalsIgnoreCase("PsPool")){
				listOfFDN.remove("-1");
			}
			System.out.println("TOPOLOGYOPRATORCLI: From CS list is :"+listOfFDN+" and size is "+listOfFDN.size());
			if(nodeNameList.isEmpty() && listOfFDN.isEmpty()){
					return "OK";
				}
			else if (nodeNameList.size() > 0 && listOfFDN.size() > 0) {
				if(nodeNameList.size() == listOfFDN.size()){
					if(!listOfFDN.removeAll(nodeNameList)){	

						return "FAIL";

					}
				}else{
					return "FAIL";
				}
				return "OK";
			}
			else if(nodeNameList.size() == listOfFDN.size()){
				return "OK";
			}
			return "FAIL";		
			
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return "FAIL";
		}
			
		/*}else {
			return "FAIL";
		}*/
	
	}
	
	

}

