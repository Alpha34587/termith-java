package org.atilf.thread.enrichment;

import org.atilf.models.TermithIndex;
import org.atilf.models.enrichment.XslResources;
import org.atilf.module.enrichment.initializer.CorpusMapper;
import org.atilf.module.enrichment.initializer.TextExtractor;
import org.atilf.thread.Thread;

import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

/**
 * Extract the plain text of an xml file and retained the xml corpus file path into a map
 * @author Simon Meoni
 * Created on 25/07/16.
 */
public class InitializerThread extends Thread{

    /**
     * this constructor initialize the _termithIndex fields and initialize the _poolSize field with the default value
     * with the number of available processors.
     * @param termithIndex the termithIndex is an object that contains the results of the process
     * @param poolSize the number of thread used during the process
     */
    public InitializerThread(TermithIndex termithIndex, int poolSize) {
        super(termithIndex,poolSize);
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

        /*
        extract the text and map the path of the corpus into hashmap with identifier
         */
        Files.list(TermithIndex.getBase()).forEach(
                p -> {
                    _executorService.submit(new TextExtractor(p.toFile(), _termithIndex, xslResources));
                    _executorService.submit(new CorpusMapper(p, _termithIndex));
                }

        );
        _logger.info("Waiting initCorpusWorker executors to finish");
        _logger.info("initCorpusWorker finished");
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }

}
