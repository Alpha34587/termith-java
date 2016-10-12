package org.atilf.thread;

import org.atilf.models.TermithIndex;

/**
 * @author Simon Meoni
 *         Created on 12/10/16.
 */
public class SubLexicThread {
    private final TermithIndex termithIndex;
    private final int poolSize;

    public SubLexicThread(TermithIndex termithIndex, int poolSize) {

        this.termithIndex = termithIndex;
        this.poolSize = poolSize;
    }

    public void execute() {

    }
}
