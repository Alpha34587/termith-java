package org.atilf.worker;

import org.atilf.models.RLexic;
import org.atilf.models.TermithIndex;
import org.atilf.module.disambiguisation.LexicalProfile;
import org.atilf.module.disambiguisation.SpecCoeffCalculator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        SpecCoeffCalculator specCoeff = new SpecCoeffCalculator(termithIndex.getTermSubLexic().get(key),
                rLexic,termithIndex.getDisambGlobalLexic());
        specCoeff.execute();
    }
}
