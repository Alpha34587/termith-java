package org.atilf.thread;

import org.atilf.models.RLexic;
import org.atilf.models.TermithIndex;
import org.atilf.worker.SpecCoeffInjectorWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Simon Meoni
 *         Created on 12/10/16.
 */
public class LexicProfileThread {
    private final TermithIndex termithIndex;
    private final int poolSize;
    private static final Logger LOGGER = LoggerFactory.getLogger(LexicProfileThread.class.getName());

    public LexicProfileThread(TermithIndex termithIndex, int poolSize) {

        this.termithIndex = termithIndex;
        this.poolSize = poolSize;
    }

    public void execute() throws InterruptedException {
        RLexic rLexic = new RLexic(termithIndex.getDisambGlobalLexic());
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);

        termithIndex.getTermSubLexic().forEach(
                (key,value) -> {
                    executor.submit(new SpecCoeffInjectorWorker(
                            key,
                            termithIndex,
                            rLexic));
                }
        );

        LOGGER.info("Waiting SubLexicExtractorWorker executors to finish");
        executor.shutdown();
        executor.awaitTermination(1L, TimeUnit.DAYS);
    }
}
