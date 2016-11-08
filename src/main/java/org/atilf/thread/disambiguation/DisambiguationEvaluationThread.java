package org.atilf.thread.disambiguation;

import org.atilf.models.termith.TermithIndex;
import org.atilf.module.disambiguation.Evaluation;
import org.atilf.module.disambiguation.EvaluationExtractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Simon Meoni
 *         Created on 12/10/16.
 */
public class DisambiguationEvaluationThread {

    private final TermithIndex _termithIndex;
    private final int _poolSize;
    private static final Logger LOGGER = LoggerFactory.getLogger(DisambiguationEvaluationThread.class.getName());

    public DisambiguationEvaluationThread(TermithIndex termithIndex, int poolSize) {
        _termithIndex = termithIndex;
        _poolSize = poolSize;
    }

    public void execute() throws IOException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(_poolSize);
        Files.list(TermithIndex.getBase()).forEach(
                p -> executor.submit(new EvaluationExtractor(p.toString(), _termithIndex))
        );

        _termithIndex.getEvaluationLexicon().forEach(
                (key,value) -> executor.submit(new Evaluation(value, _termithIndex.getContextLexicon()))
        );

        LOGGER.info("Waiting ContextExtractorWorker executors to finish");
        executor.shutdown();
        executor.awaitTermination(1L, TimeUnit.DAYS);
    }
}
