package org.atilf.runner;

import org.atilf.models.TermithIndex;
import org.atilf.thread.DisambEvaluationThread;
import org.atilf.thread.LexicProfileThread;
import org.atilf.thread.SubLexicThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

/**
 * @author Simon Meoni
 *         Created on 11/10/16.
 */
public class Disambiguisation {
    private static final Logger LOGGER = LoggerFactory.getLogger(Exporter.class.getName());
    private static  final int POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private ExecutorService executor;
    private TermithIndex termithIndex;
    private int poolSize;

    public Disambiguisation(TermithIndex termithIndex){
        this(termithIndex, POOL_SIZE);
    }

    public Disambiguisation(TermithIndex termithIndex, int poolSize){
        this.poolSize = poolSize;
        this.termithIndex = termithIndex;
    }

    public void execute() {
        SubLexicThread lexic = new SubLexicThread(termithIndex,poolSize);
        try {
            lexic.execute();
        } catch (IOException | InterruptedException e) {
            LOGGER.error("errors during the subLexic phase : ", e);
        }
        LexicProfileThread lexicProfileThread = new LexicProfileThread(termithIndex,poolSize);
        try {
            lexicProfileThread.execute();
        } catch (InterruptedException e) {
            LOGGER.error("errors during the lexicProfile phase : ", e);
        }
        DisambEvaluationThread evaluation = new DisambEvaluationThread(termithIndex,poolSize);
        evaluation.execute();
        InjectionThread injection = new InjectionThread(termithIndex,poolSize);
        injection.execute();

    }
}
