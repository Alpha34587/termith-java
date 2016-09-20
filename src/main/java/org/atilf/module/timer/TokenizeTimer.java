package org.atilf.module.timer;

import org.atilf.models.TermithIndex;
import org.slf4j.Logger;

/**
 * @author Simon Meoni
 *         Created on 19/09/16.
 */
public class TokenizeTimer extends ProgressBarTimer {

    public TokenizeTimer(TermithIndex termithIndex, Logger logger) {
        super(termithIndex, logger, 100, "xml tokenization progression");
    }
    @Override
    public void run() {
        update(termithIndex.getTokenizeTeiBody().size());
    }

}
