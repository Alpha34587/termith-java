package org.atilf.thread.enrichment;

import org.atilf.models.TermithIndex;
import org.atilf.models.enrichment.StandOffResources;
import org.atilf.module.enrichment.exporter.TeiWriter;
import org.atilf.thread.Thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The ExporterThread export the result of a the termithTreeTagger runner into a tei/standoff xml format
 * @author Simon Meoni
 *         Created on 19/09/16.
 */
public class ExporterThread extends Thread {

    /**
     * this constructor initialize the _termithIndex fields and initialize the _poolSize field with the default value
     * with the number of available processors.
     * @param termithIndex the termithIndex is an object that contains the results of the process*
     */
    protected ExporterThread(TermithIndex termithIndex){
        super(termithIndex, DEFAULT_POOL_SIZE);
    }

    /**
     * this constructor initialize all the needed fields. it initialize the termithIndex who contains the result of
     * process and initialize the size of the pool of _executorService field.
     * @param termithIndex the termithIndex is an object that contains the results of the process
     * @param poolSize the number of thread used during the process
     * @see TermithIndex
     * @see ExecutorService
     */
    public ExporterThread(TermithIndex termithIndex, int poolSize) {
        super(termithIndex,poolSize);
    }

    /**
     * this method export the result of process to the tei file format
     * @throws InterruptedException throws java concurrent executorService exception
     */
    public void execute() throws InterruptedException {
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
