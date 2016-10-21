package org.atilf.thread;

import org.atilf.models.RLexic;
import org.atilf.models.TermithIndex;

/**
 * @author Simon Meoni
 *         Created on 12/10/16.
 */
public class LexicProfileThread {
    private final TermithIndex termithIndex;
    private final int poolSize;

    public LexicProfileThread(TermithIndex termithIndex, int poolSize) {

        this.termithIndex = termithIndex;
        this.poolSize = poolSize;
    }

    public void execute() {
        RLexic rLexic = new RLexic(termithIndex.getDisambGlobalCorpus());
        termithIndex.getTermSubLexic().forEach(
                (key,value) -> {

                }
        );
    }
}
