package org.atilf.thread.enrichment.cleaner;

import org.atilf.models.TermithIndex;
import org.atilf.module.enrichment.cleaner.WorkingFilesCleaner;
import org.atilf.thread.Delegate;

import java.util.concurrent.TimeUnit;

/**
 * clean the working directory file after a process
 * @author Simon Meoni
 *         Created on 27/09/16.
 */
public class WorkingFileCleanerDelegate extends Delegate {

    public WorkingFileCleanerDelegate(TermithIndex termithIndex, int poolSize) {
        super(termithIndex,poolSize);
    }

    /**
     * this method cleans the working directory of termith process. It remove all serialized java object remained and
     * the json and txt folder
     * @throws InterruptedException throws java concurrent executorService exception
     */
    public void executeTasks() throws InterruptedException {
        _executorService.submit(new WorkingFilesCleaner(TermithIndex.getOutputPath(),TermithIndex.isKeepFiles()));
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }
}
