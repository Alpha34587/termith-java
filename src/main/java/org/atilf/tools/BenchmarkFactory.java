package org.atilf.tools;

import org.atilf.monitor.observer.MemoryPerformanceEvent;
import org.atilf.monitor.observer.TermithEvent;
import org.atilf.monitor.observer.TimePerformanceEvent;

import java.util.List;

/**
 * @author Simon Meoni Created on 31/01/17.
 */
public class BenchmarkFactory {

    public static void export(List<? extends TermithEvent> termithEvents){
        Class<? extends TermithEvent> termithEventClass = termithEvents.get(0).getClass();
        if (termithEventClass == TimePerformanceEvent.class) {
            new TimeBenchmark();
        }
        else if (termithEventClass == MemoryPerformanceEvent.class){
            new MemoryBenchmark();

        }
    }

    static class TimeBenchmark {

    }

    static class MemoryBenchmark {

    }
}


