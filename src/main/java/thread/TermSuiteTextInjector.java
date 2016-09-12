package thread;

import eu.project.ttc.tools.TermSuitePipeline;
import module.tei.morphology.SyntaxGenerator;
import module.termsuite.PipelineBuilder;
import module.tools.FilesUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

/**
 * the class TermSuiteTextInjector is one of a second phase of the termith process. It splits into two main process who
 * run asynchronously : each morphology json file are retrieve by a file watcher. the file watcher service
 * execute workers who tokenizes a text and report the morphology information of specific file.
 * @author Simon Meoni
 * Created on 18/08/16.
 */
public class TermSuiteTextInjector {

    private static final Logger LOGGER = LoggerFactory.getLogger(TermSuiteTextInjector.class.getName());
    private static final int DEFAULT_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    private Path corpus;
    private Map<String, StringBuffer> extractedText;
    private Map<String, StringBuffer> xmlCorpus;
    private ExecutorService executorService;
    private String treeTaggerHome;
    private String lang;

    private List<Path> terminologies;
    private Map<String, StringBuffer> morphoSyntaxStandOff;
    private Map<String, StringBuffer> tokenizeTeiBody;


    /**
     * this is the main builder of termithXmlInjector
     * @param extractedText the plain extract previously previously
     * @param xmlCorpus the base corpus send to TermithXmlAnalyzer
     * @param treeTaggerHome the path of TreeTagger used by TermsuitePipelineBuilder
     * @param lang the language of the corpus
     * @throws IOException
     */
    public TermSuiteTextInjector(Map<String, StringBuffer> extractedText, Map<String, StringBuffer> xmlCorpus,
                                 String treeTaggerHome, String lang)
            throws IOException {
        this(DEFAULT_POOL_SIZE, extractedText, xmlCorpus, treeTaggerHome, lang);
    }

    /**
     * @see TermSuiteTextInjector
     * @param poolSize specify the number of worker
     * @param extractedText the plain extract previously previously
     * @param xmlCorpus the base corpus send to TermithXmlAnalyzer
     * @param treeTaggerHome the path of TreeTagger used by TermsuitePipelineBuilder
     * @param lang the language of the corpus
     * @throws IOException
     */
    public TermSuiteTextInjector(int poolSize, Map<String, StringBuffer> extractedText,
                                 Map<String, StringBuffer> xmlCorpus,
                                 String treeTaggerHome, String lang) throws IOException {
        this.treeTaggerHome = treeTaggerHome;
        this.executorService = Executors.newFixedThreadPool(poolSize);
        this.extractedText = extractedText;
        this.xmlCorpus = xmlCorpus;
        this.corpus = Paths.get(FilesUtilities.createTemporaryFolder("corpus"));
        this.lang = lang;
        this.morphoSyntaxStandOff = new ConcurrentHashMap<>();
        this.tokenizeTeiBody = new ConcurrentHashMap<>();
        this.terminologies = new CopyOnWriteArrayList<>();


        LOGGER.info("temporary folder created: " + this.corpus);
        Files.createDirectories(Paths.get(this.corpus + "/json"));
        Files.createDirectories(Paths.get(this.corpus + "/txt"));
        LOGGER.info("create temporary text files in " + this.corpus + "/txt folder");
        FilesUtilities.createFiles(this.corpus + "/txt", extractedText, "txt");
    }

    public TermSuiteTextInjector() {
    }

    /**
     * Execute the Termsuite task and the TermSuiteTextInjector
     * @throws InterruptedException
     * @throws IOException
     * @throws ExecutionException
     */
    public void execute() throws InterruptedException, IOException, ExecutionException {

        JsonRetrieverWorker jsonRetrieverWorker = new JsonRetrieverWorker(Paths.get(this.corpus + "/json"));
        TextTermSuiteWorker textTermSuiteWorker = new TextTermSuiteWorker(treeTaggerHome, this.corpus + "/txt", lang);

        Future<TermSuitePipeline> termsuiteTask = executorService.submit(textTermSuiteWorker);
        executorService.submit(jsonRetrieverWorker);

        LOGGER.info("waiting Termsuite executor to finish");
        termsuiteTask.get();
        jsonRetrieverWorker.stop();
        executorService.shutdown();
        executorService.awaitTermination(1L,TimeUnit.DAYS);
    }

    /**
     * This class is a file watcher service who retrieve the file generate by termsuite and send it to a
     * TeiMorphoSyntaxWorker task
     */
    private class JsonRetrieverWorker implements Runnable {

