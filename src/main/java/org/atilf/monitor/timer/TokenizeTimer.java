package org.atilf.monitor.timer;

import org.atilf.models.TermithIndex;
import org.slf4j.Logger;

/**
 * timer for tokenize body
 * @author Simon Meoni
 *         Created on 19/09/16.
 */
public class TokenizeTimer extends ProgressBarTimer {

    public TokenizeTimer(TermithIndex termithIndex, Logger logger) {
        super(termithIndex, logger, 5000, "xml tokenization progression");
    }
    @Override
    public void run() {
        update(_termithIndex.getTokenizeTeiBody().size());
    }

}
