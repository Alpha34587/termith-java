package org.atilf.observer;

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
    private String _name;


    public TermithObserver(long startTime, String name) {
        _startTime = startTime;
        _name = name;
    }

    @Override
    public void update(Observable observable, Object o) {

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
