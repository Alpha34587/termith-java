package module.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Simon Meoni
 *         Created on 19/09/16.
 */
public class ProgressBar {
    /**
     * Ascii progress meter. On completion this will reset itself,
     * so it can be reused
     * <br /><br />
     * 100% ################################################## |
     */
    private StringBuilder progress;
    private String message;
    private static final Logger LOGGER = LoggerFactory.getLogger(ProgressBar.class.getName());

    /**
     * initialize progress bar properties.
     * @param message
     */
    public ProgressBar(String message) {
        this.message = message;
        init();
    }

    /**
     * called whenever the progress bar needs to be updated.
     * that is whenever progress was made.
     *
     * @param done an int representing the work done so far
     * @param total an int representing the total work
     */
    public synchronized void update(int done, int total, Logger logger) {
        int percent = (done * 100) / total;
        int extrachars = (percent / 2) - this.progress.length();
        String fileProgress = "(" + done + "/" + total + ")";
        while (extrachars-- > 0) {
            progress.append('#');
        }

        logger.info("{} : {}% {}", message, percent, fileProgress);
        if (done == total) {
            init();
        }
    }

    private void init() {
        this.progress = new StringBuilder(60);
    }
}
