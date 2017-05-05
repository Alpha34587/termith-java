package org.atilf.delegate.tools;

import org.atilf.delegate.Delegate;
import org.atilf.module.tools.WorkingFilesCleaner;

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
    @Override
    public void executeTasks() throws InterruptedException {
        _executorService.submit(
                new WorkingFilesCleaner(
                        getFlowableVariable("out",null),
                        getFlowableVariable("learningPath",null),
                        getFlowableVariable("evaluationPath",null),
                        getFlowableVariable("txmInputPath",null),
                        getFlowableVariable("fileCleaner",true)
                )
        );
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }
}
