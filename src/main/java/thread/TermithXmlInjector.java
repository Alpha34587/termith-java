package thread;

import eu.project.ttc.tools.TermSuitePipeline;
import module.FilesUtilities;
import module.MorphoSyntaxInjector;
import module.TermSuitePipelineBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Map;
import java.util.concurrent.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

/**
 * Created by Simon Meoni on 18/08/16.
 */
public class TermithXmlInjector {

    private static final Logger LOGGER = LoggerFactory.getLogger(TermithXmlInjector.class.getName());
    private static final int DEFAULT_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    private Path corpus;
    private Map<String, StringBuffer> extractedText;
    private Map<String, StringBuffer> xmlCorpus;
    private ExecutorService executorService;
    private String treeTaggerHome;
    private String lang;

    public TermithXmlInjector(Map<String, StringBuffer> extractedText, Map<String, StringBuffer> xmlCorpus,
                              String treeTaggerHome, String lang)
            throws IOException {
        this(DEFAULT_POOL_SIZE, extractedText, xmlCorpus, treeTaggerHome, lang);
    }

    public TermithXmlInjector(int poolSize, Map<String, StringBuffer> extractedText,
                              Map<String, StringBuffer> xmlCorpus,
                              String treeTaggerHome, String lang) throws IOException {
        this.treeTaggerHome = treeTaggerHome;
        this.executorService = Executors.newFixedThreadPool(poolSize);
        this.extractedText = extractedText;
        this.xmlCorpus = xmlCorpus;
        this.corpus = Paths.get(FilesUtilities.createTemporaryFolder("corpus"));
        this.lang = lang;

        LOGGER.info("temporary folder created: " + this.corpus);
        Files.createDirectories(Paths.get(this.corpus + "/json"));
        Files.createDirectories(Paths.get(this.corpus + "/txt"));
        LOGGER.info("create temporary text files in " + this.corpus + "/txt folder");
        FilesUtilities.createFiles(this.corpus + "/txt", extractedText, "txt");
    }

    public void execute() throws InterruptedException, IOException, ExecutionException {
        Future<TermSuitePipeline> termsuiteTask = executorService.submit(
                new TextTermSuiteWorker(treeTaggerHome, this.corpus + "/txt", lang)
        );
        JsonRetrieverWorker jsonRetrieverWorker = new JsonRetrieverWorker(Paths.get(this.corpus + "/json"), extractedText,
                executorService);
        executorService.submit(jsonRetrieverWorker);
        termsuiteTask.get();
        jsonRetrieverWorker.stop();
        executorService.shutdown();
        executorService.awaitTermination(1L,TimeUnit.DAYS);
    }

    private class JsonRetrieverWorker implements Runnable {

        private final Map<String, StringBuffer> extraxtedText;
        private final Logger Logger = LoggerFactory.getLogger(JsonRetrieverWorker.class.getName());
        WatchService watcher = FileSystems.getDefault().newWatchService();
        Path dir;
        WatchKey key;
        ExecutorService executorService;

        JsonRetrieverWorker(Path dir, Map<String, StringBuffer> extractedText, ExecutorService executorService) throws IOException {
            Logger.info("Initialized File Watching Service");
            this.dir = dir;
            this.key = dir.register(watcher, ENTRY_CREATE);
            this.executorService = executorService;
            this.extraxtedText = extractedText;
        }

        @Override
        public void run() {
            Logger.info("File Watcher Service Started");
            for (;;) {
                try {
                    this.key = watcher.take();

                } catch (InterruptedException e) {
                    Logger.error("File watcher service crashed",e);
                    Thread.currentThread().interrupt();
                }
                for (WatchEvent<?> event: key.pollEvents()) {
                    WatchEvent<Path> ev = (WatchEvent<Path>)event;
                    Path filename = dir.resolve(ev.context());
                    Logger.info("New file retrieve: " + filename);
                    String basename = filename.getFileName().toString().replace(".json", "");
                    executorService.execute(new MorphoSyntaxInjectorWorker(filename.toFile(),
                            extractedText.get(basename),xmlCorpus.get(basename)));
                }
                boolean valid = key.reset();
                if (!valid) {
                    Logger.info("Interrupt File Watcher Service");
                    break;
                }
            }
        }

        public void stop() throws IOException {
            Logger.info("File Watcher Service Terminated");
            watcher.close();
        }

    }

    private class TextTermSuiteWorker implements Callable<TermSuitePipeline> {
        String textPath;
        String treeTaggerHome;
        String lang;

        TextTermSuiteWorker(String treeTaggerHome, String textPath, String lang) {
            this.textPath = textPath;
            this.treeTaggerHome = treeTaggerHome;
            this.lang = lang;
        }

        @Override
        public TermSuitePipeline call() throws Exception {
            LOGGER.info("Build Termsuite Pipeline");
            TermSuitePipelineBuilder termSuitePipelineBuilder = new TermSuitePipelineBuilder(
                    lang,
                    this.textPath,
                    this.treeTaggerHome
            );
            LOGGER.info("Run Termsuite Pipeline");
            termSuitePipelineBuilder.start();
            LOGGER.info("Finished execution of Termsuite Pipeline, result in :" +
                    textPath.replace("/txt",""));
            return termSuitePipelineBuilder.getTermsuitePipeline();
        }
    }

    private class MorphoSyntaxInjectorWorker implements Runnable{

        private final Logger LOGGER = LoggerFactory.getLogger(JsonRetrieverWorker.class.getName());
        private StringBuffer txt;
        StringBuffer xml;
        File json;

        MorphoSyntaxInjectorWorker(File json,StringBuffer txt, StringBuffer xml) {
            this.json = json;
            this.txt = txt;
            this.xml = xml;
        }

        @Override
        public void run() {
            LOGGER.info("MorphoSyntaxInjectorWorker Started, processing: " + json.getAbsolutePath());
            //TODO Implement 9th phase of TermITH process
            MorphoSyntaxInjector morphoSyntaxInjector = new MorphoSyntaxInjector(json, txt, xml);
            morphoSyntaxInjector.execute();
            LOGGER.info("MorphoSyntaxInjectorWorker Terminated");
        }
    }
}
