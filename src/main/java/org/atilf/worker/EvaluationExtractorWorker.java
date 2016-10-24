package org.atilf.worker;

import org.atilf.models.TermithIndex;
import org.atilf.module.disambiguisation.EvaluationExtractor;
import org.atilf.module.disambiguisation.SubLexicExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.concurrent.Callable;

/**
 * @author Simon Meoni
 *         Created on 24/10/16.
 */
public class EvaluationExtractorWorker implements Runnable {
    private final Path p;
    private final TermithIndex termithIndex;
    private static final Logger LOGGER = LoggerFactory.getLogger(SubLexicExtractorWorker.class.getName());


    public EvaluationExtractorWorker(Path p, TermithIndex termithIndex) {
        this.p = p;
        this.termithIndex = termithIndex;
    }

    @Override
    public void run() {
        LOGGER.debug("add " + p + " to evaluation lexic");
        EvaluationExtractor evaluationExtractor = new EvaluationExtractor(p.toString(),
                termithIndex.getEvaluationLexic());
        LOGGER.debug(p + " added");
        evaluationExtractor.execute();
    }
}
