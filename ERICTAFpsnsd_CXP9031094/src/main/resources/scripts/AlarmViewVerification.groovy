import java.util.Collection;

import ca.odell.glazedlists.BasicEventList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.beans.factory.FactoryBean;

import com.ericsson.oss.ps.nsd.domain.Alarm;
import com.ericsson.oss.ps.nsd.alarms.service.IAlarmService;

class AlarmViewVerification {
	
	private IAlarmService nsdAlarmService;
	
	public def getAlarmCountforFdn(String moFdn){
	
		try{
			Collection<Alarm> alarmList = new ArrayList<Alarm>();
			alarmList = getAlarmService().getAlarmListSynch(moFdn);
			return alarmList.size().toString();
			
		}catch (Exception e){
		e.getMessage().toString();
		}
	}
	
	//=======================================================================================================================//
	/*
	* Services
	*/
	
	public def getAlarmService(){
					final String alarmServiceUrl = "rmi://masterservice:50042/EPCAlarmService";
					nsdAlarmService= (IAlarmService) getRmiService(alarmServiceUrl, IAlarmService.class);
					return nsdAlarmService;
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