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
    private long _elapsedTime;
    public TimePerformanceEvent(String name, int corpusSize ,List<TimePerformanceEvent> timePerformanceEvents) {
        super(name,corpusSize);
        _timePerformanceEvents = timePerformanceEvents;
        _startTime = System.nanoTime();
    }

    public void countElapsedTime(){
        _elapsedTime = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - _startTime);

        _logger.info(_name + " used  : " + _elapsedTime + "s");
        _timePerformanceEvents.add(this);
    }

    public long getElapsedTime() {
        return _elapsedTime;
    }

    @AllowConcurrentEvents
    @Subscribe
    public void recordTermithTimeEvent(TimePerformanceEvent e) {
        e.countElapsedTime();
    }
}
