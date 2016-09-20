package org.atilf.module.timer;

import org.atilf.models.TermithIndex;
import org.slf4j.Logger;

/**
 * @author Simon Meoni
 *         Created on 20/09/16.
 */
public class JsonTimer extends ProgressBarTimer {
    public JsonTimer(TermithIndex termithIndex, Logger logger) {
        super(termithIndex, logger, 100, "Json serialization progression");
    }

    @Override
    public void run() {
        update(termithIndex.getSerializeJson().size());
    }
}
