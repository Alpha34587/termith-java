package org.atilf.thread.disambiguation;

import org.atilf.models.disambiguation.RLexicon;
import org.atilf.models.termith.TermithIndex;
import org.atilf.module.disambiguation.SpecCoefficientInjector;
import org.atilf.thread.Thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The LexiconProfileThread process the specificity coefficient for each pair of lemma/pos of a termEntry contained by
 * _contextLexicon map of termithIndex
 * @author Simon Meoni
 *         Created on 12/10/16.
 */
public class LexiconProfileThread extends Thread{

    /**
     * this constructor initialize the _termithIndex fields and initialize the _poolSize field with the default value
     * with the number of available processors.
     *
     * @param termithIndex
     *         the termithIndex is an object that contains the results of the process
     */
    public LexiconProfileThread(TermithIndex termithIndex) {
        super(termithIndex);
    }

    /**
     * this constructor initialize all the needed fields. it initialize the termithIndex who contains the result of
     * process and initialize the size of the pool of _executorService field.
     *
     * @param termithIndex
     *         the termithIndex is an object that contains the results of the process
     * @param poolSize
     *         the number of thread used during the process
     *
     * @see TermithIndex
     * @see ExecutorService
     */
    public LexiconProfileThread(TermithIndex termithIndex, int poolSize) {
        super(termithIndex, poolSize);
    }

    /**
     * this is the method who converts global corpus into a R variable and compute the specificity coefficient for each
     * words for each context of terms candidates entries (also known as lexical profile)
     * @throws InterruptedException thrown if awaitTermination function is interrupted while waiting
     */
    public void execute() throws InterruptedException {
        /*
        convert global corpus into R variable
         */
        RLexicon rLexicon = new RLexicon(_termithIndex.getGlobalLexicon());

        /*
        compute lexical profile for each terms candidates entries
         */
        _termithIndex.getContextLexicon().forEach(
                (key,value) -> _executorService.submit(new SpecCoefficientInjector(
                        key,
                        _termithIndex,
                        rLexicon))
        );

        _logger.info("Waiting ContextExtractorWorker executors to finish");
        _executorService.shutdown();
        _executorService.awaitTermination(1L, TimeUnit.DAYS);
    }
}
