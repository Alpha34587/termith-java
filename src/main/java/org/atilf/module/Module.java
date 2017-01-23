package org.atilf.module;

import org.atilf.models.termith.TermithIndex;
import org.atilf.monitor.observer.PerformanceObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Simon Meoni Created on 19/01/17.
 */
public abstract class Module implements Runnable{
    protected final Logger _logger = LoggerFactory.getLogger(this.getClass().getName());
    private PerformanceObserver _performanceObserver;
    protected TermithIndex _termithIndex;

    public Module(TermithIndex termithIndex){
        _termithIndex = termithIndex;
        _performanceObserver = new PerformanceObserver(
                this.getClass().getName()
        );
        _termithIndex.getTermithObservable().addObserver(_performanceObserver);
    }
    public Module(){}

    protected void execute(){}

    @Override
    public void run() {
        execute();
        _termithIndex.getTermithObservable().notifyObservers();
        _termithIndex.getTermithObservable().deleteObserver(_performanceObserver);
    }
}
