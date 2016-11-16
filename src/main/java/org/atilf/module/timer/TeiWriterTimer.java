package org.atilf.module.timer;

import org.atilf.models.termith.TermithIndex;
import org.slf4j.Logger;

/**
 * timer for teiWriter task
 * @author Simon Meoni
 *         Created on 19/09/16.
 */
public class TeiWriterTimer extends ProgressBarTimer {

    public TeiWriterTimer(TermithIndex termithIndex, Logger logger) {
        super(termithIndex, logger, 5000, "writing file progression");
    }

    @Override
    public void run() {
        update(_termithIndex.getOutputFile().size());
    }
}
