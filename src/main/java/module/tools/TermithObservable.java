package module.tools;

import java.util.Observable;

/**
 * @author Simon Meoni
 *         Created on 19/09/16.
 */
public class TermithObservable extends Observable {
    private int currentDone;
    private int currentTotal;

    public TermithObservable() {
    }

    public synchronized void changeValue(int done, int total, ProgressBarObserver progressBarObserver){
        setCurrentDone(done);
        setCurrentTotal(total);
        setChanged();
        notifyObservers(progressBarObserver);
    }

    public void setCurrentDone(int currentDone) {
        this.currentDone = currentDone;
    }

    public void setCurrentTotal(int currentTotal) {
        this.currentTotal = currentTotal;
    }

    public int getCurrentTotal() {
        return currentTotal;
    }

    public int getCurrentDone() {

        return currentDone;
    }
}
