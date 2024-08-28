package com.ericsson.nsd.taf.test.getters;

import javax.inject.Singleton;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.handlers.RemoteFileHandler;
import com.ericsson.cifwk.taf.tools.cli.handlers.impl.RemoteObjectHandler;
import com.ericsson.cifwk.taf.handlers.implementation.SshRemoteCommandExecutor;
//import com.ericsson.cifwk.taf.handlers.netsim.implementation.SshNetsimHandler;
import com.ericsson.cifwk.taf.osgi.client.ApiClient;
import com.ericsson.cifwk.taf.osgi.client.ApiContainerClient;
import com.ericsson.cifwk.taf.osgi.client.JavaApi;
import com.ericsson.oss.taf.hostconfigurator.HostGroup;

@Singleton
public class NSDMCGetter {
                
                private static ApiClient client;

    private static Host masterHost = HostGroup.getOssmaster();//DataHandler.getHostByName("ossmaster");

    //private static final Host masterRootHost = DataHandler.getHostByName("ossmasterRoot");

    private static Host netsimHost = HostGroup.getAllNetsims().get(0);//DataHandler.getHostByName("netsim");

    //private static final Host infraServerHost = DataHandler.getHostByName("infraServer");
    
    private static final String NSD_CONFIG = DataHandler.getAttribute("cex_config").toString();

    private static final String NSD_SCRIPT = DataHandler.getAttribute("cex_script").toString();

   public static ApiClient getOsgiClient() {

        if (client == null) {
                final String endPoint = ApiContainerClient.constructEndpoint(masterHost, NSD_SCRIPT, NSD_CONFIG);
         /* final String endPoint = "http://" + masterHost.getIp() + ":" + ApiContainerClient.getCLI();
                    + ApiContainerClient.AGENT_URI;*/
            client = JavaApi.createApiClient(endPoint);
                //client=ApiContainerClient.getInstance().
          // //System.out.println(endPoint+" CLIENT IS "+client);
                
        }

        return client;
    }

  /*  public static SshRemoteCommandExecutor getOSSMasterSshRemoteExecutor() {

        return getSshRemoteCommandExecutor(masterHost);
    }

    public static SshRemoteCommandExecutor getNetsimSshRemoteCommandExecutor() {

        return getSshRemoteCommandExecutor(netsimHost);
    }

    public static SshRemoteCommandExecutor getSshRemoteCommandExecutor(final Host host) {

        return new SshRemoteCommandExecutor(host);
    }*/

    public static RemoteObjectHandler getMasterHostFileHandler() {

        return getRemoteObjectHandler(masterHost);
    }

    public static RemoteObjectHandler getNetsimRemoteObjectHandler() {

        return getRemoteObjectHandler(netsimHost);
    }

    public static RemoteObjectHandler getRemoteObjectHandler(final Host host) {

        return new RemoteObjectHandler(host);
    }

  /*  public static SshNetsimHandler getSshNetsimHandler() {

        return getSshNetsimHandler(netsimHost);
    }

    public static SshNetsimHandler getSshNetsimHandler(final Host netsimHost) {

        return new SshNetsimHandler(netsimHost);
    }*/

   

    /*public static RemoteFileHandler getInfrServerFileHandler() {

        return getRemoteFileHandler(infraServerHost);
    }*/

    

    /*public static RemoteFileHandler getMasterRootFileHandler() {

        return getRemoteFileHandler(masterRootHost);
    }*/
public static Host getMasterHost() {
		
		if(masterHost==null)
		{
			masterHost= HostGroup.getOssmaster();/*DataHandler.getHostByName(MASTERHOST);*/
		}
		return masterHost;
	}
	public static Host getNetSimHost(){
		if(netsimHost==null)
		{
			netsimHost= HostGroup.getAllNetsims().get(0);/*DataHandler.getHostByName(NETSIMHOST);*/
		}
		return netsimHost;
	}


}
                

