package org.atilf.delegate.disambiguation.evaluationScore;

import org.atilf.delegate.Delegate;
import org.atilf.module.disambiguation.evaluationScore.AggregateTeiTerms;
import org.flowable.engine.delegate.DelegateExecution;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author Simon Meoni Created on 15/12/16.
 */
public class AggregateTeiTermsDelegate extends Delegate {

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
     * @param execution
     */
    @Override
    public void executeTasks(DelegateExecution execution) throws IOException, InterruptedException, ExecutionException {
        _logger.info("AggregateTeiTerms phase is started : retrieve all the evaluated terms candidate");
        _termithIndex.getEvaluationLexicon().forEach(
                (p,value) -> _executorService.submit(new AggregateTeiTerms(
                        _termithIndex.getTransformOutputDisambiguationFile().get(p).toString(),
                        value,
                        _termithIndex.getScoreTerms()))
        );
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }
}
