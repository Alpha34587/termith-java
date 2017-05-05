package org.atilf.delegate.disambiguation.evaluation;

import org.atilf.delegate.Delegate;
import org.atilf.module.disambiguation.evaluation.Evaluation;
import org.atilf.monitor.timer.TermithProgressTimer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Evaluate the corpus with the lexicalProfile creates with the LexiconProfileDelegate
 * @author Simon Meoni
 *         Created on 12/10/16.
 */
public class EvaluationDelegate extends Delegate {

    /**
     * this method is split in two parts. Firstly, for each file, the context is extract for each terms candidates.
     * Finally, each context is evaluated with the lexical associated in order to determine the terminology potentiality
     * of a term candidate.
     * @throws IOException thrown a IO exception if a file is not found or have a permission problem during the
     * xsl transformation phase
     * @throws InterruptedException thrown if awaitTermination function is interrupted while waiting
     */
    @Override
    public void executeTasks() throws IOException, InterruptedException {
        /*
        Evaluation phase
         */
        List<Future> futures = new ArrayList<>();
        _termithIndex.getEvaluationLexicon().forEach(
                (p,value) -> futures.add(_executorService.submit(new Evaluation(p, _termithIndex))));
        _logger.info("Waiting EvaluationWorker executors to finish");
        new TermithProgressTimer(futures,this.getClass(),_executorService).start();
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }
}
