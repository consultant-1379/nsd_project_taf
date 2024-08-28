/**
 * 
 */
package com.ericsson.nsd.taf.test.constants;

/**
 * @author xchashr
 *
 */
public final class StaticConstants {
	
	public static final String NEW_SGSNListInCS="NewSgsnListInCS";
	public static final String OLD_SGSNListInCS="OldSgsnListInCS";
    public static final String GGSNListInCS="GgsnListInCS";
    public static final String CPGListInCS = "CPGListInCS";
	public static final String MSCSListInCS = "MSCSListInCS";
	public static final String MSCBCListInCS="MSCBCListInCS";
	public static final String MSCListInCS = "MSCListInCS";
	public static final String PSPOOLListInCS = "PSPOOLListInCS";
	public static final String EPGListInCS = "EPGListInCS";
	public static final String APNListInCS = "ApnListInCS";
	public static final String TAListInCS = "TAListInCS";
	public static final String MSCPOOLListInCS = "MSCPOOLListInCS";
	public static final String BscListInCS = "BscListInCS";
	public static final String RncListInCS = "RncListInCS";
	public static final String SgsnBoardListInCS = "SgsnBoardListInCS";
	public static final String GgsnBoardListInCS = "GgsnBoardListInCS";
	public static final String MscMOListInCS = "MscMOListInCS";
	public static final String SASNMOListInCS = "SasnMOListInCS";
	public static final String DSCMOListInCS = "DscMOListInCS";
	public static final String DNSListInCS = "DNSListInCS";
	
	
	public static final String RoutiongAreaNetsimCommand="RoutiongAreaNetsimCommand";
	public static final String TrackingAreaNetsimCommand="TrackingAreaNetsimCommand";
	
	public static final String SGSNNetsimCommandADD="SGSNNetsimCommandADD";
	public static final String GGSNNetsimCommandADD="GGSNNetsimCommandADD";
	public static final String MSCSNetsimCommandADD="MSCSNetsimCommandADD";
	public static final String MSCBCNetsimCommandADD="MSCBCNetsimCommandADD";
	public static final String EPGNetsimCommandADD="EPGNetsimCommandADD";
	public static final String CPGNetsimCommandADD="CPGNetsimCommandADD";
	
	public static final String SGSNNetsimCommandDEL="SGSNNetsimCommandDEL";
	public static final String GGSNNetsimCommandDEL="GGSNNetsimCommandDEL";
	public static final String MSCSNetsimCommandDEL="MSCSNetsimCommandDEL";
	public static final String MSCBCNetsimCommandDEL="MSCBCNetsimCommandDEL";
	public static final String EPGNetsimCommandDEL="EPGNetsimCommandDEL";
	public static final String CPGNetsimCommandDEL="CPGNetsimCommandDEL";
	
	public static final String FAILED = "FAILED";
	
	public static final String PASSED = "PASSED";
	
	public static final String MIRRO_MIB_SYNCH_STATUS_ATTRIBUTE = "mirrorMIBsynchStatus";
	
	public static final String COLON = ":";
	
	public static final String SPACE = " ";
	
	public static final String UNSYNCH_VALUE = "4";
	
	public static final String SYNCH_VALUE_WITHOUT_NITIFICATION = "DISCOVERED";
	
	public static final String SYNCH_VALUE_WITH_NOTIFICATION = "SYNCHRONIZED";
	
	public static final String FETCH_OS_MACHINE_TYPE_COMMAND = "/usr/bin/uname -p";
	
	public static final String I386_OS_MACHINE_TYPE = "i386";
	
	public static final String SPARC_OS_MACHINE_TYPE = "sparc";
	
	public static final String NSD_EPC_LAUNCHER_FILE = "/opt/ericsson/nms_3_client_010_shell/bin/nsd4epc/nsd4epc";
	
	public static final String NSD_EPC_INI_FILE = "/opt/ericsson/nms_3_client_010_shell/bin/nsd4epc/nsd4epc.ini";
	
	public static final String I386_CONFIG_INI_FILE = "/opt/ericsson/nms_3_client_010_shell/bin/nsd4epc_i386_config.ini";
	
	public static final String SPARC_CONFIG_INI_FILE = "/opt/ericsson/nms_3_client_010_shell/bin/nsd4epc_sparc_config.ini";
	
	public static final String CEX_SCRIPT_PROPERTY_NAME = "cex_script";
	
