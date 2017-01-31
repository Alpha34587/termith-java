package org.atilf.monitor.observer;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;

/**
 * @author Simon Meoni Created on 31/01/17.
 */
public class MemoryPerformanceEvent extends TermithEvent {

    Runtime _rt = Runtime.getRuntime();
    private long _usedMemoryBefore;
    public MemoryPerformanceEvent(String name) {
        super(name);
        _usedMemoryBefore = _rt.totalMemory() - _rt.freeMemory();

    }

    @AllowConcurrentEvents
    @Subscribe
    public void recordTermithMemoryEvent(MemoryPerformanceEvent e) {
        e.usedMemory();
    }

    private void usedMemory() {
        long usedMemory = Math.abs(_usedMemoryBefore - (_rt.maxMemory() - _rt.freeMemory())) / (1024 * 1024);
        _logger.info(_name + " takes  : " + usedMemory + "Mo");
    }
}
