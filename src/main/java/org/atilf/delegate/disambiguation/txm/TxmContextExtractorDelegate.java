package org.atilf.delegate.disambiguation.txm;

import org.atilf.delegate.Delegate;
import org.atilf.module.disambiguation.txm.TxmContextExtractor;
import org.atilf.monitor.timer.TermithProgressTimer;
import org.flowable.engine.delegate.DelegateExecution;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by Simon Meoni on 19/04/17.
 */
public class TxmContextExtractorDelegate extends Delegate{

    private Path _outputPath;
    private int _windows;
    private String _annotation;

    public void setOutputPath(Path outputPath) {
        _outputPath = outputPath;
    }

    public void setWindows(int windows) {
        _windows = windows;
    }

    public void setAnnotation(String annotation) {
        _annotation = annotation;
    }

    @Override
    public void initialize(DelegateExecution execution) {
        super.initialize(execution);
        _outputPath = getFlowableVariable("out",null);
        _windows = getFlowableVariable("window",0);
        _annotation = getFlowableVariable("annotation","");

    }

    @Override
    public void executeTasks() throws IOException, InterruptedException {

        List<String> includeElement = new ArrayList<>();
        includeElement.add("p");
        includeElement.add("head");
        includeElement.add("cit");
        includeElement.add("note");
        includeElement.add("q");

        List<Future> futures = new ArrayList<>();
        Files.list(_outputPath).forEach(
                file -> futures.add(_executorService.submit(
                        new TxmContextExtractor(file.toString(),
                                _termithIndex.getTermsTxmContext(),
                                _windows,
                                _annotation,
                                includeElement
                        ))
                )
        );
        new TermithProgressTimer(futures,this.getClass(),_executorService).start();
        _logger.info("Waiting ContextExtractor executors to finish");
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }
}
