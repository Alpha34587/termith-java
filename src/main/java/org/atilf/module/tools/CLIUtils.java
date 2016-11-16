package org.atilf.module.tools;

import ch.qos.logback.classic.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CLI tools for cli package
 * @author Simon Meoni
 *         Created on 19/09/16.
 */
public class CLIUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(CLIUtils.class.getName());

    /**
     * set log level
     * @param level the level of log that we want to show during the process
     */
    public static void setGlobalLogLevel(Level level) {
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger)LoggerFactory
                .getLogger(Logger.ROOT_LOGGER_NAME);
        logger.setLevel(level);
    }
}
