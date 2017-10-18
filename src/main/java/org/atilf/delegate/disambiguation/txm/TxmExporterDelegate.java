package org.atilf.delegate.disambiguation.txm;

import org.atilf.delegate.Delegate;
import org.atilf.module.disambiguation.txm.TxmExporter;
import org.atilf.monitor.timer.TermithProgressTimer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by Simon Meoni on 19/04/17.
 */
public class TxmExporterDelegate extends Delegate {
    Path _outputPath = getFlowableVariable("out",null);

    public void setOutputPath(Path outputPath) {
        _outputPath = outputPath;
    }

    @Override
    public void executeTasks() throws IOException, InterruptedException {
        List<Future> futures = new ArrayList<>();
        if (_termithIndex.getTermsTxmContext().isEmpty()){
            throw  new InterruptedException("no context are extracted by the txmContextExtractor, perhaps the " +
                    "annotation argument are not well-formed : " + getFlowableVariable("annotation","")
                    + " or there are no such annotations on this corpus ? try 'grep \"" + getFlowableVariable
                    ("annotation","") + "\" " + getFlowableVariable("txmInputPath",null) +"/*'");
        }
        _termithIndex.getTermsTxmContext().forEach(
                (k,v) -> futures.add(_executorService.submit(new TxmExporter(k,v,_outputPath.toString())))
        );
        new TermithProgressTimer(futures,this.getClass(),_executorService).start();
        _logger.info("Waiting ContextExtractorWorker executors to finish");
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }
}
