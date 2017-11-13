package org.atilf.delegate.disambiguation.evaluation;

import org.atilf.delegate.Delegate;
import org.atilf.module.disambiguation.evaluation.EvaluationExtractor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Evaluate the corpus with the lexicalProfile creates with the LexiconProfileDelegate
 * @author Simon Meoni
 *         Created on 12/10/16.
 */
public class EvaluationExtractorDelegate extends Delegate {
    /**
     * this method is split in two parts. Firstly, for each file, the context is extract for each terms candidates.write threshold integration test
     * Finally, each context is evaluated with the lexical associated in order to determine the terminology potentiality
     * of a term candidate.
     * @throws IOException thrown a IO exception if a file is not found or have a permission problem during the
     * xsl transformation phase
     * @throws InterruptedException thrown if awaitTermination function is interrupted while waiting
     */
    @Override
    public void executeTasks() throws IOException, InterruptedException {


        _termithIndex.getEvaluationTransformedFiles().values().forEach(
                p -> _executorService.submit(
                        new EvaluationExtractor(p.toString(), _termithIndex)
                ));

        _logger.info("Waiting EvaluationExtractorWorker executors to finish");
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }
}
