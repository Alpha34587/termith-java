package org.atilf.thread;

import org.atilf.models.TermithIndex;
import org.atilf.worker.CleanerWorker;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Simon Meoni
 *         Created on 27/09/16.
 */
public class CleanerThread {

    public CleanerThread() {}

    public void execute() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(new CleanerWorker(TermithIndex.outputPath,TermithIndex.keepFiles));
    }
}
