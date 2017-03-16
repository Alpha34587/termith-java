package org.atilf.delegate.enrichment.exporter;

import org.atilf.delegate.Delegate;
import org.atilf.models.enrichment.StandOffResources;
import org.atilf.module.enrichment.exporter.TeiWriter;

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

        /*
        export result
         */
        _termithIndex.getXmlCorpus().forEach(
                (key,value) -> _executorService.submit(new TeiWriter(key, _termithIndex,standOffResources))
        );
        _logger.info("Waiting executors to finish");
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }
}
