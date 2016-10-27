package org.atilf.module.timer;

import org.atilf.models.TermithIndex;
import org.slf4j.Logger;

/**
 * @author Simon Meoni
 *         Created on 19/09/16.
 */
public class TerminologyTimer extends ProgressBarTimer {

    public TerminologyTimer(TermithIndex termithIndex, Logger logger) {
        super(termithIndex, logger, 1, "terminology extraction progression");
    }
    @Override
    public void run() {
        update(termithIndex.get_terminologyStandOff().size());
    }

}