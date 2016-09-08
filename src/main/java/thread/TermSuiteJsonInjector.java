package thread;

import module.termsuite.JsonPipelineBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author Simon Meoni
 *         Created on 01/09/16.
 */
public class TermSuiteJsonInjector {

    private static final Logger LOGGER = LoggerFactory.getLogger(TermSuiteJsonInjector.class.getName());
    private static final int DEFAULT_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    private Path corpus;
    private Map<String, Path> json;
    private Map<String, StringBuffer> xmlCorpus;
    private ExecutorService executorService;
    private String treeTaggerHome;
    private String lang;

    private List<Path> terminologies;

    public TermSuiteJsonInjector(Map<String, Path> json, Map<String, StringBuffer> xmlCorpus,
                                 String treeTaggerHome, String lang, Path corpus)
            throws IOException {
        this(DEFAULT_POOL_SIZE, json, xmlCorpus, treeTaggerHome, lang, corpus);
    }

    public TermSuiteJsonInjector(int poolSize, Map<String, Path> json,
                                 Map<String, StringBuffer> xmlCorpus,
                                 String treeTaggerHome, String lang, Path corpus) throws IOException {
        this.treeTaggerHome = treeTaggerHome;
        this.executorService = Executors.newFixedThreadPool(poolSize);
        this.json = json;
        this.xmlCorpus = xmlCorpus;
        this.corpus = corpus;
        this.lang = lang;
        this.terminologies = new CopyOnWriteArrayList<>();
    }

    public void execute() throws ExecutionException, InterruptedException {
        JsonTermSuiteWorker jsonTermSuiteWorker = new JsonTermSuiteWorker(this.corpus + "/json", lang);
        Future<?> termsuiteTask = executorService.submit(jsonTermSuiteWorker);
        termsuiteTask.get();
        executorService.shutdown();
        executorService.awaitTermination(1L,TimeUnit.DAYS);
    }

    private class JsonTermSuiteWorker implements Runnable{
        private final String jsonCorpus;
        private final String lang;

        JsonTermSuiteWorker(String jsonCorpus, String lang) {
            this.jsonCorpus = jsonCorpus;
            this.lang = lang;
        }

        @Override
        public void run() {
            LOGGER.info("Build Termsuite Pipeline");
            terminologies.add(Paths.get(jsonCorpus.replace("json","") + "/" + "terminology.json"));
            terminologies.add(Paths.get(jsonCorpus.replace("json","") + "/" + "terminology.tbx"));

            JsonPipelineBuilder pipelineBuilder = new JsonPipelineBuilder(
                    lang,
                    this.jsonCorpus,
                    terminologies.get(0).toString(),
                    terminologies.get(1).toString()
            );
            LOGGER.info("Run Termsuite Pipeline");
            pipelineBuilder.start();
            LOGGER.info("Finished execution of Termsuite Pipeline, result in :" +
                    jsonCorpus);

        }
    }
}
