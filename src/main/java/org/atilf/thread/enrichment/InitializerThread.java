package org.atilf.thread.enrichment;

import org.atilf.models.termith.TermithIndex;
import org.atilf.module.timer.ExtractTextTimer;
import org.atilf.module.tools.InitializeCorpus;
import org.atilf.worker.TextExtractorWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private TermithIndex _termithIndex;
    private ExecutorService _executor;
    private CountDownLatch _corpusCnt;
    private static final Logger LOGGER = LoggerFactory.getLogger(InitializerThread.class.getName());
    private static final int DEFAULT_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    /**
     * a constructor of initialize who take on parameter a folder path with xml files
     * @param termithIndex the input folder
     */
    public InitializerThread(TermithIndex termithIndex) throws IOException {
        this(DEFAULT_POOL_SIZE, termithIndex);
    }

    /**
     * this constructor can be specify the number of org.atilf.worker for this process
     * @param poolSize number of org.atilf.worker
     * @param termithIndex the input of folder
     */
    public InitializerThread(int poolSize, TermithIndex termithIndex) throws IOException {
        _termithIndex = termithIndex;
        _executor = Executors.newFixedThreadPool(poolSize);
        _corpusCnt = new CountDownLatch(
                (int)Files.list(TermithIndex.getBase()).count());
    }

    /**
     * execute the extraction text task with the help of inner InitializerWorker class
     * @throws IOException throws exception if a file is not find
     * @throws InterruptedException throws java concurrent executorService exception
     */
    public void execute() throws IOException, InterruptedException {
        Files.list(TermithIndex.getBase()).forEach(
                p -> {
                    _executor.submit(new TextExtractorWorker(p, _termithIndex));
                    _executor.submit(new InitializeCorpus(p, _termithIndex, _corpusCnt));
                }

        );
        new ExtractTextTimer(_termithIndex,LOGGER).start();
        LOGGER.info("Waiting initCorpusWorker executors to finish");
        _corpusCnt.await();
        LOGGER.info("initCorpusWorker finished");
        _executor.shutdown();
        _executor.awaitTermination(1L, TimeUnit.DAYS);
    }

}
