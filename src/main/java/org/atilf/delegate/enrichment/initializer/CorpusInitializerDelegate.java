package org.atilf.delegate.enrichment.initializer;

import org.atilf.delegate.Delegate;
import org.atilf.module.enrichment.initializer.CorpusInitializer;
import org.flowable.engine.delegate.DelegateExecution;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

/**
 * Extract the plain text of an xml file and retained the xml corpus file path into a map
 * @author Simon Meoni
 * Created on 25/07/16.
 */
public class CorpusInitializerDelegate extends Delegate {

    private Path _outputPath;
    private Path _base;

    public void setOutputPath(Path outputPath) {
        _outputPath = outputPath;
    }

    public void setBase(Path base) {
        _base = base;
    }

    @Override
    public void initialize(DelegateExecution execution) {
        super.initialize(execution);
        _outputPath = getFlowableVariable("out",null);
        _base = getFlowableVariable("base",null);
    }

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
        Files.list(_base).filter(el -> el.toString().contains(".xml")).forEach(
                p -> _executorService.submit(new CorpusInitializer(p, _outputPath,_termithIndex))
        );
        _logger.info("Waiting initCorpusWorker executors to finish");
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
        _logger.info("initCorpusWorker finished");
    }

}
