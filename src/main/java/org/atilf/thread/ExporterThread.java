package org.atilf.thread;

import org.atilf.models.StandOffResources;
import org.atilf.models.TermithIndex;
import org.atilf.module.timer.ExporterTimer;
import org.atilf.worker.TeiWriterWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Simon Meoni
 *         Created on 19/09/16.
 */
public class ExporterThread {

    private TermithIndex _termithIndex;
    private ExecutorService _executor;
    private CountDownLatch _corpusCnt;
    private static final int DEFAULT_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private static final Logger LOGGER =  LoggerFactory.getLogger(ExporterThread.class.getName());

    /**
     * a constructor of initialize who take on parameter a folder path with xml files
     * @param termithIndex the input folder
     */
    public ExporterThread(TermithIndex termithIndex) throws IOException {
        this(DEFAULT_POOL_SIZE, termithIndex);
    }

    /**
     * this constructor can be specify the number of org.atilf.worker for this process
     * @param poolSize number of org.atilf.worker
     * @param termithIndex the input of folder
     */
    public ExporterThread(int poolSize, TermithIndex termithIndex) throws IOException {
        _termithIndex = termithIndex;
        _executor = Executors.newFixedThreadPool(poolSize);
    }

    public void execute() throws InterruptedException {
        new ExporterTimer(_termithIndex,LOGGER).start();
        StandOffResources standOffResources = new StandOffResources();
        _termithIndex.get_xmlCorpus().forEach(
                (key,value) -> _executor.submit(new TeiWriterWorker(key, _termithIndex,standOffResources))
        );
        LOGGER.info("Waiting executors to finish");
        _executor.shutdown();
        _executor.awaitTermination(1L, TimeUnit.DAYS);
    }
}
