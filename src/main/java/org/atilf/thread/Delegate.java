package org.atilf.thread;

import com.google.common.eventbus.EventBus;
import org.atilf.models.TermithIndex;
import org.atilf.monitor.observer.MemoryPerformanceEvent;
import org.atilf.monitor.observer.TimePerformanceEvent;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.atilf.runner.Runner.DEFAULT_POOL_SIZE;

/**
 * the abstract class Delegate is a main part of the workflow of the termith process. The runner classes call the
 * inherit Delegate classes linearly.
 * The executeTasks method is contains the multithreaded jobs (like the classes module inherited from the Runnable class)
 * who process the file corpus
 * @author Simon Meoni Created on 10/11/16.
 */
public abstract class Delegate implements JavaDelegate{
    protected final ExecutorService _executorService;
    protected final Logger _logger = LoggerFactory.getLogger(this.getClass().getName());
    protected TermithIndex _termithIndex;
    protected EventBus _eventBus = new EventBus();
    private TimePerformanceEvent _timePerformanceEvent;
    private MemoryPerformanceEvent _memoryPerformanceEvent;


    /**
     * this constructor initialize the _termithIndex fields and initialize the _poolSize field with the default value
     * with the number of available processors.
     * @param termithIndex the termithIndex is an object that contains the results of the process
     */
    protected Delegate(TermithIndex termithIndex){
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
    public Delegate(TermithIndex termithIndex, int poolSize) {
        _termithIndex = termithIndex;
        _executorService = Executors.newFixedThreadPool(poolSize);
        _timePerformanceEvent = new TimePerformanceEvent(
                this.getClass().getSimpleName(),
                _termithIndex.getCorpusSize(),
                _termithIndex.getTimePerformanceEvents()
        );
        _memoryPerformanceEvent = new MemoryPerformanceEvent(
                this.getClass().getSimpleName(),
                _termithIndex.getCorpusSize(),
                _termithIndex.getMemoryPerformanceEvents()
        );
        _eventBus.register(_timePerformanceEvent);
        _eventBus.register(_memoryPerformanceEvent);
    }

    protected Delegate() {
        _executorService = Executors.newFixedThreadPool(DEFAULT_POOL_SIZE);
    }

    /**
     * this method is used to executeTasks the different steps of processing of a thread
     * @throws IOException thrown a IO exception if a file is not found or have a permission problem during the
     * xsl transformation phase
     * @throws InterruptedException thrown if awaitTermination function is interrupted while waiting
     * @throws ExecutionException thrown a exception if a system process is interrupted
     */
    protected void executeTasks() throws IOException, InterruptedException, ExecutionException {}

    @Override
    public void execute(DelegateExecution execution) {
        try {
            executeTasks();
            _eventBus.post(_timePerformanceEvent);
            _eventBus.post(_memoryPerformanceEvent);
        } catch (IOException | InterruptedException | ExecutionException e) {
            _logger.error("there are some errors during execution of " + this.getClass().getName() + " :",e);
        }
    }
}
