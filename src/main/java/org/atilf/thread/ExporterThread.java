package org.atilf.thread;

import org.atilf.models.StandOffResources;
import org.atilf.models.TermithIndex;
import org.atilf.module.timer.ExporterTimer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.atilf.worker.TeiWriterWorker;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.atilf.module.tools.FilesUtilities.exportTerminology;
import static org.atilf.module.tools.FilesUtilities.readFile;

/**
 * @author Simon Meoni
 *         Created on 19/09/16.
 */
public class ExporterThread {

    private static final Logger LOGGER =  LoggerFactory.getLogger(ExporterThread.class.getName());
    private static final int DEFAULT_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    private TermithIndex termithIndex;
    private ExecutorService executor;
    private CountDownLatch corpusCnt;

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
        this.termithIndex = termithIndex;
        this.executor = Executors.newFixedThreadPool(poolSize);
    }

    public void execute() throws InterruptedException {
        new ExporterTimer(termithIndex,LOGGER).start();
        exportTerminology(termithIndex);
        StandOffResources standOffResources = new StandOffResources();
        termithIndex.getXmlCorpus().forEach(
                (key,value) -> executor.submit(new TeiWriterWorker(key,readFile(value),termithIndex,standOffResources))
        );
        LOGGER.info("Waiting executors to finish");
        executor.shutdown();
        executor.awaitTermination(1L, TimeUnit.DAYS);
    }
}
