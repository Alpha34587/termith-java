package org.atilf.delegate.enrichment.LexicalResourceProjection;

import org.atilf.delegate.Delegate;
import org.atilf.models.enrichment.LexicalResourceProjectionResources;
import org.atilf.module.enrichment.lexicalResourceProjection.TransdisciplinaryLexicsProjector;
import org.atilf.monitor.timer.TermithProgressTimer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.atilf.models.enrichment.LexicalResourceProjectionResources.LST_TYPE;

/**
 * Created by Simon Meoni on 12/06/17.
 */
public class TransdisciplinaryLexicsProjectorDelegate extends Delegate {
    @Override
    protected void executeTasks() throws IOException, InterruptedException, ExecutionException {

        LexicalResourceProjectionResources lexicalResourceProjectionResources =
                new LexicalResourceProjectionResources(
                        getFlowableVariable("lang",null),
                        LST_TYPE
                );

        List<Future> futures = new ArrayList<>();
        _termithIndex.getMorphologyStandOff().forEach(
                (id,value) -> futures.add(_executorService.submit(
                        new TransdisciplinaryLexicsProjector(id, _termithIndex, lexicalResourceProjectionResources))
                )
        );
        _logger.info("waiting that all files are treated");
        new TermithProgressTimer(futures,TransdisciplinaryLexicsProjectorDelegate.class,_executorService).start();
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
        _logger.info("Phraseology projection step is finished");

    }
}
