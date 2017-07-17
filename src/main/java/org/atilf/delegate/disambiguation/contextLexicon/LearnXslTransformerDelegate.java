package org.atilf.delegate.disambiguation.contextLexicon;

import org.atilf.delegate.Delegate;
import org.atilf.models.disambiguation.DisambiguationXslResources;
import org.atilf.module.disambiguation.contextLexicon.DisambiguationXslTransformer;
import org.atilf.monitor.timer.TermithProgressTimer;
import org.atilf.runner.TermithResourceManager;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.atilf.runner.TermithResourceManager.*;

/**
 * transform files of a corpus into working file format and extract context of the terminology entry of corpus
 * @author Simon Meoni
 *         Created on 12/10/16.
 */
public class LearnXslTransformerDelegate extends Delegate {

    @Override
    public void executeTasks() throws IOException, InterruptedException {
        DisambiguationXslResources xslResources = new DisambiguationXslResources(TermithResource.DISAMBIGUATION_XSL.getPath());
        List<Future> futures = new ArrayList<>();
        /*
        Transformation phase
         */
        Files.list(getFlowableVariable("learningPath",null)).forEach(
                p -> futures.add(_executorService.submit(new DisambiguationXslTransformer(
                        p.toFile(),
                        _termithIndex,
                        xslResources,
                        getFlowableVariable("out",null)
                        ))
                )
        );
        new TermithProgressTimer(futures,LearnXslTransformerDelegate.class,_executorService).start();
        _logger.info("Waiting ContextExtractor executors to finish");
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }
}
