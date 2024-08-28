
/*import java.util.ArrayList;
import java.util.List;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import org.springframework.beans.factory.FactoryBean
import com.ericsson.oss.common.cm.service.CMException;
import com.ericsson.oss.common.cm.service.ICMService;
import com.ericsson.oss.cp.transport.activemq.messagejobs.MessageJobInfo;
import com.ericsson.oss.cp.core.progress.JobInfo.JobState;
import com.ericsson.oss.ps.nsd.domain.MO;
import com.ericsson.oss.nsd.alarms.service.impl.AlarmServiceFactory;*/

class NSDFM {/*
	
	MO mo;
	MessageJobInfo job;
	
	public def checkFMAlarm(String fdn){
		
		try {
			mo = new MO(fdn);
			job = AlarmServiceFactory.getInstance().getAlarmList(mo,false);
			checkJobFinished(job);
			if (job.getState() == JobState.FAILED){
			return job.getState().toString();
			
			}
		}
		catch(Exception e){
			return e.getMessage().toString();
		}
	}
	
	public def checkJobFinished(final MessageJobInfo job){
		int retrycount = 0;
		while (!job.isCompleted() && retrycount < 10){
			sleep(1000);
			retrycount ++;
		}
	}

*/}
