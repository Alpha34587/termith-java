package org.atilf.worker;

import org.atilf.models.TermithIndex;
import org.atilf.module.disambiguisation.EvaluationInjector;
import org.atilf.module.disambiguisation.EvaluationProfile;
import org.atilf.module.disambiguisation.LexicalProfile;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * @author Simon Meoni
 *         Created on 24/10/16.
 */
public class EvaluationInjectorWorker implements Runnable {
    private final String term;
    private TermithIndex termithIndex;

    public EvaluationInjectorWorker(String term, TermithIndex termithIndex) {
        this.term = term;
        this.termithIndex = termithIndex;
    }

    @Override
    public void run() {
        EvaluationInjector evaluation = new EvaluationInjector(
                term,
                termithIndex.getEvaluationLexic(),
                termithIndex.getTermSubLexic()
        );
        evaluation.execute();
    }
}
