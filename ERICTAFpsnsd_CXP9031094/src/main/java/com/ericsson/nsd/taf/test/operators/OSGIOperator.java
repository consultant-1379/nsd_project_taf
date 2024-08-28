package com.ericsson.nsd.taf.test.operators;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.ApiOperator;
import com.ericsson.cifwk.taf.data.DataHandler;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.handlers.implementation.SshRemoteCommandExecutor;
import com.ericsson.cifwk.taf.osgi.client.ApiClient;
import com.ericsson.cifwk.taf.osgi.client.ApiContainerClient;
import com.ericsson.cifwk.taf.osgi.client.ContainerNotReadyException;
import com.ericsson.cifwk.taf.utils.FileFinder;
import com.ericsson.nsd.taf.test.getters.NSDMCGetter;
import com.ericsson.oss.taf.hostconfigurator.HostGroup;

public class OSGIOperator implements ApiOperator {

    private static Logger log = Logger.getLogger(OSGIOperator.class);

    private static ApiClient client = NSDMCGetter.getOsgiClient();

    private static final Host masterHost = HostGroup.getOssmaster();//DataHandler.getHostByName("ossmaster");

    private static final Long NSD_START_TIME = 50000L;

    private static final String NSD_CONFIG = DataHandler.getAttribute("cex_config").toString();

    private static final String NSD_SCRIPT = DataHandler.getAttribute("cex_script").toString();

    public static void main(final String[] args) throws ContainerNotReadyException, IOException {

        prepareNSD();
    }

	public static void prepareNSD() throws ContainerNotReadyException,
			IOException {

		launchNSD();
		OSGIOperator.registerOSGIRemoteParts();
	}

	private static void launchNSD() throws ContainerNotReadyException {

		String s = ApiContainerClient.constructEndpoint(masterHost, NSD_SCRIPT,
				NSD_CONFIG);
		final ApiContainerClient osgiContainer = ApiContainerClient
				.getInstance();
		osgiContainer.prepare(DataHandler.getAttribute("xdisplay").toString(),
				NSD_START_TIME);
	}

    
    private static void registerOSGIRemoteParts() throws IOException {

        final String workingDir = System.getProperty("user.dir");
        //System.out.println("Current working directory =====> " + workingDir);

        final List<String> groovyFiles = new ArrayList<String>();
        final List<String> rawGroovyFiles = FileFinder.findFile(".groovy");
        for (final String fullFilePath : rawGroovyFiles) {
            // filter files
            if (fullFilePath.toLowerCase().contains("/target/classes/scripts/".replace("/", File.separator))) {
                groovyFiles.add(fullFilePath);
                //System.out.println(fullFilePath);
            }
        }

        if (groovyFiles.size() > 0) {
            for (final String resource : groovyFiles) {
                final String fileName = resource.substring(resource.lastIndexOf(File.separator) + 1);
                final String remotePart = fileName.substring(0, fileName.indexOf(".groovy"));
                String var1=client.register(readResource(resource)).getValue().toString();
                if (client.register(readResource(resource)).getValue().equals(remotePart)) {
                                
                    log.info("Deploy OSGI remote part " + remotePart + " successfully.");
                }
                else {
                    throw new IOException("Cannot deploy OSGi remote part: " + remotePart);
                }
            }
        }
    }

    private static String readResource(final String path) throws IOException {
    	final InputStream in = new FileInputStream(path);
        Scanner scanner = null;
        try {
            scanner = new Scanner(in);
            return scanner.useDelimiter("\\A").next();
        }
        finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }
}


