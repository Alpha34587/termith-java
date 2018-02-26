package org.atilf.delegate.disambiguation.lexiconProfile;

import org.atilf.delegate.Delegate;
import org.atilf.models.disambiguation.RConnectionPool;
import org.atilf.models.disambiguation.RLexicon;
import org.atilf.module.disambiguation.lexiconProfile.SpecEvalCoefficientInjector;
import org.atilf.resources.disambiguation.RResources;
import org.atilf.runner.TermithResourceManager;
import org.flowable.engine.delegate.DelegateExecution;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.currentThread;

public class EvalLexiconProfileDelegate  extends Delegate {
    /**
     * this is the method who converts global corpus into a R variable and compute the specificity coefficient for each
     * words for each context of terms candidates entries (also known as lexical profile)
     */

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
    public void executeTasks() throws InterruptedException, IOException {
        /*
        convert global corpus into R variable
         */
        RLexicon rLexicon = new RLexicon(
                _termithIndex.getCorpusLexicon(),
                _outputPath.toString()
        );
        RResources.init(TermithResourceManager.TermithResource.DISAMBIGUATION_R_SCRIPT.getPath());
        RConnectionPool rConnectionPool = new RConnectionPool(8, rLexicon);
        _termithIndex.getEvaluationLexicon().forEach(
                (file,map) -> map.forEach(
                (key, value) -> _executorService.submit(new SpecEvalCoefficientInjector(
                        key,
                        file,
                        _termithIndex,
                        rLexicon,
                        _outputPath.toString(),
                        rConnectionPool))
        )
        );

        _logger.info("Waiting SpecCoefficientInjector executors to finish");
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
        rConnectionPool.removeThread(currentThread());
        Files.delete(rLexicon.getCsvPath());
    }
}