        private final Logger Logger = LoggerFactory.getLogger(JsonRetrieverWorker.class.getName());
        WatchService watcher = FileSystems.getDefault().newWatchService();
        Path dir;
        WatchKey key;

        JsonRetrieverWorker(Path dir) throws IOException {
            Logger.info("Initialized File Watching Service");
            this.dir = dir;
            this.key = dir.register(watcher, ENTRY_CREATE);
        }

        /**
         * run the file watching
         */
        @Override
        public void run() {
            Logger.info("File Watcher Service Started");
            for (;;) {
                try {
                    this.key = watcher.take();

                } catch (InterruptedException e) {
                    Logger.error("File watcher service crashed", e);
                    Thread.currentThread().interrupt();
                }
                for (WatchEvent<?> event: key.pollEvents()) {
                    WatchEvent<Path> ev = (WatchEvent<Path>)event;
                    Path filename = dir.resolve(ev.context());
                    Logger.info("New file retrieve: " + filename);
                    String basename = filename.getFileName().toString().replace(".json", "");
                    executorService.execute(
                            new TeiMorphoSyntaxWorker(
                                    filename.toFile(),
                                    extractedText.get(basename),
                                    xmlCorpus.get(basename)
                            )
                    );
                }
                boolean valid = key.reset();
                if (!valid) {
                    Logger.info("Interrupt File Watcher Service");
                    break;
                }
            }
        }

        /**
         * Stop the process
         * @throws IOException
         */
        public void stop() throws IOException {
            Logger.info("File Watcher Service Terminated");
            watcher.close();
        }

    }

    /**
     * run a Thread who execute a TermsuitePipeline task
     */
    private class TextTermSuiteWorker implements Callable<TermSuitePipeline> {
        String textPath;
        String treeTaggerHome;
        String lang;

        /**
         * the constructor of TextTermsuiteWorker
         * @see PipelineBuilder
         * @param treeTaggerHome
         * @param textPath
         * @param lang
         */
        TextTermSuiteWorker(String treeTaggerHome, String textPath, String lang) {
            this.textPath = textPath;
            this.treeTaggerHome = treeTaggerHome;
            this.lang = lang;
        }

        /**
         * Execute a termsuitePipelineBuilder process and return the result
         * @return return a the result of the termsuite process
         * @throws Exception
         */
        public TermSuitePipeline call() throws Exception {
            LOGGER.info("Build Termsuite Pipeline");

            terminologies.add(Paths.get(textPath.replace("txt","") + "/" + "terminology.tbx"));
            terminologies.add(Paths.get(textPath.replace("txt","") + "/" + "terminology.json"));

            PipelineBuilder pipelineBuilder = new PipelineBuilder(
                    lang,
                    this.textPath,
                    this.treeTaggerHome,
                    terminologies.get(0).toString(),
                    terminologies.get(1).toString()
            );
            LOGGER.info("Run Termsuite Pipeline");
            pipelineBuilder.start();
            LOGGER.info("Finished execution of Termsuite Pipeline, result in :" +
                    textPath.replace("/txt",""));

            return pipelineBuilder.getTermsuitePipeline();
        }
    }

    /**
     * This class execute a TermSuiteTextInjector
     * @see TermSuiteTextInjector
     */
    class TeiMorphoSyntaxWorker implements Runnable{

        private final Logger LOGGER = LoggerFactory.getLogger(JsonRetrieverWorker.class.getName());
        private StringBuffer txt;
        private StringBuffer xml;
        private File json;

        /**
         * The constructor of the class. this parameter is used to instanced a TermSuiteTextInjector
         * @param json
         * @param txt
         * @param xml
         */
        TeiMorphoSyntaxWorker(File json, StringBuffer txt, StringBuffer xml) {
            this.json = json;
            this.txt = txt;
            this.xml = xml;
        }

        /**
         * run a task who execute the process of analyzes of TeiMorphoSyntaxWorker
         * @see TermSuiteTextInjector
         */
        @Override
        public void run() {
            LOGGER.info("TeiMorphoSyntaxWorker Started, processing: " + json.getAbsolutePath());
            SyntaxGenerator syntaxGenerator = new SyntaxGenerator(json, txt, xml);
            syntaxGenerator.execute();
            tokenizeTeiBody.put(json.getName().replace("json",""), syntaxGenerator.getTokenizeBody());
            LOGGER.info("TeiMorphoSyntaxWorker Terminated");
        }
    }
}
