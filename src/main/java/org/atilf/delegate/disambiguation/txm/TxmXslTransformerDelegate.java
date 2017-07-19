package org.atilf.delegate.disambiguation.txm;


import org.atilf.delegate.Delegate;
import org.atilf.resources.disambiguation.DisambiguationXslResources;
import org.atilf.resources.disambiguation.TxmXslResource;
import org.atilf.module.disambiguation.contextLexicon.DisambiguationXslTransformer;
import org.atilf.monitor.timer.TermithProgressTimer;

import java.io.IOException;
import java.nio.file.Files;
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
    @Override
    protected void executeTasks() throws IOException, InterruptedException, ExecutionException {
        DisambiguationXslResources xslResources = new TxmXslResource(TermithResource.TXM_XSL.getPath());
        List<Future> futures = new ArrayList<>();
        /*
        Transformation phase
         */
        Files.list(getFlowableVariable("txmInputPath",null)).forEach(
                p -> futures.add(_executorService.submit(new DisambiguationXslTransformer(
                                p.toFile(),
                                _termithIndex,
                                xslResources,
                                getFlowableVariable("out",null)
                        ))
                )
        );
        new TermithProgressTimer(futures,TxmXslTransformerDelegate.class,_executorService).start();
        _logger.info("Waiting ContextExtractor executors to finish");
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }
}