	public static final String CEX_CONFIG_PROPERTY_NAME = "cex_config";
	
	public static final String CEX_BUNDLE_HEARTBEAT_PROPERTY_NAME = "cex.bundle.heart.beat";
	
	public static final String CEX_BUNDLE_HEARTBEAT_PROPERTY_VALUE = "80000";
	
	public static final String PROPERTY_FILE_CREATION_PATH_WITH_PROPERTY_FILE_NAME = "ERICTAFpsnsd_CXP9031094/src/main/resources/taf_properties/nsd_testware_runtime.properties";
	
	public static final String PATH_WHERE_TO_COPY_LAUNCHER_INI_CONFIG_FILE = "/opt/ericsson/nms_3_client_010_shell/bin/";

	public static final String NSD_CONFIG_INI_FILE_NAME_JDK7 = "nsd4epc_jdk7.ini";
	
	public static final String NSD_CONFIG_INI_FILE_NAME_ORIGINAL = "nsd4epc_original.ini";
	
	public static final String TAF_DIRECTORY_PATH_WHERE_CONFIG_FILES_PRESENT = "launcherConfigIniFiles/";
	
	public static final String REMOVE_COMMAND="rm -rf";
	
	public static final String VERSION_STRING = "VERSION:";
	
	public static final String WGET_COMMAND = "/usr/sfw/bin/wget";
	
	public static final String NEXUS_URL = "http://eselivm2v214l.lmera.ericsson.se:8081/nexus/service/local/repositories/releases/content";
	
	public static final String NEXUS_REPO_PATH = "/com/ericsson/oss/psnsd/";
	
	public static final String DELIMITER_HYPHEN = "-";
	
	public static final String DELIMITER_DOT = ".";
	
	public static final String DELIMITER_BACK_SLASH = "/";
	
	public static final String WGET_PARAMETER = "-O";
	
	public static final String SD_CXP_NUMBER = "CXP9021939";
	
	public static final String GROUP_ID = "com.ericsson.oss.epcsd";
	
	public static final String EXTENSION_TYPE = "jar";
	
	public static final String PKGINFO_COMMAND = "pkginfo -l ERICcpsnsd";
	
	public static final String NSD_CLIENT_PLUGIN_PATH = "/opt/ericsson/nms_3_client_010_shell/bin/nsd4epc/plugins";
	
	public static final String CD_COMMAND = "cd";
	
	public static final String CHMOD_COMMAND_WITH_PERMISSIONS = "chmod 755";
	
	public static final String PLUGINS_NAME_FILE = "pluginsToDownloadFromNexus.txt";
	
	public static final String PLUGINS_FILE_FOLDER_PATH = "filesToCopyOnServer/";
	
	public static final String SMTOOL_LIST_COMMAND = "/opt/ericsson/bin/smtool -list";
	
	public static final String SMTOOL_ONLINE_COMMAND = "/opt/ericsson/bin/smtool -online";
	
	public static final String STARTED = "started";
	
	public static final String SMTOOL_RMI_SERVICES_LIST_COMMAND = "/opt/ericsson/bin/smtool -viewrmi 50042";
	
	public static final String MISCELLENOUS_DIRECTORY_PATH = "miscellaneousFiles/";
	
	public static final String MC_NAMES_TO_CHECK_FILE = "mcNamesToCheck.txt";

	public static final String RMI_SERVICES_NAME_FILE = "rmiServicesNames.txt";

	public static final String PROCESS_ID = "ps -ef|grep nsd_cn_ps";

	public static final String HEAPDUMP_CMD = "find /ossrc/upgrade/JREheapdumps/ -name '*'";

	public static final String COREDUMP_CMD = "find /ossrc/upgrade/core/ -name '*'";

	public static final String MEMORY_UTILIZATION = "/usr/local/bin/top -n 1";

	public static final String USER_PERMISSION = "/opt/ericsson/bin/roleAdmin -contains DefaultNetworkAccess";
	
	public static final String MO_TYPE_SUPPORT = "moSupportedNodes.txt";
	
	public static final String INPUT_QUERY_FILE = "Input.sql";
	
	public static final String QUERY = "/opt/sybase/sybase/OCS-15_0/bin/isql -Usa -Psybase11 -Dfmadb_1_1 -i";
	
	public static final String QUERY_FILE_DESTINATION = "/home/nmsadm";
	
	public static final String NEW_LINE = "\n";
	

}
