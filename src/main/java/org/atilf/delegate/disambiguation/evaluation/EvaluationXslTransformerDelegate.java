package org.atilf.delegate.disambiguation.evaluation;

import org.atilf.delegate.Delegate;
import org.atilf.resources.disambiguation.DisambiguationXslResources;
import org.atilf.module.disambiguation.contextLexicon.DisambiguationXslTransformer;
import org.atilf.monitor.timer.TermithProgressTimer;

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
public class EvaluationXslTransformerDelegate extends Delegate {

    Path _evaluationPath = getFlowableVariable("evaluationPath",null);
    Path _learningPath = getFlowableVariable("learningPath",null);
    Path _outputPath = getFlowableVariable("outputPath",null);

    public void setEvaluationPath(Path evaluationPath) {
        _evaluationPath = evaluationPath;
    }

    public void setOutputPath(Path outputPath) {
        _outputPath = outputPath;
    }

    @Override
    public void executeTasks() throws IOException, InterruptedException {
        DisambiguationXslResources xslResources = new DisambiguationXslResources(TermithResource.DISAMBIGUATION_XSL.getPath());
        List<Future> futures = new ArrayList<>();
        /*
        Transformation phase
         */
        if (_learningPath != _evaluationPath) {
            Files.list(_evaluationPath).forEach(
                    p -> futures.add(_executorService.submit(
                            new DisambiguationXslTransformer(
                                    p.toFile(),
                                    _termithIndex,
                                    _termithIndex.getEvaluationTransformedFiles(),
                                    xslResources,
                                    _outputPath
                            )))
            );
        }
        new TermithProgressTimer(futures,EvaluationXslTransformerDelegate.class,_executorService).start();
        _logger.info("Waiting ContextExtractor executors to finish");
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }
}
