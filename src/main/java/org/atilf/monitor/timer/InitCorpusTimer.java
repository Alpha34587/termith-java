package org.atilf.monitor.timer;

import org.atilf.models.TermithIndex;
import org.slf4j.Logger;

/**
 * Timer for initialization task
 * @author Simon Meoni
 *         Created on 19/09/16.
 */
public class InitCorpusTimer extends ProgressBarTimer{

    public InitCorpusTimer(TermithIndex termithIndex, Logger logger) {
        super(termithIndex, "Init corpus progression", logger);
    }
    @Override
    public void run() {
        update(_termithIndex.getXmlCorpus().size());
    }
}
