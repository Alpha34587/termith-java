package org.atilf.thread;

import org.atilf.models.TermithIndex;

/**
 * @author Simon Meoni
 *         Created on 12/10/16.
 */
public class DisambEvaluationThread {
    private final TermithIndex termithIndex;
    private final int poolSize;

    public DisambEvaluationThread(TermithIndex termithIndex, int poolSize) {

        this.termithIndex = termithIndex;
        this.poolSize = poolSize;
    }

    public void execute() {

    }
}
