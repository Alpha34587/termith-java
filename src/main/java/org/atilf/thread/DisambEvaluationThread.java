package org.atilf.thread;

import org.atilf.models.TermithIndex;
import org.atilf.worker.EvaluationExtractorWorker;
import org.atilf.worker.EvaluationWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.atilf.models.TermithIndex.base;

/**
 * @author Simon Meoni
 *         Created on 12/10/16.
 */
public class DisambEvaluationThread {

    private final TermithIndex termithIndex;
    private final int poolSize;
    private static final Logger LOGGER = LoggerFactory.getLogger(DisambEvaluationThread.class.getName());

    public DisambEvaluationThread(TermithIndex termithIndex, int poolSize) {

        this.termithIndex = termithIndex;
        this.poolSize = poolSize;
    }

    public void execute() throws IOException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);
        Files.list(base).forEach(
                p -> executor.submit(new EvaluationExtractorWorker(p,termithIndex))
        );

        termithIndex.getEvaluationLexic().forEach(
                (key,value) -> executor.submit(new EvaluationWorker(value,termithIndex))
        );

        LOGGER.info("Waiting SubLexicExtractorWorker executors to finish");
        executor.shutdown();
        executor.awaitTermination(1L, TimeUnit.DAYS);
    }
}
