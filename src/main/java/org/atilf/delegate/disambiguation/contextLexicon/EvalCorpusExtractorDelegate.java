package org.atilf.delegate.disambiguation.contextLexicon;

import org.atilf.delegate.Delegate;
import org.atilf.module.disambiguation.contextLexicon.EvalCorpusExtractor;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class EvalCorpusExtractorDelegate extends Delegate {

    @Override
    public void executeTasks() throws IOException, InterruptedException, ExecutionException {

        _executorService.submit(new EvalCorpusExtractor(_termithIndex)).get();
        _logger.info("Waiting ContextExtractor executors to finish");
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }
}
