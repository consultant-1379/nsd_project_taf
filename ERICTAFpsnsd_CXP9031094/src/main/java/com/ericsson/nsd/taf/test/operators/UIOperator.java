package com.ericsson.nsd.taf.test.operators;

import java.util.Map;

import org.testng.collections.Maps;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.data.Ports;
import com.ericsson.cifwk.taf.ui.DesktopNavigator;
import com.ericsson.cifwk.taf.ui.DesktopWindow;
import com.ericsson.cifwk.taf.ui.SwtNavigator;
import com.ericsson.cifwk.taf.ui.UI;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;
import com.ericsson.cifwk.taf.ui.sdk.ViewModel;
import com.ericsson.oss.taf.hostconfigurator.HostGroup;

/*import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.data.Ports;
import com.ericsson.cifwk.taf.ui.DesktopNavigator;
import com.ericsson.cifwk.taf.ui.DesktopWindow;
import com.ericsson.cifwk.taf.ui.SwtNavigator;
import com.ericsson.cifwk.taf.ui.UI;
import com.ericsson.cifwk.taf.ui.sdk.Button;
import com.ericsson.cifwk.taf.ui.sdk.TextBox;
import com.ericsson.cifwk.taf.ui.sdk.ViewModel;*/

@Operator(context = Context.UI)
public class UIOperator implements IUIOperator {

	@Override
	public boolean validadeUILaunchSSHShell() {
		boolean retBol=false;
		System.out.println("inside ui launch");
		try{
			System.out.println("inside ui launch try ");
		//SwtNavigator s=	new SwtNavigator(DataHandler.getHostByName("master"), "/opt/ericsson/nms_cex_client/bin/cex_client","/opt/ericsson/nms_cex_client/bin/cex_client_application.ini","192.168.0.86:0.0",300000L);
			SwtNavigator s=	new SwtNavigator(HostGroup.getOssmaster(), "/opt/ericsson/nms_3_client_010_shell/bin/nsd4epc/nsd4epc","/opt/ericsson/nms_3_client_010_shell/bin/nsd4epc/nsd4epc.ini","192.168.0.86:0.0",300000L);
			System.out.println("****************************"); 
			DesktopNavigator desktopNavigator = UI.newSwtNavigator(HostGroup.getOssmaster());
			System.out.println("****************************22222"); 
		    DesktopWindow window = desktopNavigator.getWindowByTitle("OSS Common Explorer - valid configuration");
		    System.out.println("****************************333333");
	     	ViewModel genericView = window.getGenericView();
	        TextBox nameTextBox = genericView.getTextBox("{label = 'Name:'}");
	        nameTextBox.setText("Name");
	        Button okButton = genericView.getButton("{text = 'OK'}");
	        okButton.click();
		}catch (Exception e){
			System.out.println("inside ui launch catch ");
			e.printStackTrace();
			e.getMessage();
		}
	       /* List<Button> allOkButtons = view.getViewComponents("{text = 'OK'}", Button.class);
	        genericView.getViewComponent(arg0)*/
		     return retBol;
	}
	private Host getRemoteHostX() {
           return HostGroup.getOssmaster();//DataHandler.getHostByName("master");
   }

   private Host getRemoteHost() {
           Host host = new Host();
           host.setIp("atvts1553.athtem.eei.ericsson.se");
           Map<Ports, String> map = Maps.newHashMap();
           map.put(Ports.HTTP, "10001");
           host.setPort(map);
           return host;
   }
}
