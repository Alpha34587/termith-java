package thread;

import models.MorphoSyntaxOffsetId;
import models.TerminologyOffetId;
import models.TermithIndex;
import module.termsuite.json.JsonPipelineBuilder;
import module.termsuite.terminology.TerminologyParser;
import module.termsuite.terminology.TerminologyStandOff;
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
    private Map<String, List<MorphoSyntaxOffsetId>> morphosyntaxStandOff;
    private Map<String, List<TerminologyOffetId>> terminologyStandOff;
    private Path corpus;
    private Map<String, Path> json;
    private Map<String, StringBuffer> xmlCorpus;
    private ExecutorService executorService;
    private String treeTaggerHome;
    private String lang;

    private List<Path> terminologies;

    public TermSuiteJsonInjector(TermithIndex termithIndex)
            throws IOException {
        this(DEFAULT_POOL_SIZE, termithIndex);
    }

    public TermSuiteJsonInjector(int poolSize, TermithIndex termithIndex) throws IOException {
        treeTaggerHome = termithIndex.getTreeTaggerHome();
        executorService = Executors.newFixedThreadPool(poolSize);
        xmlCorpus = termithIndex.getXmlCorpus();
        corpus = termithIndex.getCorpus();
        lang = termithIndex.getLang();
        terminologies = termithIndex.getTerminologies();
        morphosyntaxStandOff =  termithIndex.getMorphoSyntaxStandOff();
    }

    public void execute() throws ExecutionException, InterruptedException {
        JsonTermSuiteWorker jsonTermSuiteWorker = new JsonTermSuiteWorker(corpus + "/json", lang);
        Future<?> termsuiteTask = executorService.submit(jsonTermSuiteWorker);
        termsuiteTask.get();
        Future<?> terminologyTask = executorService.submit(new TerminologyParserWorker());
        terminologyTask.get();
        morphosyntaxStandOff.forEach(
                (id,value) -> executorService.submit(new TerminologyStandOffWorker(
                        id,value,terminologyStandOff.get(id))
                )
        );
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
            terminologies.add(Paths.get(jsonCorpus.replace("json","") + "/" + "terminology.tbx"));
            terminologies.add(Paths.get(jsonCorpus.replace("json","") + "/" + "terminology.json"));

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

    private class TerminologyStandOffWorker implements Runnable {
        private final String id;
        private final List<MorphoSyntaxOffsetId> morpho;
        private List<TerminologyOffetId> termino;

        TerminologyStandOffWorker(String id, List<MorphoSyntaxOffsetId> morpho, List<TerminologyOffetId> termino) {

            this.id = id;
            this.morpho = morpho;

            this.termino = termino;
        }

        @Override
        public void run() {
            TerminologyStandOff terminologyStandOff = new TerminologyStandOff(id, morpho, termino);
            terminologyStandOff.execute();
        }
    }

    private class TerminologyParserWorker implements Runnable {

        @Override
        public void run() {
            LOGGER.info("parsing terminology started");
            TerminologyParser terminologyParser = new TerminologyParser(terminologies.get(1));
            try {
                terminologyParser.execute();
                terminologyStandOff = terminologyParser.getStandOffTerminology();
                LOGGER.info("parsing terminology ended");
            } catch (IOException e) {
                LOGGER.error("error during terminology parsing", e);
            }
        }
    }
}
