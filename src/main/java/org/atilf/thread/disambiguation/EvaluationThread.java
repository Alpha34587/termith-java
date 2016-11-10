package org.atilf.thread.disambiguation;

import org.atilf.models.termith.TermithIndex;
import org.atilf.module.disambiguation.Evaluation;
import org.atilf.module.disambiguation.EvaluationExtractor;
import org.atilf.thread.Thread;

import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author Simon Meoni
 *         Created on 12/10/16.
 */
public class EvaluationThread extends Thread{

    /**
     * this constructor initialize the _termithIndex fields and initialize the _poolSize field with the default value
     * with the number of available processors.
     *
     * @param termithIndex
     *         the termithIndex is an object that contains the results of the process*
     */
    public EvaluationThread(TermithIndex termithIndex) {
        super(termithIndex);
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
    public EvaluationThread(TermithIndex termithIndex, int poolSize) {
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
        /*
        TODO problem : wait that the extraction is finished before executing the evaluation phase or extract and after
        evaluate. Or use a listener ?
         */

        /*
        Extraction phase
         */
        Files.list(TermithIndex.getBase()).forEach(
                p -> _executorService.submit(new EvaluationExtractor(p.toString(), _termithIndex))
        );

        /*
        Evaluation phase
         */
        _termithIndex.getEvaluationLexicon().forEach(
                (key,value) -> _executorService.submit(new Evaluation(value, _termithIndex.getContextLexicon()))
        );

        _logger.info("Waiting ContextExtractorWorker executors to finish");
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }
}
