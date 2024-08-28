import ca.odell.glazedlists.BasicEventList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.beans.factory.FactoryBean

import com.ericsson.oss.ps.nsd.topology.service.ITopologyService;
import com.ericsson.oss.ps.nsd.domain.MO;
//import com.ericsson.oss.ps.nsd.status.service.MoStatus;
 
class TopologyviewCheck {

	private ITopologyService nsdTopologyService;
	

	public def getMoList(String nodeType) {
		try {
			StringBuilder buffer = new StringBuilder();
			final List<MO> sgsnList= new ArrayList<MO>();
			final Object  nodeObj = getTopologyService().findNodesByTypeSync(nodeType);
			sgsnList.addAll((List<MO>) nodeObj);
			for(MO obj: sgsnList){
					buffer.append(";"+obj.getFdnString());
			}
			return buffer;
		}catch (Exception e){
			e.getMessage().toString();
		}
	}

	
	public def getTopologyService(){
		final String topologyServiceUrl = "rmi://masterservice:50042/EPCTopologyService";
		nsdTopologyService= (ITopologyService) getRmiService(topologyServiceUrl, ITopologyService.class);
		//buffer.append("Got topologyService: " + nsdTopologyService + "\n");
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