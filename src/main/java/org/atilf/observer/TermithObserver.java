package org.atilf.observer;

import org.atilf.module.timer.ProgressBarTimer;

import java.text.NumberFormat;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.TimerTask;

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
    private List<ProgressBarTimer> _progressBarTimers;


    public TermithObserver(String name, List<ProgressBarTimer> progressBarTimers) {
        this(name);
        _progressBarTimers = progressBarTimers;
        _progressBarTimers.forEach(TimerTask::run);
    }

    public TermithObserver(String name) {
        _startTime = System.nanoTime();
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
