package org.atilf.delegate.disambiguation.txm;

import org.atilf.delegate.Delegate;
import org.atilf.module.disambiguation.txm.TxmExporter;
import org.atilf.monitor.timer.TermithProgressTimer;
import org.atilf.runner.Runner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by smeoni on 19/04/17.
 */
public class TxmExporterDelegate extends Delegate {
    public void executeTasks() throws IOException, InterruptedException {

        List<Future> futures = new ArrayList<>();
        _termithIndex.getTermsTxmContext().forEach(
                (k,v) -> futures.add(_executorService.submit(new TxmExporter(k,v,Runner.getOut().toString())))
        );
        new TermithProgressTimer(futures,this.getClass(),_executorService).start();
        _logger.info("Waiting ContextExtractorWorker executors to finish");
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }
}
