package org.atilf.runner;

import org.atilf.models.termith.TermithIndex;
import org.atilf.thread.Thread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutionException;

/**
 * This class is used to create some Thread inherited classes and execute each of them linearly.
 * @author Simon Meoni Created on 10/11/16.
 */
public abstract class Runner {

    public static final int DEFAULT_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    protected final Logger _logger = LoggerFactory.getLogger(this.getClass().getName());
    protected TermithIndex _termithIndex;
    protected int _poolSize;

    /**
     * this constructor initializes the _termithIndex field.
     * @param termithIndex
     *         the termithIndex is an object that contains the results of the process
     */
    protected Runner(TermithIndex termithIndex) {
        this(termithIndex, Runner.DEFAULT_POOL_SIZE);
    }

    /**
     * this constructor initializes the _termithIndex field and the number of thread used during the process
     * @param termithIndex
     *         the termithIndex is an object that contains the results of the process
     * @param poolSize
     *         the number of thread used during the process
     */
    protected Runner(TermithIndex termithIndex, int poolSize) {
        _poolSize = poolSize;
        _termithIndex = termithIndex;
    }

    /**
     * this method contains the process chain. This method calls inherited thread classes.
     * @throws IOException Throws an IO exception if a file is not found or have a permission problem during process
     * @throws InterruptedException thrown if awaitTermination function is interrupted while waiting
     */
    public void execute() throws IOException, InterruptedException {
    }

    /**
     * this method construct an inherited org.atilf.thread.Thread object and call the execute method associated to this
     * object.
     * @param threadClass the inherited org.atilf.thread.Thread class of the object who need to instantiate
     * @param termithIndex the termithIndex for the _termithIndex field of org.atilf.thread.Thread
     * @param poolSize the poolSize for the _poolSize field of org.atilf.thread.Thread
     * @param <T> the class of inherited org.atilf.thread.Thread class
     */
    <T extends Thread> void executeThread(Class<T> threadClass, TermithIndex termithIndex, int poolSize)
            throws InterruptedException, ExecutionException, IOException {
        _logger.info("Started : " + threadClass.getSimpleName());
        try {
            T thread = threadClass.getConstructor(TermithIndex.class, int.class)
                    .newInstance(termithIndex, poolSize);
            thread.execute();

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            _logger.error("error during instantiation of object thread object : " + e);
        }
        finally {
            _logger.info("Finished : " + threadClass.getSimpleName());
        }

    }
}
