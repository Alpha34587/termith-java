package org.atilf.thread.enrichment;

import org.atilf.models.treetagger.TagNormalizer;
import org.atilf.models.termith.TermithIndex;
import org.atilf.module.timer.JsonTimer;
import org.atilf.module.timer.TokenizeTimer;
import org.atilf.module.tools.FilesUtils;
import org.atilf.module.treetagger.CorpusAnalyzer;
import org.atilf.worker.TerminologyParserWorker;
import org.atilf.worker.TerminologyStandOffWorker;
import org.atilf.worker.TermsuiteWorker;
import org.atilf.worker.TreeTaggerWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author Simon Meoni
 *         Created on 01/09/16.
 */
public class AnalyzeThread {

    private TermithIndex _termithIndex;
    private CountDownLatch _jsonCnt;
    private final ExecutorService _executorService;
    private static final int DEFAULT_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyzeThread.class.getName());

    public AnalyzeThread(TermithIndex termithIndex)
            throws IOException {
        this(DEFAULT_POOL_SIZE, termithIndex);
    }

    public AnalyzeThread(int poolSize, TermithIndex termithIndex) throws IOException {
        termithIndex.set_corpus(TermithIndex.getOutputPath());
        _executorService = Executors.newFixedThreadPool(poolSize);
        _termithIndex = termithIndex;
        _jsonCnt = new CountDownLatch(termithIndex.get_extractedText().size());
    }

    public Path getCorpus() {
        return _termithIndex.get_corpus();
    }

    private Map<String,StringBuilder> getDeserializeText(){
        Map<String,StringBuilder> textMap = new HashMap<>();
        _termithIndex.get_extractedText().forEach(
                (key,value) -> {
                    textMap.put(key,(StringBuilder) FilesUtils.readObject(value));
                }
        );
        return textMap;
    }

    public void execute() throws InterruptedException, IOException, ExecutionException {
        init();
        new TokenizeTimer(_termithIndex,LOGGER).start();
        new JsonTimer(_termithIndex,LOGGER).start();

        CorpusAnalyzer corpusAnalyzer = new CorpusAnalyzer(getDeserializeText());
        _termithIndex.get_extractedText().forEach((key, txt) ->
                _executorService.submit(new TreeTaggerWorker(
                        _termithIndex,
                        corpusAnalyzer,
                        key,
                        _jsonCnt
                ))
        );
        LOGGER.info("waiting that all json files are serialized");
        _jsonCnt.await();
        LOGGER.info("json files serialization finished, termsuite task started");
        _executorService.submit(new TermsuiteWorker(_termithIndex)).get();
        LOGGER.info("terminology extraction started");
        _executorService.submit(new TerminologyParserWorker(_termithIndex)).get();
        _termithIndex.get_morphoSyntaxStandOff().forEach(
                (id,value) -> {
                    _executorService.submit(new TerminologyStandOffWorker(id,value, _termithIndex));
                }
        );
        _executorService.shutdown();
        _executorService.awaitTermination(1L,TimeUnit.DAYS);
        LOGGER.info("terminology extraction finished");
    }

    private void init() throws IOException {
        TagNormalizer.initTag(TermithIndex.get_lang());
        LOGGER.debug("temporary folder created: " + _termithIndex.get_corpus());
        Files.createDirectories(Paths.get(_termithIndex.get_corpus() + "/json"));
        Files.createDirectories(Paths.get(_termithIndex.get_corpus() + "/txt"));
        LOGGER.debug("create temporary text files in " + _termithIndex.get_corpus() + "/txt folder");
    }
}
