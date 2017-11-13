package org.atilf.delegate.enrichment.initializer;

import org.atilf.delegate.Delegate;
import org.atilf.resources.enrichment.XslResources;
import org.atilf.module.enrichment.initializer.TextExtractor;
import org.atilf.monitor.timer.TermithProgressTimer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.atilf.runner.TermithResourceManager.*;

/**
 * Extract the plain text of an xml file and retained the xml corpus file path into a map
 * @author Simon Meoni
 * Created on 25/07/16.
 */
public class TextExtractorDelegate extends Delegate {


    private Path _base = getFlowableVariable("base",null);
    private Path _output = getFlowableVariable("out",null);


    public void setBase(Path base) {
        _base = base;
    }

    public void setOutput(Path output) {
        _output = output;
    }

    /**
     * executeTasks the extraction text task with the help of inner InitializerWorker class
     * @throws IOException throws exception if a file is not find
     * @throws InterruptedException throws java concurrent executorService exception
     */
    @Override
    public void executeTasks() throws IOException, InterruptedException {
        /*
        initialize XslResource & ExtractTextTimer
         */
        XslResources xslResources = new XslResources(TermithResource.TEXT_XSL.getPath());
        List<Future> futures = new ArrayList<>();
        /*
        extract the text and map the path of the corpus into hashMap with identifier
         */
        Files.list(_base).forEach(
                p -> futures.add(_executorService.submit(new TextExtractor(p.toFile(), _termithIndex,
                        _output,xslResources)))
        );
        new TermithProgressTimer(futures,this.getClass(),_executorService).start();
        _logger.info("Waiting initCorpusWorker executors to finish");
        _logger.info("initCorpusWorker finished");
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }

}
