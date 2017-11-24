package org.atilf.delegate.disambiguation.contextLexicon;

import org.atilf.delegate.Delegate;
import org.atilf.resources.disambiguation.DisambiguationXslResources;
import org.atilf.module.disambiguation.contextLexicon.DisambiguationXslTransformer;
import org.atilf.monitor.timer.TermithProgressTimer;
import org.flowable.engine.delegate.DelegateExecution;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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

    private Path _learningPath;
    private Path _outputPath;

    public void setLearningPath(Path learningPath) {
        _learningPath = learningPath;
    }

    public void setOutputPath(Path outputPath) {
        _outputPath = outputPath;
    }

    @Override
    public void initialize(DelegateExecution execution) {
        super.initialize(execution);
        _learningPath = getFlowableVariable("learningPath",null);
        _outputPath = getFlowableVariable("out",null);
    }

    @Override
    public void executeTasks() throws IOException, InterruptedException {
        DisambiguationXslResources xslResources = new DisambiguationXslResources(TermithResource.DISAMBIGUATION_XSL.getPath());
        List<Future> futures = new ArrayList<>();
        /*
        Transformation phase
         */
        Files.list(_learningPath).forEach(
                p -> futures.add(_executorService.submit(new DisambiguationXslTransformer(
                        p.toFile(),
                        _termithIndex,
                        xslResources,
                        _outputPath
                        ))
                )
        );
        new TermithProgressTimer(futures,LearnXslTransformerDelegate.class,_executorService).start();
        _logger.info("Waiting ContextExtractor executors to finish");
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }
}
