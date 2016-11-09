package org.atilf.runner;

import org.atilf.models.termith.TermithIndex;
import org.atilf.thread.disambiguation.ContextLexiconThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Simon Meoni
 *         Created on 11/10/16.
 */
public class Disambiguation {

    private TermithIndex _termithIndex;
    private int _poolSize;
    private static final Logger LOGGER = LoggerFactory.getLogger(Exporter.class.getName());
    private static  final int POOL_SIZE = Runtime.getRuntime().availableProcessors();

    public Disambiguation(TermithIndex termithIndex){
        this(termithIndex, POOL_SIZE);
    }

    public Disambiguation(TermithIndex termithIndex, int poolSize){
        _poolSize = poolSize;
        _termithIndex = termithIndex;
    }

    public void execute() {
        ContextLexiconThread lexicon = new ContextLexiconThread(_termithIndex, 8);
        try {
            lexicon.execute();
        } catch (IOException | InterruptedException e) {
            LOGGER.error("errors during the sub-lexicon phase : ", e);
            Thread.currentThread().interrupt();
        }
        int a = 0;
//        LexiconProfileThread lexiconProfileThread = new LexiconProfileThread(_executorService, _poolSize);
//        try {
//            lexiconProfileThread.execute();
//        } catch (InterruptedException e) {
//            LOGGER.error("errors during the lexicon profile phase : ", e);
//            Thread.currentThread().interrupt();
//        }
//        DisambiguationEvaluationThread evaluation = new DisambiguationEvaluationThread(_executorService, _poolSize);
//        try {
//            evaluation.execute();
//        } catch (IOException | InterruptedException e) {
//            LOGGER.error("errors during evaluation phase : ", e);
//            Thread.currentThread().interrupt();
//        }
//        DisambiguationExporterThread exporter = new DisambiguationExporterThread(_executorService, _poolSize);
//        try {
//            exporter.execute();
//        } catch (IOException | InterruptedException e) {
//            LOGGER.error("errors during exporting phase : ", e);
//            Thread.currentThread().interrupt();
//        }
    }
}
