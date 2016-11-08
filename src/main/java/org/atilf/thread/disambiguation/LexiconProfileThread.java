package org.atilf.thread.disambiguation;

import org.atilf.models.disambiguation.RLexicon;
import org.atilf.models.termith.TermithIndex;
import org.atilf.module.disambiguation.SpecCoefficientInjector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Simon Meoni
 *         Created on 12/10/16.
 */
public class LexiconProfileThread {
    private final TermithIndex _termithIndex;
    private final int _poolSize;
    private static final Logger LOGGER = LoggerFactory.getLogger(LexiconProfileThread.class.getName());

    public LexiconProfileThread(TermithIndex termithIndex, int poolSize) {

        _termithIndex = termithIndex;
        _poolSize = poolSize;
    }

    public void execute() throws InterruptedException {
        RLexicon rLexicon = new RLexicon(_termithIndex.getGlobalLexicon());
        ExecutorService executor = Executors.newFixedThreadPool(_poolSize);

        _termithIndex.getContextLexicon().forEach(
                (key,value) -> executor.submit(new SpecCoefficientInjector(
                        key,
                        _termithIndex,
                        rLexicon))
        );

        LOGGER.info("Waiting ContextExtractorWorker executors to finish");
        executor.shutdown();
        executor.awaitTermination(1L, TimeUnit.DAYS);
    }
}
