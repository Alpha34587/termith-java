package org.atilf.thread;

import org.atilf.models.TermithIndex;
import org.atilf.worker.CorpusGlobalWorker;
import org.atilf.worker.SubLexicWorker;
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
public class SubLexicThread {
    private final TermithIndex termithIndex;
    private final int poolSize;
    private static final Logger LOGGER = LoggerFactory.getLogger(SubLexicThread.class.getName());

    public SubLexicThread(TermithIndex termithIndex, int poolSize) {

        this.termithIndex = termithIndex;
        this.poolSize = poolSize;
    }

    public void execute() throws IOException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);
        Files.list(base).forEach(
                p -> {
                    executor.submit(new SubLexicWorker(p,termithIndex));
                    executor.submit(new CorpusGlobalWorker(p,termithIndex));
                }
        );
        LOGGER.info("Waiting SubLexicWorker executors to finish");
        executor.shutdown();
        executor.awaitTermination(1L, TimeUnit.DAYS);    }
}
