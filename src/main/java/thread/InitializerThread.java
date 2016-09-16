package thread;

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
 * This is the first phase of the termith process. The objective is to extract the text in order to send it to
 * a termsuite process.
 * @author Simon Meoni
 * Created on 25/07/16.
 */
public class InitializerThread {

    private static final Logger LOGGER = LoggerFactory.getLogger(InitializerThread.class);
    private static final int DEFAULT_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    private TermithIndex termithIndex;
    private ExecutorService executor;
    private CountDownLatch corpusCnt;

    /**
     * a constructor of initialize who take on parameter a folder path with xml files
     * @param termithIndex the input folder
     */
    public InitializerThread(TermithIndex termithIndex) throws IOException {
        this(DEFAULT_POOL_SIZE, termithIndex);
    }

    /**
     * this constructor can be specify the number of worker for this process
     * @param poolSize number of worker
     * @param termithIndex the input of folder
     */
    public InitializerThread(int poolSize, TermithIndex termithIndex) throws IOException {
        this.termithIndex = termithIndex;
        this.executor = Executors.newFixedThreadPool(poolSize);
        corpusCnt  = new CountDownLatch(
                (int)Files.list(termithIndex.getBase()).count());
    }

    /**
     * execute the extraction text task with the help of inner InitializerWorker class
     * @throws IOException
     * @throws InterruptedException
     */
    public void execute() throws IOException, InterruptedException {
        Files.list(termithIndex.getBase()).forEach(
                p -> {
                    executor.submit(new TextExtractorWorker(p,termithIndex));
                    executor.submit(new InitCorpusWorker(p, termithIndex,corpusCnt));
                }

        );
        LOGGER.info("Waiting initCorpusWorker executors to finish");
        corpusCnt.await();
        LOGGER.info("Waiting initCorpusWorker executors to finish");
        corpusCnt.await();
        LOGGER.info("initCorpusWorker finished");
        executor.shutdown();
        executor.awaitTermination(1L, TimeUnit.DAYS);
    }

    public void addText(String id, StringBuffer content) {
        termithIndex.getExtractedText().put(id,content);
    }
}
