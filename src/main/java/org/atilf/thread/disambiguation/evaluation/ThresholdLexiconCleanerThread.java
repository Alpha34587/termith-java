package org.atilf.thread.disambiguation.evaluation;

import org.atilf.models.TermithIndex;
import org.atilf.models.disambiguation.DisambiguationXslResources;
import org.atilf.module.disambiguation.evaluation.ThresholdLexiconCleaner;
import org.atilf.thread.Thread;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static org.atilf.runner.Runner.DEFAULT_POOL_SIZE;

/**
 * Evaluate the corpus with the lexicalProfile creates with the LexiconProfileThread
 * @author Simon Meoni
 *         Created on 12/10/16.
 */
public class ThresholdLexiconCleanerThread extends Thread{

    /**
     * this constructor initialize the _termithIndex fields and initialize the _poolSize field with the default value
     * with the number of available processors.
     *
     * @param termithIndex
     *         the termithIndex is an object that contains the results of the process*
     */
    public ThresholdLexiconCleanerThread(TermithIndex termithIndex) {
        this(termithIndex,DEFAULT_POOL_SIZE);
    }

    /**
     * this constructor initialize all the needed fields. it initialize the termithIndex who contains the result of
     * process and initialize the size of the pool of _executorService field.
     *
     * @param termithIndex
     *         the termithIndex is an object that contains the results of the process
     * @param poolSize
     *         the number of thread used during the process
     *
     * @see TermithIndex
     * @see ExecutorService
     */
    public ThresholdLexiconCleanerThread(TermithIndex termithIndex, int poolSize) {
        super(termithIndex, poolSize);
    }

    /**
     * this method is split in two parts. Firstly, for each file, the context is extract for each terms candidates.
     * Finally, each context is evaluated with the lexical associated in order to determine the terminology potentiality
     * of a term candidate.
     * @throws IOException thrown a IO exception if a file is not found or have a permission problem during the
     * xsl transformation phase
     * @throws InterruptedException thrown if awaitTermination function is interrupted while waiting
     */
    public void execute() throws IOException, InterruptedException {
        DisambiguationXslResources xslResources = new DisambiguationXslResources();

        /*
        Threshold cleaner
         */
        _termithIndex.getContextLexicon().keySet().forEach(
                key -> _executorService.submit(new ThresholdLexiconCleaner(
                        key,
                        _termithIndex,
                        3,
                        15
                ))
        );
        _logger.info("Waiting EvaluationWorker executors to finish");
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }
}
