package module.tools;

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
    private int currentDone;
    private int currentTotal;
    private Logger currentLogger;
    private Map<Logger, Observer> observerMap;
    public TermithObservable() {
        observerMap = new ConcurrentHashMap<>();
    }

    public synchronized void changeValue(int done, int total, Logger logger){
        setCurrentDone(done);
        setCurrentTotal(total);
        setCurrentLogger(logger);
        setChanged();
        notifyObservers(observerMap.get(logger));
    }

    public Logger getCurrentLogger() {
        return currentLogger;
    }

    public void setCurrentLogger(Logger currentLogger) {
        this.currentLogger = currentLogger;
    }

    public synchronized void setCurrentDone(int currentDone) {
        this.currentDone = currentDone;
    }

    public synchronized void setCurrentTotal(int currentTotal) {
        this.currentTotal = currentTotal;
    }

    public int getCurrentTotal() {
        return currentTotal;
    }

    public int getCurrentDone() {

        return currentDone;
    }

    public synchronized void addObserver(Observer observer, Logger logger) {
        super.addObserver(observer);
        observerMap.put(logger,observer);
    }

}
