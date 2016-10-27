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
        set_currentDone(done);
        set_currentTotal(total);
        set_currentLogger(logger);
        setChanged();
        notifyObservers(logger);
    }

    public Logger get_currentLogger() {
        return _currentLogger;
    }

    public int get_currentTotal() {
        return _currentTotal;
    }

    public int get_currentDone() { return _currentDone; }

    public void set_currentLogger(Logger _currentLogger) {
        this._currentLogger = _currentLogger;
    }

    public synchronized void set_currentDone(int _currentDone) {
        this._currentDone = _currentDone;
    }

    public synchronized void set_currentTotal(int _currentTotal) {
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
