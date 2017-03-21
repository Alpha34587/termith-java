package org.atilf.delegate.disambiguation.contextLexicon;

import org.atilf.delegate.Delegate;
import org.atilf.module.disambiguation.evaluation.CommonWordsPosLemmaCleaner;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Evaluate the corpus with the lexicalProfile creates with the LexiconProfileDelegate
 * @author Simon Meoni
 *         Created on 12/10/16.
 */
public class CommonWordsPosLemmaCleanerDelegate extends Delegate {

    /**
     * this method is split in two parts. Firstly, for each file, the context is extract for each terms candidates.
     * Finally, each context is evaluated with the lexical associated in order to determine the terminology potentiality
     * of a term candidate.
     * @throws IOException thrown a IO exception if a file is not found or have a permission problem during the
     * xsl transformation phase
     * @throws InterruptedException thrown if awaitTermination function is interrupted while waiting
     */
    public void execute() throws IOException, InterruptedException {

        /*
        Common PosLemma cleaner
         */
        _termithIndex.getContextLexicon().forEach(
                (key,value) ->
                {
                    String lexOff = key.replace("On","Off");
                    if (key.contains("_lexOn") &&
                            _termithIndex.getContextLexicon().containsKey(lexOff)) {
                        _executorService.submit(new CommonWordsPosLemmaCleaner(
                                key,
                                value,
                                _termithIndex.getContextLexicon().get(lexOff)
                        ));
                    }
                }
        );

        _logger.info("Waiting EvaluationWorker executors to finish");
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }
}
