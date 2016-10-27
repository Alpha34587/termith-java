package org.atilf.module.timer;

import org.atilf.models.TermithIndex;
import org.slf4j.Logger;

/**
 * @author Simon Meoni
 *         Created on 19/09/16.
 */
public class ExporterTimer extends ProgressBarTimer {

    public ExporterTimer(TermithIndex termithIndex, Logger logger) {
        super(termithIndex, logger, 5000, "writing file progression");
    }

    @Override
    public void run() {
        update(termithIndex.get_outputFile().size());
    }
}
