package org.atilf.runner;

import org.atilf.models.termith.TermithIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Simon Meoni Created on 10/11/16.
 * This class is used to create some Thread inherited classes and execute each of them linearly.
 */
public abstract class Runner {

    static final int DEFAULT_POOL_SIZE = Runtime.getRuntime().availableProcessors();
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
}
