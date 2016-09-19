package module.tools;

import ch.qos.logback.classic.Level;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Simon Meoni
 *         Created on 19/09/16.
 */
public class CLIUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(CLIUtils.class.getName());
    private static Level level = Level.INFO;

    public static void setGlobalLogLevel(Level level) {
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger)LoggerFactory
                .getLogger(Logger.ROOT_LOGGER_NAME);
        logger.setLevel(level);
    }
}
