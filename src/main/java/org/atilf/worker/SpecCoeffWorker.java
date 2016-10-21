package org.atilf.worker;

import org.atilf.models.RLexic;
import org.atilf.models.TermithIndex;

/**
 * @author Simon Meoni
 *         Created on 21/10/16.
 */
public class SpecCoeffWorker implements Runnable {
    private final String key;
    private final TermithIndex termithIndex;
    private final RLexic rLexic;

    public SpecCoeffWorker(String key, TermithIndex termithIndex, RLexic rLexic) {

        this.key = key;
        this.termithIndex = termithIndex;
        this.rLexic = rLexic;
    }

    @Override
    public void run() {

    }
}
