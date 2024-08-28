//package scripts

import ca.odell.glazedlists.BasicEventList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.beans.factory.FactoryBean

import com.ericsson.oss.ps.nsd.topology.service.ITopologyService;
import com.ericsson.oss.ps.nsd.domain.MO;

class NsdMoTopologyGroovy {
	
	private ITopologyService nsdTopologyService;

	public def getMoList(String nodeType) {
		try {
			List<String> list= new ArrayList<String>();
			final List<MO> sgsnList= new ArrayList<MO>();
			final Object  nodeObj = getTopologyService().findNodesByTypeSync(nodeType);
			sgsnList.addAll((List<MO>) nodeObj);
			
			for(MO obj: sgsnList){
					list.add(obj.getId());
			}
			return list;
		}catch (Exception e){
			e.getMessage().toString();
	    }
	}
	
	public def getMoListSize(String nodeType){
		List<String> list = getMoList(nodeType);
		return list.size().toString();
	}
	
	public def getMoDetails(String nodeType) {
		try {
			String domianMoWithAttr = "";
			final List<MO> moList= new ArrayList<MO>();
			final Object  nodeObj = getTopologyService().findNodesByTypeSync(nodeType);
			moList.addAll((List<MO>) nodeObj);
			domianMoWithAttr = fetchMoWithAttribute(moList);
			return domianMoWithAttr;
		}catch (Exception e){
			return e.getMessage();
		}
	}
	
	private def fetchMoWithAttribute(List<MO> moList)
	{
		String moWithAttr="";
		try{
			for(MO mo: moList){
				if(mo.getAttribute("neMIMName").toString().equals("ECIM_Top") && ((mo.getAttribute("mirrorMIBsynchStatus").toString().trim().equals("SYNCHRONIZED"))))
				{
					moWithAttr = mo.getFdnString()+":"+mo.getId()+":"+mo.getAttribute("ipAddress").toString();
					return moWithAttr;
				}
				
			}
			return moWithAttr;
		}catch (Exception e){
			return e.getMessage();
		}
	}
	
	public def getMoFdnForNodeType(String nodeType) {
			String moFdn = "";
			String nodeTypeToFetch = "";
			final List<MO> moList= new ArrayList<MO>();
			if(nodeType.equals("EPG-SSR") || nodeType.equals("EPG-JUNIPER")){
				nodeTypeToFetch = "EPG";
			}else
			{
				nodeTypeToFetch = nodeType;
			}
			final Object  nodeObj = getTopologyService().findNodesByTypeSync(nodeTypeToFetch);
			moList.addAll((List<MO>) nodeObj);
			moFdn = fetchMoFdn(nodeType, moList);
			return moFdn;
	}
	
	private def fetchMoFdn(String nodeType, List<MO> moList)
	{
		String moFdn="";
		/*try{*/
			for(MO mo: moList){
				if(nodeType.equals("SGSN"))
				{
					if(mo.getAttribute("neMIMName").toString().equals("ECIM_Top") && ((mo.getAttribute("mirrorMIBsynchStatus").toString().trim().equals("SYNCHRONIZED"))))
					{
						moFdn = mo.getFdnString().trim();
						break;
					}
				}else if(nodeType.equals("EPG-SSR"))
				{
					if(mo.getAttribute("neMIMName").toString().equals("ECIM_Top") && ((mo.getAttribute("mirrorMIBsynchStatus").toString().trim().equals("SYNCHRONIZED")) ||
						 (mo.getAttribute("mirrorMIBsynchStatus").toString().trim().equals("DISCOVERED"))))
					{
						moFdn = mo.getFdnString().trim();
						break;
					}
				}else if(nodeType.equals("EPG-JUNIPER"))
				{
					if(mo.getAttribute("neMIMName").toString().equals("EPG_NODE_MODEL") && ((mo.getAttribute("mirrorMIBsynchStatus").toString().trim().equals("DISCOVERED"))))
					{
						moFdn = mo.getFdnString().trim();
						break;
					}
				}else
				{
					if((mo.getAttribute("mirrorMIBsynchStatus").toString().trim().equals("SYNCHRONIZED")) || 
						(mo.getAttribute("mirrorMIBsynchStatus").toString().trim().equals("DISCOVERED")))
					{
						moFdn = mo.getFdnString().trim();
						break;
					}
				}
			}
			return moFdn;
	}
	
	public def getAttributeForNode(MO mo, String attributeName) {
		return mo.getAttribute(attributeName);
	
		
}
	
	public def getMoDetailswithAttributes(String nodeType, String propertiesToFetch) {
		try {
			String domianMoWithAttr = "";
			String nodeTypeToFetch = "";
			if(nodeType.equals("EPG-SSR") || nodeType.equals("EPG-JUNIPER"))
			{
				nodeTypeToFetch = "EPG"; 
			}else{
				nodeTypeToFetch = nodeType;
			}
			final List<MO> moList= new ArrayList<MO>();
			final Object  nodeObj = getTopologyService().findNodesByTypeSync(nodeTypeToFetch);
			moList.addAll((List<MO>) nodeObj);
			domianMoWithAttr = fetchMoWithListofAttribute(nodeType,moList,propertiesToFetch);
			return domianMoWithAttr;
		}catch (Exception e){
			return e.getMessage();
		}
	}
	
	private def fetchMoWithListofAttribute(String nodeType,List<MO> moList, String propertiesToFetch)
	{
		StringBuffer moWithAttr = new StringBuffer();
		String moFdn = "";
		try{
			for(MO mo: moList){
				 if(nodeType.equals("EPG-SSR"))
				{
					if(mo.getAttribute("neMIMName").toString().equals("ECIM_Top"))
					{
						moFdn = mo.getFdnString().trim();
						
					}else{
					continue;
					}
				}else if(nodeType.equals("EPG-JUNIPER"))
				{
					if(mo.getAttribute("neMIMName").toString().equals("EPG_NODE_MODEL"))
					{
						moFdn = mo.getFdnString().trim();
						
					}else{
					continue;
					}
				}else
				{
					moFdn = mo.getFdnString().trim();
				}		
				
						
				final String[] properties = propertiesToFetch.split(":");
				for (String property : properties) {
					
					
					moWithAttr.append(mo.getAttribute(property));
					moWithAttr.append(";");
					
				}
				
				moWithAttr.append(moFdn);
				return moWithAttr;
			}
			
		}catch (Exception e){
			return e.getMessage();
		}
	}
	
	//=======================================================================================================================//
	/*
	* Services
	*/
	
	public def getTopologyService(){
					final String topologyServiceUrl = "rmi://masterservice:50042/EPCTopologyService";
					nsdTopologyService= (ITopologyService) getRmiService(topologyServiceUrl, ITopologyService.class);
					return nsdTopologyService;
	}
					
	private static Object getRmiService(final String url, final Class clazz) {
					final RmiProxyFactoryBean plannedManagementRmiFactory = new RmiProxyFactoryBean();
					plannedManagementRmiFactory.setServiceInterface(clazz);
					plannedManagementRmiFactory.setServiceUrl(url);
					plannedManagementRmiFactory.setRefreshStubOnConnectFailure(true);
					plannedManagementRmiFactory.afterPropertiesSet();
					
					return ((FactoryBean) plannedManagementRmiFactory).getObject();
	}
	

}