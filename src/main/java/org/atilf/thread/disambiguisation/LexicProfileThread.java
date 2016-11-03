package org.atilf.thread.disambiguisation;

import org.atilf.models.disambiguisation.RLexic;
import org.atilf.models.termith.TermithIndex;
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
    private final TermithIndex _termithIndex;
    private final int _poolSize;
    private static final Logger LOGGER = LoggerFactory.getLogger(LexicProfileThread.class.getName());

    public LexicProfileThread(TermithIndex termithIndex, int poolSize) {

        _termithIndex = termithIndex;
        _poolSize = poolSize;
    }

    public void execute() throws InterruptedException {
        RLexic rLexic = new RLexic(_termithIndex.getDisambGlobalLexic());
        ExecutorService executor = Executors.newFixedThreadPool(_poolSize);

        _termithIndex.getTermSubLexic().forEach(
                (key,value) -> executor.submit(new SpecCoeffInjectorWorker(
                        key,
                        _termithIndex,
                        rLexic))
        );

        LOGGER.info("Waiting ContextExtractorWorker executors to finish");
        executor.shutdown();
        executor.awaitTermination(1L, TimeUnit.DAYS);
    }
}
