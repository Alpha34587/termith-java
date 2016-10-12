package org.atilf.runner;

import org.atilf.models.TermithIndex;

/**
 * @author Simon Meoni
 *         Created on 12/10/16.
 */
public class InjectionThread {
    private final TermithIndex termithIndex;
    private final int poolSize;

    public InjectionThread(TermithIndex termithIndex, int poolSize) {

        this.termithIndex = termithIndex;
        this.poolSize = poolSize;
    }

    public void execute() {

    }
}
