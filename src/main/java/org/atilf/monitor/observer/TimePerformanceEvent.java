package org.atilf.monitor.observer;

import com.google.common.eventbus.Subscribe;

import java.util.concurrent.TimeUnit;

/**
 * @author Simon Meoni Created on 30/01/17.
 */
public class TimePerformanceEvent extends TermithEvent {
    private final long _startTime;

    public TimePerformanceEvent(String name) {
        super(name);
        _startTime = System.nanoTime();
    }

    public void countElapsedTime(){
        long elapsedTime = System.nanoTime() - _startTime;
        _logger.info(_name + " used  : " + TimeUnit.NANOSECONDS.toSeconds(elapsedTime) + "s");
    }

    @Subscribe
    public void recordTermithTimeEvent(TimePerformanceEvent e) {
        e.countElapsedTime();
    }
}
