package org.atilf.thread.disambiguisation;

import org.atilf.models.termith.TermithIndex;
import org.atilf.worker.ContextExtractorWorker;
import org.atilf.worker.DisambXslTransformerWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Simon Meoni
 *         Created on 12/10/16.
 */
public class ContextLexicThread {

    private final TermithIndex _termithIndex;
    private final int _poolSize;
    private static final Logger LOGGER = LoggerFactory.getLogger(ContextLexicThread.class.getName());
    private CountDownLatch _transformCounter;
    public ContextLexicThread(TermithIndex termithIndex, int poolSize) {

        _termithIndex = termithIndex;
        _poolSize = poolSize;
        try {
            _transformCounter = new CountDownLatch(
                    (int) Files.list(TermithIndex.getBase()).count()
            );
        } catch (IOException e) {
            LOGGER.error("could not find folder : ",e);
        }

    }

    public void execute() throws IOException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(_poolSize);
        Files.list(TermithIndex.getBase()).forEach(
                p -> executor.submit(new DisambXslTransformerWorker(p, _termithIndex, _transformCounter))
        );
        _transformCounter.await();
        _termithIndex.getDisambTranformedFile().values().forEach(
                (file) -> {
                    executor.submit(new ContextExtractorWorker(file, _termithIndex));
//                    executor.submit(new LexicExtractorWorker(file, _termithIndex));
                }
        );
        LOGGER.info("Waiting ContextExtractorWorker executors to finish");
        executor.shutdown();
        executor.awaitTermination(1L, TimeUnit.DAYS);
        int a = 0;

    }
}
