package org.atilf.delegate.disambiguation.txm;


import org.atilf.delegate.Delegate;
import org.atilf.models.disambiguation.DisambiguationXslResources;
import org.atilf.models.disambiguation.TxmXslResource;
import org.atilf.module.disambiguation.contextLexicon.DisambiguationXslTransformer;
import org.atilf.monitor.timer.TermithProgressTimer;
import org.atilf.runner.Runner;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by smeoni on 19/04/17.
 */
public class TxmXslTransformerDelegate extends Delegate {
    @Override
    protected void executeTasks() throws IOException, InterruptedException, ExecutionException {
        DisambiguationXslResources xslResources = new TxmXslResource();
        List<Future> futures = new ArrayList<>();
        /*
        Transformation phase
         */
        Files.list(Runner.getTxmInputPath()).forEach(
                p -> futures.add(_executorService.submit(new DisambiguationXslTransformer(
                                p.toFile(),
                                _termithIndex,
                                xslResources,
                                Runner.getOut()
                        ))
                )
        );
        new TermithProgressTimer(futures,TxmXslTransformerDelegate.class,_executorService).start();
        _logger.info("Waiting ContextExtractor executors to finish");
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }
}
