package org.atilf.delegate.disambiguation.lexiconProfile;

import org.atilf.delegate.Delegate;
import org.atilf.models.disambiguation.RConnectionPool;
import org.atilf.models.disambiguation.RLexicon;
import org.atilf.module.disambiguation.lexiconProfile.SpecCoefficientInjector;

import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.currentThread;

/**
 * The LexiconProfileDelegate process the specificity coefficient for each pair of lemma/_pos of a termEntry contained by
 * _contextLexicon map of termithIndex
 * @author Simon Meoni
 *         Created on 12/10/16.
 */
public class LexiconProfileDelegate extends Delegate {

    /**
     * this is the method who converts global corpus into a R variable and compute the specificity coefficient for each
     * words for each context of terms candidates entries (also known as lexical profile)
     * @throws InterruptedException thrown if awaitTermination function is interrupted while waiting
     */
    public void executeTasks() throws InterruptedException, IOException {
        /*
        convert global corpus into R variable
         */
        RLexicon rLexicon = new RLexicon(_termithIndex.getCorpusLexicon());
        RConnectionPool RConnectionPool = new RConnectionPool(8,rLexicon);
        _termithIndex.getContextLexicon().forEach(
                (key, value) -> _executorService.submit(new SpecCoefficientInjector(
                        key,
                        _termithIndex,
                        rLexicon,
                        RConnectionPool))
        );

        _logger.info("Waiting SpecCoefficientInjector executors to finish");
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
        RConnectionPool.removeThread(currentThread());
        Files.delete(rLexicon.getCsvPath());
    }
}
