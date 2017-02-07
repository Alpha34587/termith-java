package org.atilf.monitor.timer;

import org.slf4j.Logger;

/**
 * @author Simon Meoni
 *         Created on 19/09/16.
 */
public class ProgressBar {

    private StringBuilder _progress;
    private String _message;

    /**
     * initialize _progress bar properties.
     * @param message the title of the observed thread
     */
    public ProgressBar(String message) {
        _message = message;
        init();
    }

    /**
     * called whenever the _progress bar needs to be updated.
     * that is whenever _progress was made.
     *
     * @param done an int representing the work done so far
     * @param total an int representing the total work
     */
    public synchronized void update(int done, int total, Logger logger) {
        int percent = (done * 100) / total;
        int extraChars = (percent / 2) - _progress.length();
        String fileProgress = "(" + done + "/" + total + ")";
        while (extraChars-- > 0) {
            _progress.append('#');
        }

        logger.info("{} : {}% {}", _message, percent, fileProgress);
        if (done == total) {
            init();
        }
    }

    private void init() {
        _progress = new StringBuilder(60);
    }
}
