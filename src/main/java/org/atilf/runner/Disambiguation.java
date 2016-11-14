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

    /**
     * this constructor initializes the _termithIndex field.
     * @param termithIndex
     *         the termithIndex is an object that contains the results of the process
     */
    public Disambiguation(TermithIndex termithIndex){
        super(termithIndex, Runner.DEFAULT_POOL_SIZE);
    }

    /**
     * this constructor initializes the _termithIndex field and the number of thread used during the process
     * @param termithIndex
     *         the termithIndex is an object that contains the results of the process
     * @param poolSize
     *         the number of thread used during the process
     */
    public Disambiguation(TermithIndex termithIndex, int poolSize){
        super(termithIndex, poolSize);
    }

    /**
     * The execute method of Disambiguation has four phases :
     *      1) ContextLexiconThread : the extraction of context for each terminology entries in the learning corpus
     *      2) LexiconProfileThread :the creation of lexical profile for each terminology entries
     *      3) EvaluationThread : the evaluation consists to extract context for terminologies entries in the evaluation
     *      corpus and compare context evaluation corpus and learning evaluation
     *      in order to determine the terminology or not a terms candidates in the evaluation corpus
     *      4) DisambiguationExporterThread : report the result on the associate tei file
     *      @see ContextLexiconThread
     *      @see LexiconProfileThread
     *      @see EvaluationThread
     *      @see DisambiguationExporterThread
     */
    @Override
    public void execute() {
        /*
        Context extraction phase
         */
        ContextLexiconThread lexicon = new ContextLexiconThread(_termithIndex, _poolSize);
        try {
            lexicon.execute();
        } catch (IOException | InterruptedException e) {
            _logger.error("errors during the sub-lexicon phase : ", e);
            Thread.currentThread().interrupt();
        }
        /*
        Lexicon profile processing
         */
        LexiconProfileThread lexiconProfileThread = new LexiconProfileThread(_termithIndex, _poolSize);
        try {
            lexiconProfileThread.execute();
        } catch (InterruptedException e) {
            _logger.error("errors during the lexicon profile phase : ", e);
            Thread.currentThread().interrupt();
        }
        /*
        Evaluation phase
         */
        EvaluationThread evaluation = new EvaluationThread(_termithIndex, _poolSize);
        try {
            evaluation.execute();
        } catch (IOException | InterruptedException e) {
            _logger.error("errors during evaluation phase : ", e);
            Thread.currentThread().interrupt();
        }
        /*
        Export results
         */
        DisambiguationExporterThread exporter = new DisambiguationExporterThread(_termithIndex, _poolSize);
        try {
            exporter.execute();
        } catch (IOException | InterruptedException e) {
            _logger.error("errors during exporting phase : ", e);
            Thread.currentThread().interrupt();
        }
    }
}
