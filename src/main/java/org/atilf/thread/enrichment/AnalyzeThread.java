package org.atilf.thread.enrichment;

import org.atilf.models.termith.TermithIndex;
import org.atilf.models.termsuite.CorpusAnalyzer;
import org.atilf.module.enrichment.analyze.TerminologyParser;
import org.atilf.module.enrichment.analyze.TerminologyStandOff;
import org.atilf.module.enrichment.analyze.TermsuitePipelineBuilder;
import org.atilf.module.enrichment.analyze.TreeTaggerWorker;
import org.atilf.module.enrichment.initializer.TextExtractor;
import org.atilf.module.tools.FilesUtils;
import org.atilf.thread.Thread;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The AnalyzeThread calls several modules classes which analyze the morphology of each file in the corpus and the
 * terminology of the corpus. The morphology is analyzed with a treetagger wrapper. The result is serialized to
 * the json termsuite format. The terminology uses the json files write during the analyze of the morphology.
 * The terminology is export as two json and tbx files. Finally the result of the phase is prepared in order to
 * write them into the tei files of the corpus
 * @author Simon Meoni
 *         Created on 01/09/16.
 */
public class AnalyzeThread extends Thread{

    private CountDownLatch _jsonCnt;

    /**
     * this constructor initialize the _termithIndex fields and initialize the _poolSize field with the default value
     * with the number of available processors.
     *
     * @param termithIndex
     *         the termithIndex is an object that contains the results of the process
     */
    public AnalyzeThread(TermithIndex termithIndex) {
        this(termithIndex,Thread.DEFAULT_POOL_SIZE);
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
    public AnalyzeThread(TermithIndex termithIndex, int poolSize) {
        super(termithIndex, poolSize);
        _jsonCnt = new CountDownLatch(termithIndex.getExtractedText().size());
    }

    /**
     * this method return the result of the InitializerThread.
     * @return it returns a hashmap who contains the extracted text of the previous step of each files
     * @see InitializerThread
     * @see TextExtractor
     */
    private Map<String,StringBuilder> createTextHashmap(){
        Map<String,StringBuilder> textMap = new HashMap<>();

        /*
        read extracted text of the previous phase and put the result to the hashmap. the filename is
        the key of each entries
         */
        _termithIndex.getExtractedText().forEach(
                (key,value) -> textMap.put(key,FilesUtils.readObject(value, StringBuilder.class))
        );
        return textMap;
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
     * @see TerminologyParser
     * @see TerminologyStandOff
     */
    public void execute() throws InterruptedException, IOException, ExecutionException {
        /*
        Build Corpus analyzer
         */
        CorpusAnalyzer corpusAnalyzer = new CorpusAnalyzer(createTextHashmap());

        /*
        Write morphology json file
         */
        _termithIndex.getExtractedText().forEach((key, txt) ->
                _executorService.submit(new TreeTaggerWorker(
                        _termithIndex,
                        corpusAnalyzer,
                        key,
                        _jsonCnt
                ))
        );
        _logger.info("waiting that all json files are serialized");
        /*
        wait that all corpus file are treated
         */
        _jsonCnt.await();
        _logger.info("json files serialization finished, termsuite task started");

        /*
        execute termsuite
         */
        _executorService.submit(new TermsuitePipelineBuilder(_termithIndex)).get();
        _logger.info("terminology extraction started");
        _executorService.submit(new TerminologyParser(_termithIndex)).get();

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
