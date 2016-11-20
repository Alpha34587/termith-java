package org.atilf.benchmark;

import org.atilf.models.termith.TermithIndex;
import org.atilf.thread.Thread;
import org.atilf.thread.disambiguation.ContextLexiconThread;
import org.atilf.thread.disambiguation.DisambiguationExporterThread;
import org.atilf.thread.disambiguation.EvaluationThread;
import org.atilf.thread.disambiguation.LexiconProfileThread;
import org.junit.After;
import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;


/**
 * Created by simon on 18/11/16.
 */

public class BenchmarkTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BenchmarkTest.class.getName());
    public static TermithIndex _termithIndex;
    public ThreadMXBean _threadMXBean = ManagementFactory.getThreadMXBean();
    private HashMap<String, Long> timeHashMap = new HashMap<>();

    @Benchmark
    public void ContextLexiconThread() throws NoSuchMethodException {
        ThreadTime(ContextLexiconThread.class);
    }

    @Benchmark
    public void LexiconProfileThread() throws NoSuchMethodException {
        ThreadTime(LexiconProfileThread.class);

    }

    @Benchmark
    public void EvaluationThread() throws NoSuchMethodException {
        ThreadTime(EvaluationThread.class);
    }

    @Test
    public void DisambiguationExporter() throws NoSuchMethodException {
        ThreadTime(DisambiguationExporterThread.class);

    }

    @After
    public <T> void printBenchmark(long elapsedTime, Class<T> tClass) {
        timeHashMap.forEach(
                (key, value) -> LOGGER.info(tClass.getName() + ":\t" + elapsedTime + "ms")
        );
    }

    public <T extends Thread> void ThreadTime(Class<T> threadClass) throws NoSuchMethodException {
        long startTime = _threadMXBean.getThreadCpuTime(java.lang.Thread.currentThread().getId());
        try {
            threadClass.getConstructor(TermithIndex.class, int.class).newInstance(_termithIndex, 8).execute();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException
                e) {
            LOGGER.error("cannot execute thread " + threadClass + " : " + e);
        } catch (InterruptedException | ExecutionException | IOException e) {
            LOGGER.error("some errors during execution " + threadClass + " : " + e);
        }
        long finishTime = _threadMXBean.getThreadCpuTime(java.lang.Thread.currentThread().getId());
        timeHashMap.put(EvaluationThread.class.getName(),
                TimeUnit.MILLISECONDS.convert(finishTime - startTime, TimeUnit.NANOSECONDS));
    }
}
