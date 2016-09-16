package runner;

import models.TermithIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import worker.InitCorpusWorker;
import worker.TextExtractorWorker;

import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Simon Meoni
 *         Created on 16/09/16.
 */
public class TermithTreeTagger {
    private static final Logger LOGGER = LoggerFactory.getLogger(TermithTreeTagger.class.getName());
    private static final int DEFAULT_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private TermithIndex termithIndex;
    private ExecutorService executor;
    private CountDownLatch corpusCnt;

    public TermithTreeTagger(TermithIndex termithIndex) throws IOException {
        this(DEFAULT_POOL_SIZE, termithIndex);
    }


    public TermithTreeTagger(int poolSize,TermithIndex termithIndex) throws IOException {
        this.termithIndex = termithIndex;
        this.executor = Executors.newFixedThreadPool(poolSize);
        corpusCnt  = new CountDownLatch(
                (int)Files.list(termithIndex.getBase()).count());
    }

    public TermithIndex getTermithIndex() {
        return termithIndex;
    }

    public void execute() throws IOException, InterruptedException {

        int poolSize = Runtime.getRuntime().availableProcessors();
        LOGGER.info("Pool size set to: " + poolSize);
        LOGGER.info("Starting First Phase: Text extraction");
        Files.list(termithIndex.getBase()).forEach(
                p -> {
                    executor.submit(new TextExtractorWorker(p,termithIndex));
                    executor.submit(new InitCorpusWorker(p, termithIndex,corpusCnt));
                }

        );
        LOGGER.info("Waiting initCorpusWorker executors to finish");
        corpusCnt.await();
        LOGGER.info("initCorpusWorker finished");
        executor.shutdown();
        executor.awaitTermination(1L, TimeUnit.DAYS);

    }

}
