package org.atilf.delegate.disambiguation.contextLexicon;

import org.atilf.delegate.Delegate;
import org.atilf.models.TermithIndex;
import org.atilf.models.disambiguation.DisambiguationXslResources;
import org.atilf.module.disambiguation.contextLexicon.DisambiguationXslTransformer;

import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

/**
 * transform files of a corpus into working file format and extract context of the terminology entry of corpus
 * @author Simon Meoni
 *         Created on 12/10/16.
 */
public class DisambiguationXslTransformerDelegate extends Delegate {

    @Override
    public void executeTasks() throws IOException, InterruptedException {
        DisambiguationXslResources xslResources = new DisambiguationXslResources();

        /*
        Transformation phase
         */
        Files.list(TermithIndex.getLearningPath()).forEach(
                p -> _executorService.submit(new DisambiguationXslTransformer(
                        p.toFile(),
                        _termithIndex,
                        xslResources)
                )
        );

        if (TermithIndex.getLearningPath() != TermithIndex.getEvaluationPath()) {
            Files.list(TermithIndex.getEvaluationPath()).forEach(
                    p -> _executorService.submit(
                            new DisambiguationXslTransformer(
                                    p.toFile(),
                                    _termithIndex,
                                    _termithIndex.getEvaluationTransformedFiles(),
                                    xslResources)
                    )
            );
        }

        _logger.info("Waiting ContextExtractor executors to finish");
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }
}
