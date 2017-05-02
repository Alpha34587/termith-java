package org.atilf.delegate.tools;

import org.atilf.delegate.Delegate;
import org.atilf.module.tools.BenchmarkFactory;
import org.atilf.runner.Runner;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Simon Meoni on 24/03/17.
 */
public class BenchmarkFactoryDelegate extends Delegate{


    @Override
    public void executeTasks() throws InterruptedException, IOException, ExecutionException {

        Path path = Paths.get(getFlowableVariable("performancePath","."));
        /*
        executeTasks termsuite
         */
        _executorService.submit(new BenchmarkFactory(Runner.getMemoryPerformanceEvents(),path)).get();
        _executorService.submit(new BenchmarkFactory(Runner.getTimePerformanceEvents(),path)).get();

        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
        _logger.info("benchmark export is finished");
    }
}
