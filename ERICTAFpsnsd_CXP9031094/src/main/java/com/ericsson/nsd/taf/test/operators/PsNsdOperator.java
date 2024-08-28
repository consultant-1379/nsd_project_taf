package com.ericsson.nsd.taf.test.operators;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.annotations.Context;
import com.ericsson.cifwk.taf.annotations.Operator;
import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.osgi.client.ApiClient;
import com.ericsson.nsd.taf.test.util.HelperUtility;
import com.ericsson.oss.cex.operator.CEXOperator;

/**
 * PsNsdOperator
 * 
 * Operator to prepare OSGI container for PsNsd application
 * 
 * @author ekellmi
 * 
 */
// @Anomaly its not annotated as operator
@Operator(context = Context.CLI)
@Singleton
public class PsNsdOperator extends CEXOperator{

   	private static final Logger log = Logger.getLogger(PsNsdOperator.class);

    private static ApiClient apiClient;

  	@Inject
    public PsNsdOperator(final Host host) {
			super(host);		
		}
	 
	 
	/**
     * Get OSGI Client
     * 
     * @return osgi client
     */
    public static ApiClient getClient() {
        return apiClient;
    }
    
    /**
     * set client to be used in GroovyTestOperators.
     * @param client
     */
    public void setClient(final ApiClient client) {
        apiClient = client;
        HelperUtility.setApiClient(apiClient);
    }

}