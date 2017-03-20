package org.atilf.delegate.enrichment.analyzer;

import org.atilf.delegate.Delegate;
import org.atilf.module.enrichment.analyzer.TerminologyParser;
import org.atilf.module.enrichment.analyzer.TerminologyStandOff;
import org.atilf.module.enrichment.analyzer.TermsuitePipelineBuilder;
import org.atilf.module.enrichment.analyzer.TreeTaggerWorker;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * The TerminologyStandOffDelegate calls several modules classes which analyzer the morphology of each file in the corpus and the
 * terminology of the corpus. The morphology is analyzed with a treetagger wrapper. The result is serialized to
 * the json termsuite format. The terminology uses the json files write during the analyzer of the morphology.
 * The terminology is export as two json and tbx files. Finally the result of the phase is prepared in order to
 * write them into the tei files of the corpus
 * @author Simon Meoni
 *         Created on 01/09/16.
 */
public class TerminologyStandOffDelegate extends Delegate {

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
     * @see TerminologyParser
     * @see TerminologyStandOff
     */
    public void executeTasks() throws InterruptedException, IOException, ExecutionException {
        /*
        deserialize the termsuite terminology
         */
        _termithIndex.getMorphologyStandOff().forEach(
                (id,value) -> _executorService.submit(
                        new TerminologyStandOff(id,_termithIndex)
                )
        );
        _executorService.shutdown();
        _executorService.awaitTermination(1L,TimeUnit.DAYS);
        _logger.info("terminology extraction finished");
    }
}
