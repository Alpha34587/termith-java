package org.atilf.thread.disambiguation;

import org.atilf.models.disambiguation.DisambiguationXslResources;
import org.atilf.models.termith.TermithIndex;
import org.atilf.module.disambiguation.DisambiguationXslTransformer;
import org.atilf.module.disambiguation.Evaluation;
import org.atilf.module.disambiguation.EvaluationExtractor;
import org.atilf.thread.Thread;

import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Evaluate the corpus with the lexicalProfile creates with the LexiconProfileThread
 * @author Simon Meoni
 *         Created on 12/10/16.
 */
public class EvaluationThread extends Thread{

    private CountDownLatch _transformCounter;
    private CountDownLatch _extactorCounter;
    /**
     * this constructor initialize the _termithIndex fields and initialize the _poolSize field with the default value
     * with the number of available processors.
     *
     * @param termithIndex
     *         the termithIndex is an object that contains the results of the process*
     */
    public EvaluationThread(TermithIndex termithIndex) {
        this(termithIndex,Thread.DEFAULT_POOL_SIZE);
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
        try {
            _transformCounter = new CountDownLatch(
                    (int) Files.list(TermithIndex.getEvaluationPath()).count()
            );
            _extactorCounter = new CountDownLatch(
                    (int) Files.list(TermithIndex.getEvaluationPath()).count()
            );
        } catch (IOException e) {
            _logger.error("could not find folder : ",e);
        }
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
        Transformation phase
         */
        Files.list(TermithIndex.getEvaluationPath()).forEach(
                p -> _executorService.submit(
                        new DisambiguationXslTransformer(
                        p.toFile(),
                        _transformCounter,
                        _termithIndex.getEvaluationTransformedFiles(),
                        xslResources)
                )
        );

        _transformCounter.await();
        /*
        Extraction phase
         */
        _termithIndex.getEvaluationTransformedFiles().values().forEach(
                p -> _executorService.submit(new EvaluationExtractor(p.toString(), _termithIndex,_extactorCounter))
        );

        _extactorCounter.await();
        /*
        Evaluation phase
         */
        _termithIndex.getEvaluationLexicon().forEach(
                (p,value) -> _executorService.submit(new Evaluation(p, value, _termithIndex.getContextLexicon()))
        );
        _logger.info("Waiting EvaluationWorker executors to finish");
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }
}
