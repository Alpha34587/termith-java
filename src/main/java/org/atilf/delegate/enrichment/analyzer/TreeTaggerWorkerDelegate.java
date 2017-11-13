package org.atilf.delegate.enrichment.analyzer;

import org.atilf.delegate.Delegate;
import org.atilf.models.enrichment.CorpusAnalyzer;
import org.atilf.models.enrichment.TagNormalizer;
import org.atilf.models.enrichment.TreeTaggerParameter;
import org.atilf.module.enrichment.analyzer.TerminologyParser;
import org.atilf.module.enrichment.analyzer.TerminologyStandOff;
import org.atilf.module.enrichment.analyzer.TermsuitePipelineBuilder;
import org.atilf.module.enrichment.analyzer.TreeTaggerWorker;
import org.atilf.module.enrichment.initializer.TextExtractor;
import org.atilf.module.tools.FilesUtils;
import org.atilf.monitor.timer.TermithProgressTimer;
import org.atilf.runner.TermithResourceManager;
import org.flowable.engine.delegate.DelegateExecution;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.atilf.runner.TermithResourceManager.*;

/**
 * The TreeTaggerWorkerDelegate calls several modules classes which analyzer the morphology of each file in the corpus and the
 * terminology of the corpus. The morphology is analyzed with a treetagger wrapper. The result is serialized to
 * the json termsuite format. The terminology uses the json files write during the analyzer of the morphology.
 * The terminology is export as two json and tbx files. Finally the result of the phase is prepared in order to
 * write them into the tei files of the corpus
 * @author Simon Meoni
 *         Created on 01/09/16.
 */
public class TreeTaggerWorkerDelegate extends Delegate {

    private String _lang;
    private Path _outputPath;

    public void setLang(String lang) {
        _lang = lang;
    }

    public void setOutputPath(Path outputPath) {
        _outputPath = outputPath;
    }

    @Override
    public void initialize(DelegateExecution execution) {
        super.initialize(execution);
        _lang = getFlowableVariable("lang",null);
        _outputPath  = getFlowableVariable("out",null);
    }

    /**
     * this method return the result of the InitializerThread.
     * @return it returns a hashMap who contains the extracted text of the previous step of each files
     * @see TextExtractor
     */
    private Map<String,StringBuilder> createTextHashMap(){
        Map<String,StringBuilder> textMap = new HashMap<>();

        /*
        read extracted text of the previous phase and put the result to the hashMap. the filename is
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
    @Override
    public void executeTasks() throws InterruptedException, IOException, ExecutionException {
        /*
        Build Corpus analyzer
         */
        CorpusAnalyzer corpusAnalyzer = new CorpusAnalyzer(createTextHashMap());
        TagNormalizer.initTag(_lang);
        TreeTaggerParameter treeTaggerParameter =  new TreeTaggerParameter(
                false,
                _lang,
                TermithResource.TREETAGGER_HOME.getPath()
        );
        List<Future> futures = new ArrayList<>();

        /*
        Write morphology json file
         */
        _termithIndex.getExtractedText().forEach((key, txt) ->
                futures.add(_executorService.submit(new TreeTaggerWorker(
                        _termithIndex,
                        corpusAnalyzer,
                        key,
                        _outputPath.toString(),
                        treeTaggerParameter
                )))
        );
        _logger.info("waiting that all json files are serialized");
        new TermithProgressTimer(futures,TreeTaggerWorker.class,_executorService).start();
        _executorService.shutdown();
        _executorService.awaitTermination(1L,TimeUnit.DAYS);
    }
}
