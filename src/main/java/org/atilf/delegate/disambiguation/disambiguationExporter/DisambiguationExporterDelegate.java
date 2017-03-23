package org.atilf.delegate.disambiguation.disambiguationExporter;

import org.atilf.delegate.Delegate;
import org.atilf.module.disambiguation.disambiguationExporter.DisambiguationTeiWriter;
import org.atilf.runner.Runner;
import org.atilf.tools.FilesUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

/**
 * export the result of the module.disambiguation to the tei/standoff xml file format
 * @author Simon Meoni
 *         Created on 25/10/16.
 */

public class DisambiguationExporterDelegate extends Delegate {

    /**
     * this method add to the stack of executorService a work that consist to inject the result of terms module.disambiguation
     * for each tei file in the corpus
     * @throws IOException thrown a IO exception if a file is not found or have a permission problem during the
     * xsl transformation phase
     * @throws InterruptedException thrown if awaitTermination function is interrupted while waiting
     */
    public void executeTasks() throws IOException, InterruptedException {

        Files.list(Runner.getEvaluationPath()).forEach(
                p -> {
                    String file = FilesUtils.nameNormalizer(p.toString());
                    _executorService.submit(new DisambiguationTeiWriter(
                            p.toString(),
                            _termithIndex,
                            Runner.getOut().toString()
                    ));
                }

        );
        _logger.info("Waiting ContextExtractorWorker executors to finish");
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }
}
