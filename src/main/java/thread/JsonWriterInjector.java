package thread;

import module.tools.FilesUtilities;
import module.treetagger.TreeTaggerToJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author Simon Meoni
 *         Created on 01/09/16.
 */
public class JsonWriterInjector extends TermSuiteTextInjector {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonWriterInjector.class.getName());
    private static final int DEFAULT_POOL_SIZE = Runtime.getRuntime().availableProcessors();
    private final String treeTaggerHome;
    private final ExecutorService executorService;
    private final Map<String, StringBuffer> extractedText;
    private final Map<String, StringBuffer> xmlCorpus;
    private Map<String, Path> JsonTreeTagger;

    private final Path corpus;
    private final String lang;
    private final CopyOnWriteArrayList terminologies;

    public JsonWriterInjector(Map<String, StringBuffer> extractedText, Map<String, StringBuffer> xmlCorpus,
                              String treeTaggerHome, String lang)
            throws IOException {
        this(DEFAULT_POOL_SIZE, extractedText, xmlCorpus,treeTaggerHome, lang);
    }

    public JsonWriterInjector(int poolSize, Map<String, StringBuffer> extractedText,
                              Map<String, StringBuffer> xmlCorpus,
                              String treeTaggerHome, String lang) throws IOException {
        this.treeTaggerHome = treeTaggerHome;
        this.executorService = Executors.newFixedThreadPool(poolSize);
        this.extractedText = extractedText;
        this.xmlCorpus = xmlCorpus;
        this.corpus = Paths.get(FilesUtilities.createTemporaryFolder("corpus"));
        this.lang = lang;
        this.terminologies = new CopyOnWriteArrayList<>();
        this.JsonTreeTagger = new ConcurrentHashMap<>();

        LOGGER.info("temporary folder created: " + this.corpus);
        Files.createDirectories(Paths.get(this.corpus + "/json"));
        Files.createDirectories(Paths.get(this.corpus + "/txt"));
        LOGGER.info("create temporary text files in " + this.corpus + "/txt folder");
        FilesUtilities.createFiles(this.corpus + "/txt", extractedText, "txt");
    }

    public Path getCorpus() {
        return corpus;
    }

    public Map<String, Path> getJsonTreeTagger() {
        return JsonTreeTagger;
    }

    public void execute() throws InterruptedException  {
        extractedText.values().forEach(txt -> executorService.submit(new TreeTaggerToJsonWorker(txt)));
        executorService.shutdown();
        executorService.awaitTermination(1L,TimeUnit.DAYS);
    }

    private class TreeTaggerToJsonWorker implements Runnable {
        StringBuffer txt;
        public TreeTaggerToJsonWorker(StringBuffer txt) {
            this.txt = txt;
        }

        @Override
        public void run() {
            LOGGER.info("new treetaggerjson task started");
            TreeTaggerToJson treeTaggerToJson = new TreeTaggerToJson();
            treeTaggerToJson.execute();
            //TODO put here a tokenizer module
            LOGGER.info("treetaggerjson task ended");
        }
    }
}
