package org.atilf.module;

import org.atilf.models.TermithIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Simon Meoni Created on 19/01/17.
 */
public abstract class Module implements Runnable{
    protected final Logger _logger = LoggerFactory.getLogger(this.getClass().getName());
    protected TermithIndex _termithIndex;

    public Module(TermithIndex termithIndex){
        _termithIndex = termithIndex;
    }
    public Module(){}

    protected void execute(){}

    @Override
    public void run() {
        execute();
    }
}
