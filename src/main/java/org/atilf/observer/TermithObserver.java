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

    private int _currentElementDone;
    private int _totalElement;

    @Override
    public void update(Observable observable, Object o) {

    }

}
