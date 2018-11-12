package org.atilf.delegate.disambiguation.disambiguationExporter;

import org.atilf.delegate.Delegate;
import org.atilf.module.disambiguation.disambiguationExporter.JsonEvalContextExporter;
import org.atilf.monitor.timer.TermithProgressTimer;
import org.flowable.engine.delegate.DelegateExecution;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class JsonEvalContextExporterDelegate extends Delegate {
    private Path _outputPath;

    public void setOutputPath(Path outputPath) {
        _outputPath = outputPath;
    }

    @Override
    public void initialize(DelegateExecution execution) {
        super.initialize(execution);
        _outputPath = getFlowableVariable("out",null);
    }

    @Override
    public void executeTasks() throws IOException, InterruptedException {

        List<Future> futures = new ArrayList<>();
        _termithIndex.getEvaluationLexicon().forEach(
                (k,v) -> futures.add(
                        _executorService.submit(
                                new JsonEvalContextExporter(
                                        k,
                                        v,
                                        _outputPath.toString())
                        )
                )
        );
        new TermithProgressTimer(futures,this.getClass(),_executorService).start();
        _logger.info("Waiting ContextExtractorWorker executors to finish");
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }
}
