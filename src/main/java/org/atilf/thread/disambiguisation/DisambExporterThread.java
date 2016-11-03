package org.atilf.thread.disambiguisation;

import org.atilf.models.termith.TermithIndex;
import org.atilf.worker.DisambExporterWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Simon Meoni
 *         Created on 25/10/16.
 */
public class DisambExporterThread {

    private final TermithIndex _termithIndex;
    private final int _poolSize;
    private static final Logger LOGGER = LoggerFactory.getLogger(DisambEvaluationThread.class.getName());

    public DisambExporterThread(TermithIndex termithIndex, int poolSize) {

        _termithIndex = termithIndex;
        _poolSize = poolSize;
    }

    public void execute() throws IOException, InterruptedException {

        ExecutorService executor = Executors.newFixedThreadPool(_poolSize);
        Files.list(TermithIndex.getBase()).forEach(
                p -> executor.submit(new DisambExporterWorker(p, _termithIndex))
        );
        LOGGER.info("Waiting SubLexicExtractorWorker executors to finish");
        executor.shutdown();
        executor.awaitTermination(1L, TimeUnit.DAYS);
    }
}
