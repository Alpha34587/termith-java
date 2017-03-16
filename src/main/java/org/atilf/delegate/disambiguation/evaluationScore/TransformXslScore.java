package org.atilf.delegate.disambiguation.evaluationScore;

import org.atilf.delegate.Delegate;
import org.atilf.models.TermithIndex;
import org.atilf.models.disambiguation.DisambiguationXslResources;
import org.atilf.models.enrichment.XslResources;
import org.atilf.module.disambiguation.contextLexicon.DisambiguationXslTransformer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author Simon Meoni Created on 15/12/16.
 */
public class TransformXslScore extends Delegate {

    /**
     * this method is used to executeTasks the different steps of processing of a delegate
     *
     * @throws IOException
     *         thrown a IO exception if a file is not found or have a permission problem during the xsl transformation
     *         phase
     * @throws InterruptedException
     *         thrown if awaitTermination function is interrupted while waiting
     * @throws ExecutionException
     *         thrown a exception if a system process is interrupted
     */
    @Override
    public void executeTasks() throws IOException, InterruptedException, ExecutionException {
        XslResources xslResources = new DisambiguationXslResources();
        _logger.info("transformation phase is started for TransformXslScore");
        Path scoreFolder = Files.createDirectory(Paths.get(TermithIndex.getOutputPath().toString() + "/score/"));
        Files.list(TermithIndex.getOutputPath())
                .filter(p -> !Files.isDirectory(p))
                .filter(p -> p.toString().endsWith(".xml"))
                .forEach(
                        p -> _executorService.submit(new DisambiguationXslTransformer(
                                p.toFile(),
                                _termithIndex,
                                _termithIndex.getTransformOutputDisambiguationFile(),
                                xslResources,
                                scoreFolder)));
;
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }
}

