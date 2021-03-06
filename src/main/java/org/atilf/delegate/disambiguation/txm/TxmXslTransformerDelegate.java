package org.atilf.delegate.disambiguation.txm;


import org.atilf.delegate.Delegate;
import org.atilf.resources.disambiguation.DisambiguationXslResources;
import org.atilf.resources.disambiguation.TxmXslResource;
import org.atilf.module.disambiguation.contextLexicon.DisambiguationXslTransformer;
import org.atilf.monitor.timer.TermithProgressTimer;
import org.flowable.engine.delegate.DelegateExecution;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.atilf.runner.TermithResourceManager.*;

/**
 * Created by Simon Meoni on 19/04/17.
 */
public class TxmXslTransformerDelegate extends Delegate {
    private Path _txmInputPath;
    private Path _outputPath;

    public void setTxmInputPath(Path txmInputPath) {
        _txmInputPath = txmInputPath;
    }

    public void setOutputPath(Path outputPath) {
        _outputPath = outputPath;
    }

    @Override
    public void initialize(DelegateExecution execution) {
        super.initialize(execution);
        _txmInputPath = getFlowableVariable("txmInputPath",null);
        _outputPath =  getFlowableVariable("out",null);

    }

    @Override
    public void executeTasks() throws IOException, InterruptedException, ExecutionException {
        DisambiguationXslResources _xslResources = new TxmXslResource(TermithResource.TXM_XSL.getPath());
        List<Future> _futures = new ArrayList<>();
        /*
        Transformation phase
         */
        Files.list(_txmInputPath).forEach(
                p -> _futures.add(_executorService.submit(new DisambiguationXslTransformer(
                                p.toFile(),
                                _termithIndex,
                                _xslResources,
                                _outputPath
                        ))
                )
        );
        new TermithProgressTimer(_futures,TxmXslTransformerDelegate.class,_executorService).start();
        _logger.info("Waiting ContextExtractor executors to finish");
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }
}
