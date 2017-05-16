package org.atilf.delegate.disambiguation.contextLexicon;

import org.atilf.delegate.Delegate;
import org.atilf.module.disambiguation.contextLexicon.HeuristicsContextFilter;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Simon Meoni on 16/05/17.
 */
public class HeuristicsContextFilterDelegate extends Delegate {
    @Override
    protected void executeTasks() throws IOException, InterruptedException, ExecutionException {
        _executorService.submit(new HeuristicsContextFilter(_termithIndex));
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
        _logger.info("context filter step is finished");
    }
}
