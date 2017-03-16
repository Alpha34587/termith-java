package org.atilf.thread.disambiguation.disambiguationExporter;

import org.atilf.models.TermithIndex;
import org.atilf.module.disambiguation.disambiguationExporter.DisambiguationTeiWriter;
import org.atilf.thread.Delegate;
import org.atilf.tools.FilesUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static org.atilf.runner.Runner.DEFAULT_POOL_SIZE;

/**
 * export the result of the module.disambiguation to the tei/standoff xml file format
 * @author Simon Meoni
 *         Created on 25/10/16.
 */

public class DisambiguationExporterDelegate extends Delegate {

    /**
     * this constructor initialize the _termithIndex fields and initialize the _poolSize field with the default value
     * with the number of available processors.
     * @param termithIndex the termithIndex is an object that contains the results of the process*
     */
    public DisambiguationExporterDelegate(TermithIndex termithIndex) {
        super(termithIndex, DEFAULT_POOL_SIZE);
    }

    /**
     * this constructor initialize all the needed fields. it initialize the termithIndex who contains the result of
     * process and initialize the size of the pool of _executorService field.
     * @param termithIndex the termithIndex is an object that contains the results of the process
     * @param poolSize the number of thread used during the process
     * @see TermithIndex
     * @see ExecutorService
     */
    public DisambiguationExporterDelegate(TermithIndex termithIndex, int poolSize) {
        super(termithIndex, poolSize);

    }

    /**
     * this method add to the stack of executorService a work that consist to inject the result of terms module.disambiguation
     * for each tei file in the corpus
     * @throws IOException thrown a IO exception if a file is not found or have a permission problem during the
     * xsl transformation phase
     * @throws InterruptedException thrown if awaitTermination function is interrupted while waiting
     */
    public void executeTasks() throws IOException, InterruptedException {

        Files.list(TermithIndex.getEvaluationPath()).forEach(
                p -> {
                    String file = FilesUtils.nameNormalizer(p.toString());
                    _executorService.submit(new DisambiguationTeiWriter(
                            p.toString(),
                            _termithIndex
                    ));
                }

        );
        _logger.info("Waiting ContextExtractorWorker executors to finish");
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }
}
