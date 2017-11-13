package org.atilf.delegate.enrichment.lexical.resource.projection;

import org.atilf.delegate.Delegate;
import org.atilf.module.enrichment.lexical.resource.projection.TransdisciplinaryLexiconsProjector;
import org.atilf.monitor.timer.TermithProgressTimer;
import org.atilf.resources.enrichment.ResourceProjection;
import org.atilf.resources.enrichment.TransdisciplinaryResourceProjection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.atilf.runner.TermithResourceManager.*;

/**
 * Created by Simon Meoni on 12/06/17.
 */
public class TransdisciplinaryLexiconsProjectorDelegate extends Delegate {
    @Override
    public void executeTasks() throws IOException, InterruptedException, ExecutionException {
        ResourceProjection resourceProjection = new TransdisciplinaryResourceProjection(TermithResource.LST.getPath());
        List<Future> futures = new ArrayList<>();
        _termithIndex.getMorphologyStandOff().forEach(
                (id,value) -> {
                    _termithIndex.getTransdisciplinaryOffsetId().put(id,new ArrayList<>());
                    futures.add(_executorService.submit(new TransdisciplinaryLexiconsProjector(id, _termithIndex,
                            resourceProjection)));
                }
        );
        _logger.info("waiting that all files are treated");
        new TermithProgressTimer(futures,TransdisciplinaryLexiconsProjectorDelegate.class,_executorService).start();
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
        _logger.info("Phraseology projection step is finished");
    }
}