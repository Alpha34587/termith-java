package org.atilf.thread.enrichment;

import org.atilf.models.extractor.XslResources;
import org.atilf.models.termith.TermithIndex;
import org.atilf.module.extractor.TextExtractor;
import org.atilf.module.timer.ExtractTextTimer;
import org.atilf.module.tools.CorpusMapper;
import org.atilf.thread.Thread;

import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @author Simon Meoni
 * Created on 25/07/16.
 */
public class InitializerThread extends Thread{

    private CountDownLatch _corpusCnt;

    /**
     * this constructor initialize the _termithIndex fields and initialize the _poolSize field with the default value
     * with the number of available processors.
     * @param termithIndex the termithIndex is an object that contains the results of the process
     * @param poolSize the number of thread used during the process
     */
    public InitializerThread(TermithIndex termithIndex, int poolSize) {
        super(termithIndex,poolSize);
        try {
            _corpusCnt = new CountDownLatch((int)Files.list(TermithIndex.getBase()).count());
        } catch (IOException e) {
            _logger.error("cannot find corpus folder", e);
        }
    }

    /**
     * this constructor initialize the _termithIndex fields and initialize the _poolSize field with the default value
     * with the number of available processors.
     * @param termithIndex the termithIndex is an object that contains the results of the process
     */
    public InitializerThread(TermithIndex termithIndex) {
        this(termithIndex,Thread.DEFAULT_POOL_SIZE);
    }

    /**
     * execute the extraction text task with the help of inner InitializerWorker class
     * @throws IOException throws exception if a file is not find
     * @throws InterruptedException throws java concurrent executorService exception
     */
    public void execute() throws IOException, InterruptedException {
        /*
        initialize XslResource & ExtractTextTimer
         */
        XslResources xslResources = new XslResources();
        new ExtractTextTimer(_termithIndex,_logger).start();

        /*
        extract the text and map the path of the corpus into hashmap with identifier
         */
        Files.list(TermithIndex.getBase()).forEach(
                p -> {
                    _executorService.submit(new TextExtractor(p.toFile(), _termithIndex, xslResources));
                    _executorService.submit(new CorpusMapper(p, _termithIndex, _corpusCnt));
                }

        );
        _logger.info("Waiting initCorpusWorker executors to finish");
        _corpusCnt.await();
        _logger.info("initCorpusWorker finished");
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }

}
