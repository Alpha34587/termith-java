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

    protected static final int DEFAULT_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    protected final Logger _logger = LoggerFactory.getLogger(this.getClass().getName());
    protected TermithIndex _termithIndex;
    protected int _poolSize;

    /**
     * @param termithIndex
     */
    protected Runner(TermithIndex termithIndex) {
        this(termithIndex, Runner.DEFAULT_POOL_SIZE);
    }

    /**
     * @param termithIndex
     * @param poolSize
     */
    protected Runner(TermithIndex termithIndex, int poolSize) {
        _poolSize = poolSize;
        _termithIndex = termithIndex;
    }

    /**
     * @throws IOException
     * @throws InterruptedException
     */
    public void execute() throws IOException, InterruptedException {
    }
}
