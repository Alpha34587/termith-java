package org.atilf.runner;

import org.atilf.models.termith.TermithIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by simon on 13/11/16.
 */
public abstract class Runner {
    protected static final int DEFAULT_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    protected final Logger _logger = LoggerFactory.getLogger(this.getClass().getName());
    protected TermithIndex _termithIndex;
    protected int _poolSize;
}
