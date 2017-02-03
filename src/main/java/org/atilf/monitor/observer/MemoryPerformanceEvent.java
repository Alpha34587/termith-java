package org.atilf.monitor.observer;

import com.google.common.eventbus.AllowConcurrentEvents;
import com.google.common.eventbus.Subscribe;

import java.util.List;

/**
 * @author Simon Meoni Created on 31/01/17.
 */
public class MemoryPerformanceEvent extends TermithEvent {

    private final List<MemoryPerformanceEvent> _memoryPerformanceEvents;
    private final long _usedMemoryBefore;
    private long _usedMemory;
    private final Runtime _rt = Runtime.getRuntime();

    public MemoryPerformanceEvent(String name, int corpusSize, List<MemoryPerformanceEvent> memoryPerformanceEvents) {
        super(name,corpusSize);
        _memoryPerformanceEvents = memoryPerformanceEvents;
        _usedMemoryBefore = _rt.totalMemory() - _rt.freeMemory();

    }

    @AllowConcurrentEvents
    @Subscribe
    public void recordTermithMemoryEvent(MemoryPerformanceEvent e) {
        e.usedMemory();
    }

    private void usedMemory() {
        _usedMemory = Math.abs(_usedMemoryBefore - (_rt.maxMemory() - _rt.freeMemory())) / (1024 * 1024);
        _logger.info(_name + " takes  : " + _usedMemory + "Mo");
        _memoryPerformanceEvents.add(this);
    }

    public long getUsedMemory() {
        return _usedMemory;
    }
}
