package org.atilf.delegate.tools;

import org.atilf.delegate.Delegate;
import org.atilf.module.tools.SpecialCharacterTranslator;
import org.flowable.engine.delegate.DelegateExecution;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

public class SpecialCharacterTranslatorDelegate extends Delegate {

    private Path _base;
    private Path _outputPath;

    public void setBase(Path base) {
        _base = base;
    }

    public void setOutputPath(Path outputPath) {
        _outputPath = outputPath;
    }

    @Override
    public void initialize(DelegateExecution execution) {
        super.initialize(execution);
        _base = getFlowableVariable("base",null);
        _outputPath = getFlowableVariable("output",null);
    }
    /**
     * executeTasks the extraction text task with the help of inner InitializerWorker class
     *
     * @throws IOException          throws exception if a file is not find
     * @throws InterruptedException throws java concurrent executorService exception
     */
    @Override
    public void executeTasks() throws IOException, InterruptedException {
        /*
        extract the text and map the path of the corpus into hashMap with identifier
         */
        Files.list(_base).filter(el -> el.toString().contains(".xml")).forEach(
                p -> _executorService.submit(new SpecialCharacterTranslator(p, _outputPath ,_termithIndex))
        );
        _logger.info("Waiting SpecialCharacterTranslator executors to finish");
        _logger.info("initCorpusWorker finished");
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }
}