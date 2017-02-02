package org.atilf.thread;

import org.atilf.models.termith.TermithIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.atilf.runner.Runner.DEFAULT_POOL_SIZE;

/**
 * the abstract class Thread is a main part of the workflow of the termith process. The runner classes call the
 * inherit Thread classes linearly.
 * The execute method is contains the multithreaded jobs (like the classes module inherited from the Runnable class)
 * who process the file corpus
 * @author Simon Meoni Created on 10/11/16.
 */
public abstract class Thread {
    protected final ExecutorService _executorService;
    protected final Logger _logger = LoggerFactory.getLogger(this.getClass().getName());
    protected TermithIndex _termithIndex;

    /**
     * this constructor initialize the _termithIndex fields and initialize the _poolSize field with the default value
     * with the number of available processors.
     * @param termithIndex the termithIndex is an object that contains the results of the process
     */
    protected Thread(TermithIndex termithIndex){
        this(termithIndex, DEFAULT_POOL_SIZE);
    }

    /**
     * this constructor initialize all the needed fields. it initialize the termithIndex who contains the result of
     * process and initialize the size of the pool of _executorService field.
     * @param termithIndex the termithIndex is an object that contains the results of the process
     * @param poolSize the number of thread used during the process
     * @see TermithIndex
     * @see ExecutorService
     */
    public Thread(TermithIndex termithIndex, int poolSize) {
        _termithIndex = termithIndex;
        _executorService = Executors.newFixedThreadPool(poolSize);
    }

    protected Thread() {
        _executorService = Executors.newFixedThreadPool(DEFAULT_POOL_SIZE);
    }

    /**
     * this method is used to execute the different steps of processing of a thread
     * @throws IOException thrown a IO exception if a file is not found or have a permission problem during the
     * xsl transformation phase
     * @throws InterruptedException thrown if awaitTermination function is interrupted while waiting
     * @throws ExecutionException thrown a exception if a system process is interrupted
     */
    public void execute() throws IOException, InterruptedException, ExecutionException {}
}
