package org.atilf.delegate.enrichment.cleaner;

import org.atilf.delegate.Delegate;
import org.atilf.module.enrichment.cleaner.WorkingFilesCleaner;
import org.atilf.runner.Runner;

import java.util.concurrent.TimeUnit;

/**
 * clean the working directory file after a process
 * @author Simon Meoni
 *         Created on 27/09/16.
 */
public class WorkingFileCleanerDelegate extends Delegate {

    /**
     * this method cleans the working directory of termith process. It remove all serialized java object remained and
     * the json and txt folder
     * @throws InterruptedException throws java concurrent executorService exception
     */
    public void executeTasks() throws InterruptedException {
        _executorService.submit(new WorkingFilesCleaner(Runner.getOut(),true));
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }
}
