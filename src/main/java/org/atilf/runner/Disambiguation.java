package org.atilf.runner;

import org.atilf.models.termith.TermithIndex;
import org.atilf.thread.disambiguation.ContextLexiconThread;
import org.atilf.thread.disambiguation.DisambiguationExporterThread;
import org.atilf.thread.disambiguation.EvaluationThread;
import org.atilf.thread.disambiguation.LexiconProfileThread;

import java.io.IOException;

/**
 * @author Simon Meoni
 *         Created on 11/10/16.
 */
public class Disambiguation extends Runner {
    
    public Disambiguation(TermithIndex termithIndex){
        this(termithIndex, Runner.DEFAULT_POOL_SIZE);
    }

    public Disambiguation(TermithIndex termithIndex, int poolSize){
        _poolSize = poolSize;
        _termithIndex = termithIndex;
    }

    public void execute() {
        ContextLexiconThread lexicon = new ContextLexiconThread(_termithIndex, _poolSize);
        try {
            lexicon.execute();
        } catch (IOException | InterruptedException e) {
            _logger.error("errors during the sub-lexicon phase : ", e);
            Thread.currentThread().interrupt();
        }
        LexiconProfileThread lexiconProfileThread = new LexiconProfileThread(_termithIndex, _poolSize);
        try {
            lexiconProfileThread.execute();
        } catch (InterruptedException e) {
            _logger.error("errors during the lexicon profile phase : ", e);
            Thread.currentThread().interrupt();
        }
        EvaluationThread evaluation = new EvaluationThread(_termithIndex, _poolSize);
        try {
            evaluation.execute();
        } catch (IOException | InterruptedException e) {
            _logger.error("errors during evaluation phase : ", e);
            Thread.currentThread().interrupt();
        }
        DisambiguationExporterThread exporter = new DisambiguationExporterThread(_termithIndex, _poolSize);
        try {
            exporter.execute();
        } catch (IOException | InterruptedException e) {
            _logger.error("errors during exporting phase : ", e);
            Thread.currentThread().interrupt();
        }
    }
}
