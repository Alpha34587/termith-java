package org.atilf.thread.disambiguation;

import org.atilf.models.disambiguation.DisambiguationXslResources;
import org.atilf.models.termith.TermithIndex;
import org.atilf.module.disambiguation.ContextExtractor;
import org.atilf.module.disambiguation.DisambiguationXslTransformer;
import org.atilf.module.disambiguation.LexiconExtractor;
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
public class ContextLexiconThread {

    private CountDownLatch _transformCounter;
    private final TermithIndex _termithIndex;
    private final int _poolSize;
    private static final Logger LOGGER = LoggerFactory.getLogger(ContextLexiconThread.class.getName());

    public ContextLexiconThread(TermithIndex termithIndex, int poolSize) {

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
        DisambiguationXslResources xslResources = new DisambiguationXslResources();
        Files.list(TermithIndex.getBase()).forEach(
                p -> executor.submit(new DisambiguationXslTransformer(
                        p.toFile(),
                        _transformCounter,
                        _termithIndex,
                        xslResources)
                )
        );
        _transformCounter.await();
        _termithIndex.getDisambiguationTransformedFile().values().forEach(
                (file) -> {
                    executor.submit(new ContextExtractor(file.toString(), _termithIndex.getContextLexicon()));
                    executor.submit(new LexiconExtractor(file.toString(), _termithIndex.getGlobalLexicon()));
                }
        );
        LOGGER.info("Waiting ContextExtractorWorker executors to finish");
        executor.shutdown();
        executor.awaitTermination(1L, TimeUnit.DAYS);
    }
}
