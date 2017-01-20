package org.atilf.monitor.timer;

import org.atilf.models.termith.TermithIndex;
import org.slf4j.Logger;

/**
 * the Timer for Morphology json writing
 * @author Simon Meoni
 *         Created on 20/09/16.
 */
public class JsonTimer extends ProgressBarTimer {

    public JsonTimer(TermithIndex termithIndex, Logger logger) {
        super(termithIndex, logger, 5000, "Json serialization progression");
    }

    @Override
    public void run() {
        update(_termithIndex.getSerializeJson().size());
    }
}
