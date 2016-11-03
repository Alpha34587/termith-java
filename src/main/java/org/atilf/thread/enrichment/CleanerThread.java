package org.atilf.thread.enrichment;

import org.atilf.models.termith.TermithIndex;
import org.atilf.worker.CleanerWorker;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Simon Meoni
 *         Created on 27/09/16.
 */
public class CleanerThread {

    public CleanerThread() {}

    public void execute() throws InterruptedException {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(new CleanerWorker(TermithIndex.getOutputPath(),TermithIndex.is_keepFiles()));
        executor.shutdown();
        executor.awaitTermination(1L, TimeUnit.DAYS);
    }
}
