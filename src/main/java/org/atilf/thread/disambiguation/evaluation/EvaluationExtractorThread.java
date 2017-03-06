package org.atilf.thread.disambiguation.evaluation;

import org.atilf.models.TermithIndex;
import org.atilf.module.disambiguation.evaluation.EvaluationExtractor;
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
public class EvaluationExtractorThread extends Thread{

    /**
     * this constructor initialize the _termithIndex fields and initialize the _poolSize field with the default value
     * with the number of available processors.
     *
     * @param termithIndex
     *         the termithIndex is an object that contains the results of the process*
     */
    public EvaluationExtractorThread(TermithIndex termithIndex) {
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
    public EvaluationExtractorThread(TermithIndex termithIndex, int poolSize) {

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


        _termithIndex.getEvaluationTransformedFiles().values().forEach(
                p -> _executorService.submit(
                        new EvaluationExtractor(p.toString(), _termithIndex)
                ));

        _logger.info("Waiting EvaluationExtractorWorker executors to finish");
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }
}
