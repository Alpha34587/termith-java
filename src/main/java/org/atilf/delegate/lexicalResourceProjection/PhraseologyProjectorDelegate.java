package org.atilf.delegate.lexicalResourceProjection;

import org.atilf.delegate.Delegate;
import org.atilf.module.enrichment.analyzer.TreeTaggerWorker;
import org.atilf.module.enrichment.lexicalResourceProjection.PhraseologyProjector;
import org.atilf.monitor.timer.TermithProgressTimer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by Simon Meoni on 12/06/17.
 */
public class PhraseologyProjectorDelegate extends Delegate {
    @Override
    protected void executeTasks() throws IOException, InterruptedException, ExecutionException {

        List<Future> futures = new ArrayList<>();
        _termithIndex.getMorphologyStandOff().forEach(
                (id,value) -> _executorService.submit(
                        new PhraseologyProjector(id,_termithIndex)
                )
        );
        _logger.info("waiting that all files are treated");
        new TermithProgressTimer(futures,TreeTaggerWorker.class,_executorService).start();
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
        _logger.info("Phraseology projection step is finished");

    }
}
