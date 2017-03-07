package org.atilf.thread.enrichment.analyzer;

import org.atilf.models.TermithIndex;
import org.atilf.module.enrichment.analyzer.TerminologyParser;
import org.atilf.module.enrichment.analyzer.TerminologyStandOff;
import org.atilf.module.enrichment.analyzer.TermsuitePipelineBuilder;
import org.atilf.module.enrichment.analyzer.TreeTaggerWorker;
import org.atilf.thread.Thread;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static org.atilf.runner.Runner.DEFAULT_POOL_SIZE;

/**
 * The TerminologyParserThread calls several modules classes which analyzer the morphology of each file in the corpus and the
 * terminology of the corpus. The morphology is analyzed with a treetagger wrapper. The result is serialized to
 * the json termsuite format. The terminology uses the json files write during the analyzer of the morphology.
 * The terminology is export as two json and tbx files. Finally the result of the phase is prepared in order to
 * write them into the tei files of the corpus
 * @author Simon Meoni
 *         Created on 01/09/16.
 */
public class TerminologyParserThread extends Thread{


    /**
     * this constructor initialize the _termithIndex fields and initialize the _poolSize field with the default value
     * with the number of available processors.
     *
     * @param termithIndex
     *         the termithIndex is an object that contains the results of the process
     */
    public TerminologyParserThread(TermithIndex termithIndex) {
        this(termithIndex,DEFAULT_POOL_SIZE);
    }

    /**
     * this constructor initialize all the needed fields. it initialize the termithIndex who contains the result of
     * process and initialize the size of the pool of _executorService field.
     *
     * @param termithIndex
     *         the termithIndex is an object that contains the results of the process
     * @param poolSize
     *
     * @see TermithIndex
     * @see ExecutorService
     */
    public TerminologyParserThread(TermithIndex termithIndex, int poolSize) {
        super(termithIndex, poolSize);
    }

    /**
     *  Firstly, the method create two timer inherited objects. These objects show the progress of the tokenization jobs
     *  and the Json serialization jobs. Secondly, a corpusAnalyzer object is initialized : it contains the several
     *  metadata of the corpus. this metadata is used to write termsuite morphology json format. After that all
     *  TreeTaggerWorker jobs are finished, the written json files is given as input to a termsuitePipeline. The output
     *  of Termsuite is deserialize to a java object contained in a termithIndex.
     *  The result of the morphology is added to the termithIndex
     * @throws IOException throws exception if a file is not find
     * @throws InterruptedException throws java concurrent executorService exception
     * @throws ExecutionException throws an exception if a TreeTagger process is interrupted
     * @see TreeTaggerWorker
     * @see TermsuitePipelineBuilder
     * @see TerminologyParserThread
     * @see TerminologyStandOff
     */
    public void execute() throws InterruptedException, IOException, ExecutionException {

        _executorService.submit(new TerminologyParser(_termithIndex));

        _executorService.shutdown();
        _executorService.awaitTermination(1L,TimeUnit.DAYS);
        _logger.info("terminology extraction finished");
    }
}
