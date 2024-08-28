import com.ericsson.oss.core.ps.topology.ui.core.manager.impl.TopologyManager;
import com.ericsson.oss.core.ps.topology.ui.TopologyView;
import com.ericsson.oss.core.ps.topology.ui.core.treelist.Topology;
 
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.BasicEventList;
 
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
 
import com.ericsson.oss.ps.nsd.domain.MO;
import com.ericsson.oss.ps.nsd.status.service.MoStatus;
import com.ericsson.oss.core.ps.nsd.topology.model.TopologyCacheManager;
import com.ericsson.oss.core.ps.nsd.status.service.cacheManager.StatusUICacheManager;
import com.ericsson.oss.ps.nsd.status.service.MoStatus;
import com.ericsson.oss.core.ps.nsd.topology.model.TopologyCacheManager;
import com.ericsson.oss.platform.client.core.osgi.EricssonUIActivator;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;
import org.osgi.framework.BundleContext;
import com.ericsson.oss.core.ps.nsd.topology.model.ITopologyCacheManager;
 
class NSDSGSNviewCheck {

	public def checkSGSNView(String viewName) {
		try {
			List<String> moList= new ArrayList<String>();
			StatusUICacheManager.setIsCacheInitialized(true);
			BasicEventList<MoStatus> sgsnList=StatusUICacheManager.getInstance().getCachedMOStatus(viewName);
			for(MoStatus obj: sgsnList){
				moList.add(obj.getId());
			}

			return moList;
		}
		catch (Exception e){
			e.getMessage().toString();
		}
		/*List<String> moList= new ArrayList<String>();
		 moList.add("SGSN01")
		 moList.add("SGSN02")
		 moList.add("SGSN03")
		 return moList;*/
	}

	public def checkTopologySGSNView(String viewname) {
		try {
			List<String> moList= new ArrayList<String>();
			BundleContext bundleContext =
					FrameworkUtil.getBundle(ITopologyCacheManager.class).getBundleContext();
			println("********************************* : "+bundleContext)
			final ServiceTracker tracker = new ServiceTracker(bundleContext, ITopologyCacheManager.class.getName(), null);
			tracker.open();
			tracker.waitForService(theTimeout);

			final TopologyCacheManager topocache =(ITopologyCacheManager)tracker.getService();
			List<MO> sgsnList=topocache.getMOsByType(viewname)
			for(MO obj: sgsnList){
				moList.add(obj.getId());
			}

			return moList;
		}
		catch (Exception e){
			e.getMessage().toString();
		}
		/*List<String> moList= new ArrayList<String>();
		 moList.add("SGSN01")
		 moList.add("SGSN02")
		 moList.add("SGSN03")
		 return moList;*/
	}

}