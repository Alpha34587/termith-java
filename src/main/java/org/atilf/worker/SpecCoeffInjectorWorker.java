package org.atilf.worker;

import org.atilf.models.RLexic;
import org.atilf.models.TermithIndex;
import org.atilf.module.disambiguisation.SpecCoefficientInjector;

/**
 * @author Simon Meoni
 *         Created on 21/10/16.
 */
public class SpecCoeffInjectorWorker implements Runnable {
    private final String key;
    private final TermithIndex termithIndex;
    private final RLexic rLexic;

    public SpecCoeffInjectorWorker(String key, TermithIndex termithIndex, RLexic rLexic) {

        this.key = key;
        this.termithIndex = termithIndex;
        this.rLexic = rLexic;
    }

    @Override
    public void run() {
        SpecCoefficientInjector specCoeff = new SpecCoefficientInjector(termithIndex.get_termSubLexic().get(key),
                rLexic,termithIndex.get_disambGlobalLexic());
        specCoeff.execute();
    }
}
