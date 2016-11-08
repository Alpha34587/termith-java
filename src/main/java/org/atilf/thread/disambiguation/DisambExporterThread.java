package org.atilf.thread.disambiguation;

import org.atilf.models.termith.TermithIndex;
import org.atilf.module.tools.DisambiguationTeiWriter;
import org.atilf.module.tools.FilesUtils;
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
                p ->
                {
                    String file = FilesUtils.nameNormalizer(p.toString());
                    executor.submit(new DisambiguationTeiWriter(
                        file,
                        _termithIndex.getEvaluationLexic().get(file)
                        ));
                }

        );
        LOGGER.info("Waiting ContextExtractorWorker executors to finish");
        executor.shutdown();
        executor.awaitTermination(1L, TimeUnit.DAYS);
    }
}
