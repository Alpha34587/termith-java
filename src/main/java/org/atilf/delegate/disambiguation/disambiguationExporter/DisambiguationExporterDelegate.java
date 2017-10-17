package org.atilf.delegate.disambiguation.disambiguationExporter;

import org.atilf.delegate.Delegate;
import org.atilf.module.disambiguation.disambiguationExporter.DisambiguationTeiWriter;
import org.atilf.monitor.timer.TermithProgressTimer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * export the result of the module.disambiguation to the tei/standoff xml file format
 * @author Simon Meoni
 *         Created on 25/10/16.
 */

public class DisambiguationExporterDelegate extends Delegate {

    Path _evaluationPath = getFlowableVariable("evaluationPath",null);
    Path _outputPath = getFlowableVariable("out",null);

    public void setEvaluationPath(Path evaluationPath) {
        _evaluationPath = evaluationPath;
    }

    public void setOutputPath(Path outputPath) {
        _outputPath = outputPath;
    }

    /**
     * this method add to the stack of executorService a work that consist to inject the result of terms module.disambiguation
     * for each tei file in the corpus
     * @throws IOException thrown a IO exception if a file is not found or have a permission problem during the
     * xsl transformation phase
     * @throws InterruptedException thrown if awaitTermination function is interrupted while waiting
     */

    @Override
    public void executeTasks() throws IOException, InterruptedException {

        List<Future> futures = new ArrayList<>();
        Files.list(_evaluationPath).forEach(
                p -> futures.add(
                        _executorService.submit(
                                new DisambiguationTeiWriter(
                                        p.toString(),
                                        _termithIndex,
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
