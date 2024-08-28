package com.ericsson.nsd.taf.test.robustness;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.ericsson.cifwk.taf.data.Host;
import com.ericsson.cifwk.taf.data.User;
import com.ericsson.cifwk.taf.data.UserType;
import com.ericsson.cifwk.taf.handlers.netsim.NetsimHandler;
import com.ericsson.cifwk.taf.tools.cli.CLICommandHelper;
import com.ericsson.oss.taf.cshandler.CSDatabase;
import com.ericsson.oss.taf.cshandler.CSTestHandler;
import com.ericsson.oss.taf.cshandler.model.Fdn;
import com.ericsson.oss.taf.cshandler.model.Filter;
import com.ericsson.oss.taf.hostconfigurator.OssHost;

@SuppressWarnings("deprecation")
public class ForNodeStart  extends CSTestHandler {

	/*private static final String CMD_LT = "lt";

	private static final String CMD_ARG_FILTER = "-f";	

*/	
	private static final String CSTEST = "/opt/ericsson/nms_cif_cs/etc/unsupported/bin/cstest -s ";

	//private static final Logger logger = Logger.getLogger(ForNodeStart.class);

	private final CSDatabase database;

	private final CLICommandHelper handler;

	public ForNodeStart(Host host, CSDatabase database) {
		super(host, database);
		this.database = database;	
		final OssHost ossHost = new OssHost(host);
		final User nmsadm = ossHost.getNmsadmUser();
		this.handler = new CLICommandHelper(host, nmsadm);	
	}

	//this is for netsim host with CLICommandHelper
	public ForNodeStart(Host host, CSDatabase database, boolean netsimuser) {
		super(host, database);
		this.database = database;	
		final OssHost ossHost = new OssHost(host);
		User user = ossHost.getNetsimUser();
		this.handler = new CLICommandHelper(host, user);
	}
	
	
	/*public List<Fdn> getByType(final String type, final Filter filter) {
		String stdout;
		if (filter == null) {
			stdout = execute(CMD_LT, type);
		} else {
			stdout = execute(CMD_LT, type, CMD_ARG_FILTER, "'" + filter.getFilter() + "'");
		}

		return convertStdoutToFdnList(stdout);
	}	
*/
	
		public String executeCommand(final String cmd) {
			final String stdout = this.handler.execute(cmd);
			return stdout;
		}

			/*public String detachAdapterTSP(String fdn) {
				String cmd = "" + "detach" + " " + fdn;
				execute(cmd);
				try {
					TimeUnit.SECONDS.sleep(10);
				} catch (InterruptedException ie) {
					logger.error("Detach interrupted");
				}
				return cmd;
			}*/
		
	public String executetest(final String cmd) {
		final String stdout = handler.simpleExec(cmd);
		return stdout;
	}
	
	
	public String restartNetsim(final String cmd) {
		final String stdout = handler.execute(cmd);
		return stdout;
	}
	
	/*private String execute(final String cmd, final String... args) {
		final String cmdToExec = buildCommandToExecute(cmd, args);

		logger.info("Executing: " + cmdToExec);
		final String stdout = handler.simpleExec(cmdToExec);
		final int exitCode = handler.getCommandExitValue();

		if (exitCode != 0) {
			logger.error(stdout);
		} else {
			logger.info("Command result: " + stdout);
		}
		return stdout;
	}
*/
	/*private String buildCommandToExecute(final String cmd, final String... args) {
		final StringBuilder cmdToExec = new StringBuilder();
		cmdToExec.append(CSTEST);
		cmdToExec.append(database.getDatabaseName());
		cmdToExec.append(" ");
		cmdToExec.append(cmd);
		cmdToExec.append(" ");
		for (final String arg : args) {
			cmdToExec.append(arg);
			cmdToExec.append(" ");
		}
		return cmdToExec.toString();
	}

	private List<Fdn> convertStdoutToFdnList(final String stdout) {
		final List<Fdn> listOfFdns = new ArrayList<Fdn>();
		if (stdout.isEmpty() || stdout.startsWith("exception")) {
			return Collections.emptyList();
		}

		final String[] lines = stdout.split("\\n");
		for (final String fdn : lines) {
			listOfFdns.add(new Fdn(fdn));
		}

		return listOfFdns;
	}

	public void attachAdapter(String fdn) {
		String adapterName = NMAAdaptorImplOperator.adapterInfo.get(fdn);
		logger.info("adapterName in attachAdapter :: "+adapterName);
		String cmd;
		if (!adapterName.isEmpty()) {
			cmd = "" + "attach" + " " + fdn
					+ " " + adapterName;
		} else {
			cmd = "" + "attach" + " " + fdn
					+ " " +"nma";
		}
		logger.info("cmd to attach adapter :: "+cmd);
		execute(cmd);
	}

	public void detachAdapter(String fdn) {
		String cmd = "" + "detach" + " " + fdn;
		execute(cmd);
		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException ie) {
			logger.error("Detach interrupted");
		}
	}
	
	

	public String getAdaptorAttached(String fdn) {
		String command = "" + "mi" +" "
				+ fdn + " " + NMAUtilities.getCommand("GrepAdapter");
		String attribute = execute(command);
		String adapterName = getAttributeValue(attribute);
		logger.info("Adapter:$" + adapterName + "$");
		return adapterName;
	}

	private static String getAttributeValue(String attributeCmd) {
		String attributeValue = "";
		if(attributeCmd != null && attributeCmd.split(":").length > 1) {
			attributeValue = attributeCmd.split(":")[1].split("\n")[0];
		}
		return attributeValue.trim();
	}

	public boolean isAdapterAttached(String fdn) {
		boolean flag = false;
		String adapterName = getAdaptorAttached(fdn);
		logger.info("adapterName :: "+adapterName);
		if (adapterName.trim().isEmpty()) {
			flag = true;
			logger.info("try to attachadapter");
			attachAdapter(fdn);
			adapterName = getAdaptorAttached(fdn);
		} else {
			flag = true;
			logger.info("Adapter is already attached");
		}
		if (flag && !adapterName.trim().isEmpty()) {
			logger.info("Adapter is attached successfully");
			return true;
		} else
			return false;
	}
*/
	
}

