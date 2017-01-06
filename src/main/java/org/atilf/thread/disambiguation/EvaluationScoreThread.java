package org.atilf.thread.disambiguation;

import org.atilf.models.disambiguation.DisambiguationXslResources;
import org.atilf.models.extractor.XslResources;
import org.atilf.models.termith.TermithIndex;
import org.atilf.module.disambiguation.*;
import org.atilf.thread.Thread;

import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author Simon Meoni Created on 15/12/16.
 */
public class EvaluationScoreThread extends Thread{
    private final CountDownLatch _transformCounter;
    private final CountDownLatch _aggregateCounter;
    private final CountDownLatch _scoreCounter;

    /**
     * this constructor initialize the _termithIndex fields and initialize the _poolSize field with the default value
     * with the number of available processors.
     *
     * @param termithIndex
     *         the termithIndex is an object that contains the results of the process
     */
    public EvaluationScoreThread(TermithIndex termithIndex) throws IOException {
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
     */
    public EvaluationScoreThread(TermithIndex termithIndex, int poolSize) throws IOException {

        super(termithIndex, poolSize);
        _transformCounter = new CountDownLatch((int) Files.list(TermithIndex.getOutputPath()).count());
        _aggregateCounter = new CountDownLatch((int) Files.list(TermithIndex.getOutputPath()).count());
        _scoreCounter = new CountDownLatch((int) Files.list(TermithIndex.getOutputPath()).count());
    }

    /**
     * this method is used to execute the different steps of processing of a thread
     *
     * @throws IOException
     *         thrown a IO exception if a file is not found or have a permission problem during the xsl transformation
     *         phase
     * @throws InterruptedException
     *         thrown if awaitTermination function is interrupted while waiting
     * @throws ExecutionException
     *         thrown a exception if a system process is interrupted
     */
    @Override
    public void execute() throws IOException, InterruptedException, ExecutionException {
        XslResources xslResources = new DisambiguationXslResources();
        Files.list(TermithIndex.getOutputPath()).forEach(
                p -> _executorService.submit(new DisambiguationXslTransformer(
                        p.toFile(),
                        _transformCounter,
                        _termithIndex.getTransformOutputDisambiguationFile(),
                        xslResources))
        );
        _transformCounter.await();

        _termithIndex.getEvaluationLexicon().forEach(
                (p,value) -> _executorService.submit(new AggregateTeiTerms(
                        _termithIndex.getTransformOutputDisambiguationFile().get(p).toString(),
                        value,
                        _termithIndex.getScoreTerms()))
        );
       _aggregateCounter.await();

       _termithIndex.getScoreTerms().forEach(
                (p,value) -> _executorService.submit(new ComputeTermsScore(p,value))
        );
        _scoreCounter.await();

        _executorService.submit(
                new ComputeTotalTermsScore(_termithIndex.getScoreTerms(),_termithIndex.getTotalTermScore())
        ).get();

        _executorService.submit(new ExportScoreToJson(_termithIndex.getScoreTerms(),_termithIndex.getTotalTermScore()));
        _executorService.submit(new ExportScoreToCsv(_termithIndex.getScoreTerms())).get();
        _executorService.submit(new ExportScoreToGraphJs());
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }
}
