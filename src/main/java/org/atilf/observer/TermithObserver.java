package org.atilf.observer;

import org.atilf.module.timer.ProgressBarTimer;

import java.text.NumberFormat;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Simon Meoni Created on 19/01/17.
 */
public class TermithObserver implements Observer {

    private long _elapsedTime;
    private long _startTime;
    private long _endTime;
    private long _allocatedMemory;
    private long _freeMemory;
    private String _name;
    private NumberFormat _format = NumberFormat.getInstance();
    private Runtime _runtime = Runtime.getRuntime();
    private ProgressBarTimer _progressBarTimer = null;


    public TermithObserver(long startTime, String name, ProgressBarTimer progressBarTimer) {
        _startTime = startTime;
        _name = name;
        _progressBarTimer = progressBarTimer;
        progressBarTimer.run();
    }

    public TermithObserver(long startTime, String name) {
        _startTime = startTime;
        _name = name;
    }

    @Override
    public void update(Observable observable, Object o) {
         _allocatedMemory = _runtime.totalMemory() / 1024;
    }

    public long getElapsedTime() {
        return _elapsedTime;
    }

    public long getAllocatedMemory() {
        return _allocatedMemory;
    }

    public String getName() {
        return _name;
    }
}
