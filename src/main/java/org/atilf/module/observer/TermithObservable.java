package org.atilf.module.observer;

import org.slf4j.Logger;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ConcurrentHashMap;

/**
 * the termith observable can monitor the progress of a treatment during a the execution of a Thread
 * (e.g initializer corpus)
 * @author Simon Meoni
 *         Created on 19/09/16.
 */
public class TermithObservable extends Observable {
    private int _currentDone;
    private int _currentTotal;
    private Logger _currentLogger;
    private Map<Logger, Observer> _observerMap;

    /**
     * empty constructor of TermithObservable
     */
    public TermithObservable() {
        /*
        initialize map of observer
         */
        _observerMap = new ConcurrentHashMap<>();
    }

    /**
     * change value of the termithObservable object and notify observer
     * @param done the number of treatment done during a task
     * @param total the size of the corpus
     * @param logger the name of the task
     */
    public synchronized void changeValue(int done, int total, Logger logger){
        _currentDone = done;
        _currentTotal = total;
        _currentLogger = logger;
        setChanged();
        notifyObservers(logger);
    }

    /**
     * getter of current log
     * @return the name of the task
     */
    Logger getCurrentLogger() {
        return _currentLogger;
    }

    /**
     * get the size of the total corpus
     * @return the size of the corpus
     */
    int getCurrentTotal() {
        return _currentTotal;
    }

    /**
     * get the number of treatment done during a task
     * @return the number of treatment done during a task
     */
    int getCurrentDone() { return _currentDone; }

    /**
     * link an Observer
     * @param observer the observer that we want to link
     * @param logger the name of the task that we want to monitor
     */
    public synchronized void addObserver(Observer observer, Logger logger) {
        super.addObserver(observer);
        _observerMap.put(logger,observer);
    }

    /**
     * remove an observer from observable object
     * @param logger the name of the task
     */
    public void deleteObserver(Logger logger){
        deleteObserver(_observerMap.get(logger));
    }

}
