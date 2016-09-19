package module.timer;

import models.TermithIndex;
import org.slf4j.Logger;

/**
 * @author Simon Meoni
 *         Created on 19/09/16.
 */
public class ExporterTimer extends ProgressBarTimer {

    public ExporterTimer(TermithIndex termithIndex, Logger logger) {
        super(termithIndex, logger, 1, "serialization progression");
    }

    @Override
    public void run() {
        update(termithIndex.getTerminologyStandOff().size());
    }
}
