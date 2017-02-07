package org.atilf.thread.disambiguation;

import org.atilf.models.TermithIndex;
import org.atilf.models.disambiguation.DisambiguationXslResources;
import org.atilf.models.enrichment.XslResources;
import org.atilf.module.disambiguation.contextLexicon.DisambiguationXslTransformer;
import org.atilf.module.disambiguation.evaluationScore.*;
import org.atilf.module.enrichment.cleaner.WorkingFilesCleaner;
import org.atilf.thread.Thread;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.atilf.runner.Runner.DEFAULT_POOL_SIZE;

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
     */
    public EvaluationScoreThread(TermithIndex termithIndex, int poolSize) throws IOException {

        super(termithIndex, poolSize);
        _transformCounter = new CountDownLatch(termithIndex.getEvaluationTransformedFiles().size());
        _aggregateCounter = new CountDownLatch(termithIndex.getEvaluationTransformedFiles().size());
        _scoreCounter = new CountDownLatch(termithIndex.getEvaluationTransformedFiles().size());
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
        _logger.info("transformation phase is started for EvaluationScoreThread");
        Path scoreFolder = Files.createDirectory(Paths.get(TermithIndex.getOutputPath().toString() + "/score/"));
        Files.list(TermithIndex.getOutputPath())
                .filter(p -> !Files.isDirectory(p))
                .filter(p -> p.toString().endsWith(".xml"))
                .forEach(
                        p -> _executorService.submit(new DisambiguationXslTransformer(
                                p.toFile(),
                                _termithIndex,
                                _transformCounter,
                                _termithIndex.getTransformOutputDisambiguationFile(),
                                xslResources,
                                scoreFolder)));
        _transformCounter.await();
        _logger.info("transformation phase is finished for EvaluationScoreThread");

        _logger.info("AggregateTeiTerms phase is started : retrieve all the evaluated terms candidate");
        _termithIndex.getEvaluationLexicon().forEach(
                (p,value) -> _executorService.submit(new AggregateTeiTerms(
                        _termithIndex.getTransformOutputDisambiguationFile().get(p).toString(),
                        value,
                        _termithIndex.getScoreTerms(),
                        _aggregateCounter))
        );
        _aggregateCounter.await();
        _logger.info("AggregateTeiTerms phase is finished");

        _logger.info("ComputeTermScore phase is started");
        _termithIndex.getScoreTerms().keySet().forEach(
                p -> _executorService.submit(new ComputeTermsScore(p,_termithIndex,_scoreCounter))
        );
        _scoreCounter.await();
        _logger.info("ComputeTermScore phase is finished");

        _logger.info("ComputeTotalTermsScore is started");
        _executorService.submit(
                new ComputeTotalTermsScore(_termithIndex)
        ).get();
        _logger.info("ComputeTotalTermsScore is finished");

        _logger.info("Export phase is started");
        _executorService.submit(new ExportScoreToCsv(_termithIndex,TermithIndex.getScorePath()));
        _executorService.submit(new ExportScoreToJson(_termithIndex, TermithIndex.getScorePath(), true)).get();
        _executorService.submit(new WorkingFilesCleaner(TermithIndex.getOutputPath(),false));
        _logger.info("Export phase is finished");
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }
}
