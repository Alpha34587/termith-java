package org.atilf.monitor.observer;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Simon Meoni Created on 30/01/17.
 */
public class TimePerformanceEvent extends TermithEvent {
    private final long _startTime;
    private final List<TimePerformanceEvent> _timePerformanceEvents;

    public TimePerformanceEvent(String name, List<TimePerformanceEvent> timePerformanceEvents) {
        super(name);
        _timePerformanceEvents = timePerformanceEvents;
        _startTime = System.nanoTime();
    }

    public void countElapsedTime(){
        long elapsedTime = System.nanoTime() - _startTime;
        _logger.info(_name + " used  : " + TimeUnit.NANOSECONDS.toSeconds(elapsedTime) + "s");
        _timePerformanceEvents.add(this);
    }
    @AllowConcurrentEvents
    @Subscribe
    public void recordTermithTimeEvent(TimePerformanceEvent e) {
        e.countElapsedTime();
    }
}
