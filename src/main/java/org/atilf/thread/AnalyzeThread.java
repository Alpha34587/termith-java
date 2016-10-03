package org.atilf.thread;

import org.atilf.models.MorphoSyntaxOffsetId;
import org.atilf.models.TermithIndex;
import org.atilf.module.timer.JsonTimer;
import org.atilf.module.timer.TerminologyTimer;
import org.atilf.module.timer.TokenizeTimer;
import org.atilf.module.tools.FilesUtilities;
import org.atilf.module.treetagger.CorpusAnalyzer;
import org.atilf.models.TagNormalizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.atilf.worker.TerminologyParserWorker;
import org.atilf.worker.TerminologyStandOffWorker;
import org.atilf.worker.TermsuiteWorker;
import org.atilf.worker.TreeTaggerWorker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static org.atilf.models.TermithIndex.lang;
import static org.atilf.models.TermithIndex.outputPath;

/**
 * @author Simon Meoni
 *         Created on 01/09/16.
 */
public class AnalyzeThread {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyzeThread.class.getName());
    private static final int DEFAULT_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private final ExecutorService executorService;
    private Map<String, StringBuffer> tokenizeTeiBody;
    private Map<String, Path> JsonTreeTagger;
    private Map<String, List<MorphoSyntaxOffsetId>> morphoSyntaxStandOff;
    private TermithIndex termithIndex;
    private CountDownLatch jsonCnt;

    public AnalyzeThread(TermithIndex termithIndex)
            throws IOException {
        this(DEFAULT_POOL_SIZE, termithIndex);
    }

    public AnalyzeThread(int poolSize, TermithIndex termithIndex) throws IOException {
        termithIndex.setCorpus(outputPath);
        executorService = Executors.newFixedThreadPool(poolSize);
        this.termithIndex = termithIndex;
        jsonCnt = new CountDownLatch(termithIndex.getExtractedText().size());
    }

    public Path getCorpus() {
        return termithIndex.getCorpus();
    }

    public Map<String,StringBuilder> getDeserializeText(){
        Map<String,StringBuilder> textMap = new HashMap<>();
        termithIndex.getExtractedText().forEach(
                (key,value) -> {
                    textMap.put(key,(StringBuilder) FilesUtilities.readObject(value));
                }
        );
        return textMap;
    }

    public void execute() throws InterruptedException, IOException, ExecutionException {
        init();
        new TokenizeTimer(termithIndex,LOGGER).start();
        new JsonTimer(termithIndex,LOGGER).start();

        CorpusAnalyzer corpusAnalyzer = new CorpusAnalyzer(getDeserializeText());
        termithIndex.getExtractedText().forEach((key,txt) ->
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
        termithIndex.getMorphoSyntaxStandOff().forEach(
                (id,value) -> executorService.submit(new TerminologyStandOffWorker(
                        id,value,termithIndex)
                )
        );
        executorService.shutdown();
        executorService.awaitTermination(1L,TimeUnit.DAYS);
        LOGGER.info("terminology extraction finished");
    }

    private void init() throws IOException {
        TagNormalizer.initTag(lang);
        LOGGER.debug("temporary folder created: " + termithIndex.getCorpus());
        Files.createDirectories(Paths.get(termithIndex.getCorpus() + "/json"));
        Files.createDirectories(Paths.get(termithIndex.getCorpus() + "/txt"));
        LOGGER.debug("create temporary text files in " + termithIndex.getCorpus() + "/txt folder");
    }
}
