package org.atilf.delegate.disambiguation.disambiguationExporter;

import org.atilf.delegate.Delegate;
import org.atilf.module.disambiguation.disambiguationExporter.CorpusWordsExporter;
import org.flowable.engine.delegate.DelegateExecution;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CorpusWordsExporterDelegate extends Delegate {

    private Path _outputPath;

    public void setOutputPath(Path outputPath) {
        _outputPath = outputPath;
    }

    @Override
    public void initialize(DelegateExecution execution) {
        super.initialize(execution);
        _outputPath = getFlowableVariable("out", null);
    }

    @Override
    public void executeTasks() throws IOException, InterruptedException, ExecutionException {
        _executorService.submit(new CorpusWordsExporter(_termithIndex, _outputPath.toString()));
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
        _logger.info("context filter step is finished");
    }
}
