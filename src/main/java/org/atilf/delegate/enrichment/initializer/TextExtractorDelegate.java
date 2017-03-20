package org.atilf.delegate.enrichment.initializer;

import org.atilf.delegate.Delegate;
import org.atilf.models.enrichment.XslResources;
import org.atilf.module.enrichment.initializer.TextExtractor;
import org.atilf.runner.Runner;
import org.flowable.engine.delegate.DelegateExecution;

import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

/**
 * Extract the plain text of an xml file and retained the xml corpus file path into a map
 * @author Simon Meoni
 * Created on 25/07/16.
 */
public class TextExtractorDelegate extends Delegate {

    /**
     * executeTasks the extraction text task with the help of inner InitializerWorker class
     * @throws IOException throws exception if a file is not find
     * @throws InterruptedException throws java concurrent executorService exception
     * @param execution
     */
    public void executeTasks(DelegateExecution execution) throws IOException, InterruptedException {
        /*
        initialize XslResource & ExtractTextTimer
         */
        XslResources xslResources = new XslResources();

        /*
        extract the text and map the path of the corpus into hashmap with identifier
         */
        Files.list(Runner.getBase()).forEach(
                p -> _executorService.submit(new TextExtractor(p.toFile(), _termithIndex, xslResources))

        );
        _logger.info("Waiting initCorpusWorker executors to finish");
        _logger.info("initCorpusWorker finished");
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }

}
