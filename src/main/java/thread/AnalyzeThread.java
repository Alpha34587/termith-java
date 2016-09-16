package thread;

import models.MorphoSyntaxOffsetId;
import models.TermithIndex;
import module.tools.FilesUtilities;
import module.treetagger.CorpusAnalyzer;
import module.treetagger.TagNormalizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import worker.TreeTaggerWorker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author Simon Meoni
 *         Created on 01/09/16.
 */
public class AnalyzeThread extends TermSuiteTextInjector {

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
        termithIndex.setCorpus(Paths.get(FilesUtilities.createTemporaryFolder("corpus")));
        executorService = Executors.newFixedThreadPool(poolSize);
        this.termithIndex = termithIndex;
        jsonCnt = new CountDownLatch(termithIndex.getExtractedText().size());
    }

    public Path getCorpus() {
        return termithIndex.getCorpus();
    }

    public void execute() throws InterruptedException, IOException {
        init();
        CorpusAnalyzer corpusAnalyzer = new CorpusAnalyzer(termithIndex.getExtractedText());

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
        executorService.shutdown();
        executorService.awaitTermination(1L,TimeUnit.DAYS);
    }

    private void init() throws IOException {
        TagNormalizer.initTag(TermithIndex.lang);

        LOGGER.info("temporary folder created: " + termithIndex.getCorpus());
        Files.createDirectories(Paths.get(termithIndex.getCorpus() + "/json"));
        Files.createDirectories(Paths.get(termithIndex.getCorpus() + "/txt"));
        LOGGER.info("create temporary text files in " + termithIndex.getCorpus() + "/txt folder");
        FilesUtilities.createFiles(termithIndex.getCorpus() + "/txt",
                termithIndex.getExtractedText(), "txt");
    }
}
