package org.atilf.delegate.enrichment.exporter;

import org.atilf.delegate.Delegate;
import org.atilf.models.enrichment.StandOffResources;
import org.atilf.module.enrichment.exporter.TeiWriter;
import org.atilf.monitor.timer.TermithProgressTimer;
import org.atilf.runner.Runner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * The ExporterDelegate export the result of a the termithTreeTagger runner into a tei/standoff xml format
 * @author Simon Meoni
 *         Created on 19/09/16.
 */
public class ExporterDelegate extends Delegate {

    /**
     * this method export the result of process to the tei file format
     * @throws InterruptedException throws java concurrent executorService exception
     */
    public void executeTasks() throws InterruptedException {
        /*
        initialize standoff resource object
         */

        StandOffResources standOffResources = new StandOffResources();
        List<Future> futures = new ArrayList<>();
        /*
        export result
         */
        _termithIndex.getXmlCorpus().forEach(
                (key,value) -> futures.add(_executorService.submit(new TeiWriter(key, _termithIndex,standOffResources,
                        Runner.getOut().toString())))
        );
        new TermithProgressTimer(futures,this.getClass(),_executorService).start();
        _logger.info("Waiting exporters tasks to finish");
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }
}
