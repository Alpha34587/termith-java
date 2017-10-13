package org.atilf.delegate.enrichment.lexical.resource.projection;

import org.atilf.delegate.Delegate;
import org.atilf.resources.enrichment.LexicalResourceProjectionResources;
import org.atilf.module.enrichment.lexical.resource.projection.PhraseologyProjector;
import org.atilf.monitor.timer.TermithProgressTimer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.atilf.resources.enrichment.LexicalResourceProjectionResources.PH_TYPE;

/**
 * Created by Simon Meoni on 12/06/17.
 */
public class PhraseologyProjectorDelegate extends Delegate {
    @Override
    public void executeTasks() throws IOException, InterruptedException, ExecutionException {
        if ( getFlowableVariable("lang",null).equals("fr")) {
            LexicalResourceProjectionResources lexicalResourceProjectionResources = new LexicalResourceProjectionResources(
                    getFlowableVariable("lang", null), PH_TYPE
            );
            List<Future> futures = new ArrayList<>();
            _termithIndex.getMorphologyStandOff().forEach(
                    (id, value) -> {
                        _termithIndex.getPhraseoOffsetId().put(id, new ArrayList<>());
                        futures.add(_executorService.submit(new PhraseologyProjector(id, _termithIndex, lexicalResourceProjectionResources)));
                    }
            );
            _logger.info("waiting that all files are treated");
            new TermithProgressTimer(futures, PhraseologyProjectorDelegate.class, _executorService).start();
            _executorService.shutdown();
            _executorService.awaitTermination(1L, TimeUnit.DAYS);
            _logger.info("Phraseology projection step is finished");
        }
        else {
            _logger.info("this language is not support, the resource for this language not exists");
        }
    }
}
