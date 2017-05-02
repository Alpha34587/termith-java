package org.atilf.delegate.enrichment.initializer;

import org.atilf.delegate.Delegate;
import org.atilf.module.enrichment.initializer.CorpusMapper;
import org.atilf.runner.Runner;

import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

/**
 * Extract the plain text of an xml file and retained the xml corpus file path into a map
 * @author Simon Meoni
 * Created on 25/07/16.
 */
public class CorpusMapperDelegate extends Delegate {

    /**
     * executeTasks the extraction text task with the help of inner InitializerWorker class
     * @throws IOException throws exception if a file is not find
     * @throws InterruptedException throws java concurrent executorService exception
     */
    @Override
    public void executeTasks() throws IOException, InterruptedException {
        /*
        extract the text and map the path of the corpus into hashMap with identifier
         */
        Files.list(Runner.getBase()).forEach(
                p -> _executorService.submit(new CorpusMapper(p, _termithIndex))

        );
        _logger.info("Waiting initCorpusWorker executors to finish");
        _logger.info("initCorpusWorker finished");
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }

}
