package org.atilf.delegate;

import com.google.common.eventbus.EventBus;
import org.atilf.models.TermithIndex;
import org.atilf.monitor.observer.MemoryPerformanceEvent;
import org.atilf.monitor.observer.TimePerformanceEvent;
import org.flowable.engine.delegate.DelegateExecution;
import org.flowable.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * the abstract class Delegate is a main part of the workflow of the termith process. The runner classes call the
 * inherit Delegate classes linearly.
 * The executeTasks method is contains the multithreaded jobs (like the classes module inherited from the Runnable class)
 * who process the file corpus
 * @author Simon Meoni Created on 10/11/16.
 */
public abstract class Delegate implements JavaDelegate{
    private EventBus _eventBus = new EventBus();
    private DelegateExecution _execution;
    protected TermithIndex _termithIndex;
    protected ExecutorService _executorService;
    private TimePerformanceEvent _timePerformanceEvent;
    private MemoryPerformanceEvent _memoryPerformanceEvent;
    protected final Logger _logger = LoggerFactory.getLogger(this.getClass().getName());

    /**
     * this method is used to executeTasks the different steps of processing of a delegate
     * @throws IOException thrown a IO exception if a file is not found or have a permission problem during the
     * xsl transformation phase
     * @throws InterruptedException thrown if awaitTermination function is interrupted while waiting
     * @throws ExecutionException thrown a exception if a system process is interrupted
     */
    protected void executeTasks() throws IOException, InterruptedException, ExecutionException {}

    @Override
    public void execute(DelegateExecution execution) {
        try {
            initialize(execution);
            executeTasks();
            _eventBus.post(_timePerformanceEvent);
            _eventBus.post(_memoryPerformanceEvent);
        } catch (IOException | InterruptedException | ExecutionException e) {
            _logger.error("there are some errors during execution of " + this.getClass().getName() + " :",e);
        }
    }

    public void initialize(DelegateExecution execution){



        int poolSize = getFlowableVariable("poolSize",0);
        List<MemoryPerformanceEvent> memoryPerformanceEvents =
                getFlowableVariable("memoryPerformanceEvents",null);
        List<TimePerformanceEvent> timePerformanceEvents =
                getFlowableVariable("timePerformanceEvents",null);
        int corpusSize = getFlowableVariable("corpusSize",0);

        _execution = execution;
        _termithIndex = getFlowableVariable("termithIndex",null);
        _executorService = Executors.newFixedThreadPool(poolSize);

        _timePerformanceEvent = new TimePerformanceEvent(
                this.getClass().getSimpleName(),
                corpusSize,
                timePerformanceEvents
        );
        _memoryPerformanceEvent = new MemoryPerformanceEvent(
                this.getClass().getSimpleName(),
                corpusSize,
                memoryPerformanceEvents
        );

        _eventBus.register(_timePerformanceEvent);
        _eventBus.register(_memoryPerformanceEvent);
    }

    protected <T extends Object> T getFlowableVariable(String flowableName, T defaultValue){

        T getVar = (T) _execution.getVariable(flowableName);

        if (_execution.getVariable(flowableName) != null) {
            return getVar;
        } else {
            return defaultValue;
        }
    }
}
