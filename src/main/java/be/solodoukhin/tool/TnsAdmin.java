package be.solodoukhin.tool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.File;

/**
 * Brief description here....
 *
 * @author viktor.solodoukhin@groups.be
 * @since 2018.12.03
 */
public class TnsAdmin {
    private static final Logger LOGGER = LoggerFactory.getLogger(TnsAdmin.class);

    private static final String ENV_VAR_TNS = "oracle.net.tns_admin";

    /**
     * Uses the oracle.net.tns_admin property or (if missing) the path property,
     * to look for the directory in which the file tnsnames.ora is to be found
     *
     * @return A string, with the found directory
     */
    public static void init() {
        String tnsAdmin = System.getenv(ENV_VAR_TNS);
        if (!StringUtils.hasText(tnsAdmin)) {
            tnsAdmin = System.getProperty(ENV_VAR_TNS);
            if (!StringUtils.hasText(tnsAdmin)) {
                String path = System.getenv("path");
                if (path != null) {
                    String[] pathDirectories = path.split(File.pathSeparator);
                    for (int i = 0; i < pathDirectories.length && (tnsAdmin == null || tnsAdmin.isEmpty()); i++) {
                        File tnsNamesDirectory = new File(pathDirectories[i]);
                        tnsNamesDirectory = tnsNamesDirectory.getParentFile();
                        tnsNamesDirectory = new File(tnsNamesDirectory, "network");
                        tnsNamesDirectory = new File(tnsNamesDirectory, "admin");
                        if ((tnsNamesDirectory.exists()) && (tnsNamesDirectory.isDirectory())) {
                            if (tnsNamesFileExists(tnsNamesDirectory)) {
                                tnsAdmin = tnsNamesDirectory.toString();
                                break;
                            }
                        }
                    }
                }
            }
        }
        if (StringUtils.hasText(tnsAdmin)) {
            System.setProperty(ENV_VAR_TNS, tnsAdmin);
        } else {
            LOGGER.warn(
                    "The environment path variable does not contain the Oracle BIN-directory, necessary to find tnsnames.ora!");
            LOGGER.info("    To solve this problem you have 2 possbible solutions : ");
            LOGGER.info("        1. Modify the path variable of your system and set the Oracle BIN directory");
            LOGGER.info("               (the parent of this directory contains tnsnames.ora in subdirectory network/admin)");
            LOGGER.info("        2. Set the 'oracle.net.tns_admin' system property = the directory which contains tnsnames.ora");
        }
    }

    private static boolean tnsNamesFileExists(File pathDirectory) {
        boolean exists = false;
        File tnsNamesFile = new File(pathDirectory, "tnsnames.ora");
        if ((tnsNamesFile.exists()) && (tnsNamesFile.isFile())) {
            exists = true;
        }
        return exists;
    }
}
