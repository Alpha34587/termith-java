package org.atilf.thread;

import org.atilf.models.TagNormalizer;
import org.atilf.models.TermithIndex;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyzeThread.class.getName());
    private static final int DEFAULT_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private final ExecutorService executorService;
    private TermithIndex termithIndex;
    private CountDownLatch jsonCnt;

    public AnalyzeThread(TermithIndex termithIndex)
            throws IOException {
        this(DEFAULT_POOL_SIZE, termithIndex);
    }

    public AnalyzeThread(int poolSize, TermithIndex termithIndex) throws IOException {
        termithIndex.set_corpus(TermithIndex.get_outputPath());
        executorService = Executors.newFixedThreadPool(poolSize);
        this.termithIndex = termithIndex;
        jsonCnt = new CountDownLatch(termithIndex.get_extractedText().size());
    }

    public Path getCorpus() {
        return termithIndex.get_corpus();
    }

    private Map<String,StringBuilder> getDeserializeText(){
        Map<String,StringBuilder> textMap = new HashMap<>();
        termithIndex.get_extractedText().forEach(
                (key,value) -> {
                    textMap.put(key,(StringBuilder) FilesUtils.readObject(value));
                }
        );
        return textMap;
    }

    public void execute() throws InterruptedException, IOException, ExecutionException {
        init();
        new TokenizeTimer(termithIndex,LOGGER).start();
        new JsonTimer(termithIndex,LOGGER).start();

        CorpusAnalyzer corpusAnalyzer = new CorpusAnalyzer(getDeserializeText());
        termithIndex.get_extractedText().forEach((key, txt) ->
                executorService.submit(new TreeTaggerWorker(
                        termithIndex,
                        corpusAnalyzer,
                        key,
                        jsonCnt
                ))
        );
        LOGGER.info("waiting that all json files are serialized");
        jsonCnt.await();
        LOGGER.info("json files serialization finished, termsuite task started");
        executorService.submit(new TermsuiteWorker(termithIndex)).get();
        LOGGER.info("terminology extraction started");
        executorService.submit(new TerminologyParserWorker(termithIndex)).get();
        termithIndex.get_morphoSyntaxStandOff().forEach(
                (id,value) -> {
                    executorService.submit(new TerminologyStandOffWorker(id,value,termithIndex));
                }
        );
        executorService.shutdown();
        executorService.awaitTermination(1L,TimeUnit.DAYS);
        LOGGER.info("terminology extraction finished");
    }

    private void init() throws IOException {
        TagNormalizer.initTag(TermithIndex.get_lang());
        LOGGER.debug("temporary folder created: " + termithIndex.get_corpus());
        Files.createDirectories(Paths.get(termithIndex.get_corpus() + "/json"));
        Files.createDirectories(Paths.get(termithIndex.get_corpus() + "/txt"));
        LOGGER.debug("create temporary text files in " + termithIndex.get_corpus() + "/txt folder");
    }
}
