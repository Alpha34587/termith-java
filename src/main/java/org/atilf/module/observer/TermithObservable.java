package org.atilf.module.observer;

import org.slf4j.Logger;

import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Simon Meoni
 *         Created on 19/09/16.
 */
public class TermithObservable extends Observable {
    private int _currentDone;
    private int _currentTotal;
    private Logger _currentLogger;
    private Map<Logger, Observer> _observerMap;
    public TermithObservable() {
        _observerMap = new ConcurrentHashMap<>();
    }

    public synchronized void changeValue(int done, int total, Logger logger){
        setCurrentDone(done);
        setCurrentTotal(total);
        setCurrentLogger(logger);
        setChanged();
        notifyObservers(logger);
    }

    public Logger getCurrentLogger() {
        return _currentLogger;
    }

    public int getCurrentTotal() {
        return _currentTotal;
    }

    public int getCurrentDone() { return _currentDone; }

    public void setCurrentLogger(Logger _currentLogger) {
        this._currentLogger = _currentLogger;
    }

    public synchronized void setCurrentDone(int _currentDone) {
        this._currentDone = _currentDone;
    }

    public synchronized void setCurrentTotal(int _currentTotal) {
        this._currentTotal = _currentTotal;
    }

    public synchronized void addObserver(Observer observer, Logger logger) {
        super.addObserver(observer);
        _observerMap.put(logger,observer);
    }

    public void deleteObserver(Logger logger){
        deleteObserver(_observerMap.get(logger));
    }

}
