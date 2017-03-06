package org.atilf.thread.disambiguation.evaluation;

import org.atilf.models.TermithIndex;
import org.atilf.models.disambiguation.DisambiguationXslResources;
import org.atilf.module.disambiguation.contextLexicon.DisambiguationXslTransformer;
import org.atilf.module.disambiguation.evaluation.CommonWordsPosLemmaCleaner;
import org.atilf.module.disambiguation.evaluation.Evaluation;
import org.atilf.module.disambiguation.evaluation.EvaluationExtractor;
import org.atilf.module.disambiguation.evaluation.ThresholdLexiconCleaner;
import org.atilf.thread.Thread;

import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static org.atilf.runner.Runner.DEFAULT_POOL_SIZE;

/**
 * Evaluate the corpus with the lexicalProfile creates with the LexiconProfileThread
 * @author Simon Meoni
 *         Created on 12/10/16.
 */
public class EvaluationThread extends Thread{

    private CountDownLatch _transformCounter;
    private CountDownLatch _extractorCounter;
    private CountDownLatch _cleanerCounter;
    private CountDownLatch _commonCleanerCounter;

    /**
     * this constructor initialize the _termithIndex fields and initialize the _poolSize field with the default value
     * with the number of available processors.
     *
     * @param termithIndex
     *         the termithIndex is an object that contains the results of the process*
     */
    public EvaluationThread(TermithIndex termithIndex) {
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
    public EvaluationThread(TermithIndex termithIndex, int poolSize) {

        super(termithIndex, poolSize);
        try {
            _transformCounter = new CountDownLatch(
                    (int) Files.list(TermithIndex.getEvaluationPath()).count()
            );
            _extractorCounter = new CountDownLatch(
                    (int) Files.list(TermithIndex.getEvaluationPath()).count()
            );
            _cleanerCounter = new CountDownLatch(
                    _termithIndex.getContextLexicon().size());

            _commonCleanerCounter = new CountDownLatch(
                    _termithIndex.getContextLexicon().size());
        }
        catch (IOException e) {
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
        Threshold cleaner
         */
        _termithIndex.getContextLexicon().keySet().forEach(
                key -> _executorService.submit(new ThresholdLexiconCleaner(
                        key,
                        _termithIndex,
                        3,
                        15,
                        _cleanerCounter
                ))
        );
        _cleanerCounter.await();

        /*
        Common PosLemma cleaner
         */
        _termithIndex.getContextLexicon().forEach(
                (key,value) ->
                {
                    String lexOff = key.replace("On","Off");
                    if (key.contains("_lexOn") &&
                            _termithIndex.getContextLexicon().containsKey(lexOff)) {
                        _executorService.submit(new CommonWordsPosLemmaCleaner(
                                key,
                                value,
                                _termithIndex.getContextLexicon().get(lexOff),
                                _commonCleanerCounter
                        ));
                    }
                    else {
                        _commonCleanerCounter.countDown();
                    }
                }
        );
        _commonCleanerCounter.await();

        /*
        Transformation phase
         */
        Files.list(TermithIndex.getEvaluationPath()).forEach(
                p -> _executorService.submit(
                        new DisambiguationXslTransformer(
                                p.toFile(),
                                _termithIndex,
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
                p -> _executorService.submit(
                        new EvaluationExtractor(p.toString(), _termithIndex, _extractorCounter)
        ));

        _extractorCounter.await();
        /*
        Evaluation phase
         */
        _termithIndex.getEvaluationLexicon().forEach(
                (p,value) -> _executorService.submit(new Evaluation(p, _termithIndex))
        );
        _logger.info("Waiting EvaluationWorker executors to finish");
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }
}
