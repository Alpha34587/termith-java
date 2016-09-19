package module.observer;

import module.tools.ProgressBar;

import java.util.Observable;
import java.util.Observer;

/**
 * @author Simon Meoni
 *         Created on 19/09/16.
 */
public class ProgressBarObserver implements Observer{
    ProgressBar progressBar;
    String message;

    public ProgressBarObserver(String message) {
        this.progressBar = new ProgressBar(message);
    }

    @Override
    public synchronized void update(Observable observable, Object o) {
        TermithObservable termithObservable = (TermithObservable) observable;
        progressBar.update(termithObservable.getCurrentDone(),
                termithObservable.getCurrentTotal(),
                termithObservable.getCurrentLogger());
    }
}
