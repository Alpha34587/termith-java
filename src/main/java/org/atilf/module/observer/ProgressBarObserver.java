package org.atilf.module.observer;

import org.atilf.module.tools.ProgressBar;

import java.util.Observable;
import java.util.Observer;

/**
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
        _progressBar.update(termithObservable.get_currentDone(),
                termithObservable.get_currentTotal(),
                termithObservable.get_currentLogger());
    }
}
