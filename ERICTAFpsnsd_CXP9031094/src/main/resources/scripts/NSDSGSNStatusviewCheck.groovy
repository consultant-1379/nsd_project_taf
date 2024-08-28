import ca.odell.glazedlists.BasicEventList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.beans.factory.FactoryBean
import com.ericsson.oss.ps.nsd.status.service.IStatusManager;
import com.ericsson.oss.ps.nsd.domain.MO;
import com.ericsson.oss.ps.nsd.status.service.MoStatus;
 
class NSDSGSNStatusviewCheck {

	
	private IStatusManager nsdStatusService;

	public def getMoList(String nodeType) {
		try {
			List<String> list= new ArrayList<String>();
			final List<MoStatus> sgsnList= new ArrayList<MO>();
			final Object  nodeObj = getTopologyService().findNodesByTypeSync(nodeType);
			sgsnList.addAll((List<MoStatus>) nodeObj);
			
			for(MoStatus obj: sgsnList){
					list.add(obj.getId());
			}
			return list;
		}catch (Exception e){
			e.getMessage().toString();
		}
	}

	
	public def getTopologyService(){
		final String topologyServiceUrl = "rmi://masterservice:50042/EPCStatusService";
		nsdStatusService= (IStatusManager) getRmiService(topologyServiceUrl, IStatusManager.class);
		return nsdStatusService;
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