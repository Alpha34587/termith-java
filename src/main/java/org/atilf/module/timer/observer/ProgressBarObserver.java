package org.atilf.module.timer.observer;

import java.util.Observable;
import java.util.Observer;

/**
 * ProgressBarObserver is a class that update _progressBar when ProgressBarObserver object is notified
 * @author Simon Meoni
 *         Created on 19/09/16.
 */
public class ProgressBarObserver implements Observer{
    private ProgressBar _progressBar;

    public ProgressBarObserver(String message) {
        _progressBar = new ProgressBar(message);
    }

    @Override
    public synchronized void update(Observable observable, Object o) {
        TermithObservable termithObservable = (TermithObservable) observable;
        _progressBar.update(termithObservable.getCurrentDone(),
                termithObservable.getCurrentTotal(),
                termithObservable.getCurrentLogger());
    }
}
