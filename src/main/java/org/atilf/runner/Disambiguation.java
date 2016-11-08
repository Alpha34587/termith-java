package org.atilf.runner;

import org.atilf.models.termith.TermithIndex;
import org.atilf.thread.disambiguation.ContextLexicThread;
import org.atilf.thread.disambiguation.DisambEvaluationThread;
import org.atilf.thread.disambiguation.DisambExporterThread;
import org.atilf.thread.disambiguation.LexicProfileThread;
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
        ContextLexicThread lexic = new ContextLexicThread(_termithIndex, _poolSize);
        try {
            lexic.execute();
        } catch (IOException | InterruptedException e) {
            LOGGER.error("errors during the subLexic phase : ", e);
            Thread.currentThread().interrupt();
        }
        LexicProfileThread lexicProfileThread = new LexicProfileThread(_termithIndex, _poolSize);
        try {
            lexicProfileThread.execute();
        } catch (InterruptedException e) {
            LOGGER.error("errors during the lexicProfile phase : ", e);
            Thread.currentThread().interrupt();
        }
        DisambEvaluationThread evaluation = new DisambEvaluationThread(_termithIndex, _poolSize);
        try {
            evaluation.execute();
        } catch (IOException | InterruptedException e) {
            LOGGER.error("errors during evaluation phase : ", e);
            Thread.currentThread().interrupt();
        }
        DisambExporterThread exporter = new DisambExporterThread(_termithIndex, _poolSize);
        try {
            exporter.execute();
        } catch (IOException | InterruptedException e) {
            LOGGER.error("errors during exporting phase : ", e);
            Thread.currentThread().interrupt();
        }
    }
}
