package com.ericsson.nsd.taf.test.util;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.handlers.netsim.CommandOutput;
import com.ericsson.cifwk.taf.handlers.netsim.NetSimCommandHandler;
import com.ericsson.cifwk.taf.handlers.netsim.NetSimResult;
import com.ericsson.cifwk.taf.handlers.netsim.NetSimSession;
import com.ericsson.cifwk.taf.handlers.netsim.commands.NetSimCommands;
import com.ericsson.cifwk.taf.handlers.netsim.domain.NeGroup;
import com.ericsson.cifwk.taf.handlers.netsim.domain.NetworkElement;
import com.ericsson.oss.taf.hostconfigurator.HostGroup;


public class ConfigureServer {
	
    private static Logger logger = Logger.getLogger(ConfigureServer.class);
    //private static Host masterHost;
    private static Host netsimHost;
    private static final String NETSIMHOST = "netsim";
    //private static final String MASTERHOST = "ossmaster";
    private static NeGroup allNEs=null;
    private static NetSimCommandHandler service=null;
    private static Map<String, String> mapOfAttributes= null;
    
    
    public static Map<String, String> getMapofattributes() {
                    return mapOfAttributes;
    }
    
    
    
    
    /*private static Host getHost(){
                    if(masterHost == null){
                                    masterHost = DataHandler.getHostByName(MASTERHOST);
                    }
                    return masterHost;
    }*/
    
    private static Host getNetsimHost(){
                    if(netsimHost == null){
                                    netsimHost = HostGroup.getAllNetsims().get(0);//DataHandler.getHostByName(NETSIMHOST);
                    }
                    return netsimHost;
    }
    private static String getSimulation(Host netsimHost,String nodeName){
                                   
                                    NeGroup allNEs = getAllNeName(netsimHost);
                                    NetworkElement simulations = allNEs.get(nodeName);
                                    String simulationName = simulations.getSimulationName();
                                    return simulationName;
    }
    
    private static NeGroup getAllNeName(Host netsimHost)
                    {
                                    if(allNEs==null)
                                    {
                                    NetsimCommandHandler( netsimHost);
                                    allNEs=service.getAllNEs();
                                    }
                                    return allNEs;
                    }
    
     private  static NetSimCommandHandler NetsimCommandHandler(Host Netsimhost){
                                    if(service==null)
                                    {
                                    service =NetSimCommandHandler.getInstance(Netsimhost);
                                    }
                                    return service;
                    }

    public static void startLogsOnServer(){
                    try{ 
                                    logger.info("NetSimPipeCommandHandler. startLogsOnServer ---->");
                    mapOfAttributes = HelperUtility.createDomainMo("SGSN");
                    logger.info("MapOfAttributes : "+mapOfAttributes);
                    String nodeName = mapOfAttributes.get("moId");
                    logger.info("MoId : "+ nodeName);
                      netsimHost = getNetsimHost();
                      logger.info("nesimHost : "+ netsimHost);
                    String simulationName = getSimulation(netsimHost, nodeName);
                    NetSimSession session = NetSimCommandHandler.getSession(netsimHost);                     
                    NetSimResult sessionResult = session.exec(
                                                                    NetSimCommands.echo("./inst/netsim_pipe"),
                                                                    NetSimCommands.open(simulationName),
                                                                    NetSimCommands.selectnocallback(nodeName),
								    NetSimCommands.startlogger("error",90L),
                                                                    NetSimCommands.startlogger("session",90L),
                                                                    NetSimCommands.startlogger("mobility",90L));
                                                                    logger.info("SessionResult --->" + sessionResult);                           
                    TimeUnit.SECONDS.sleep(10);                  
                    boolean errorflag = ErrorInResult(sessionResult);
                    logger.info("ErrorInResult--->"+errorflag);
                    if(!errorflag){
                                    logger.info("Log Files are now present on the server");
                                    HelperUtility.setMapOfAttributes(mapOfAttributes);
                    }else{
                                    logger.error("Log Files are not present on the server");
                    }
                    }catch(Exception e){
                                    logger.error("NetSimPipeCommandHandler. startLogsOnServer .. Exception Occured-->", e);
                    }
    }
    
     private static boolean ErrorInResult(NetSimResult sessionResult) {
                                    CommandOutput[] Commandlist = sessionResult.getOutput();
                                    boolean Errorflag = false;
                                    for (CommandOutput command : Commandlist) {

                                                    List<String> list = command.asList();

                                                    if (!list.isEmpty()
                                                                                    && (list.get(0).toLowerCase().contains("error") || list
                                                                                                                    .get(0).contains("exception"))) {                                                                              
                                                                    Errorflag = true;
                                                    }
                                    }
                                    return Errorflag;

                    }


}
