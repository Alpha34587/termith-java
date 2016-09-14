package thread;

import models.MorphoSyntaxOffsetId;
import models.TermithIndex;
import module.tei.morphology.SyntaxGenerator;
import module.tools.FilesUtilities;
import module.treetagger.CorpusAnalyzer;
import module.treetagger.TagNormalizer;
import module.treetagger.TextAnalyzer;
import module.treetagger.TreeTaggerToJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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
    private Map<String, StringBuffer> tokenizeTeiBody;
    private Map<String, Path> JsonTreeTagger;
    private final Path corpus;
    private final String lang;
    private final List terminologies;
    private Map<String, List<MorphoSyntaxOffsetId>> morphoSyntaxStandOff;

    public JsonWriterInjector(TermithIndex termithIndex)
            throws IOException {
        this(DEFAULT_POOL_SIZE, termithIndex);
    }

    public JsonWriterInjector(int poolSize, TermithIndex termithIndex) throws IOException {
        termithIndex.setCorpus(Paths.get(FilesUtilities.createTemporaryFolder("corpus")));
        treeTaggerHome = termithIndex.getTreeTaggerHome();
        executorService = Executors.newFixedThreadPool(poolSize);
        extractedText = termithIndex.getExtractedText();
        corpus = termithIndex.getCorpus();
        lang = termithIndex.getLang();
        xmlCorpus = termithIndex.getXmlCorpus();
        terminologies = termithIndex.getTerminologies();
        JsonTreeTagger = termithIndex.getJsonTreeTagger();
        tokenizeTeiBody = termithIndex.getTokenizeTeiBody();
        morphoSyntaxStandOff = termithIndex.getMorphoSyntaxStandOff();
        TagNormalizer.initTag(lang);

        LOGGER.info("temporary folder created: " + corpus);
        Files.createDirectories(Paths.get(corpus + "/json"));
        Files.createDirectories(Paths.get(corpus + "/txt"));
        LOGGER.info("create temporary text files in " + corpus + "/txt folder");
        FilesUtilities.createFiles(corpus + "/txt", extractedText, "txt");
    }

    public Path getCorpus() {
        return corpus;
    }

    public Map<String, Path> getJsonTreeTagger() {
        return JsonTreeTagger;
    }

    public void execute() throws InterruptedException  {
        CorpusAnalyzer corpusAnalyzer = new CorpusAnalyzer(extractedText);
        extractedText.forEach((key,txt) -> {
                    int index = 1;
                    executorService.submit(new TreeTaggerToJsonWorker(txt, corpus + "/txt/" + key + ".txt",
                            corpus + "/json/" + key + ".json",
                            xmlCorpus.get(key),
                            corpusAnalyzer.getAnalyzedTexts().get(key)));
                    index++;
                }
        );
        LOGGER.info("Waiting executors to finish");
        executorService.shutdown();
        executorService.awaitTermination(1L,TimeUnit.DAYS);
    }

    private class TreeTaggerToJsonWorker implements Runnable {
        private final String txtPath;
        StringBuffer txt;
        String filePath;
        StringBuffer xml;
        TextAnalyzer textAnalyzer;

        public TreeTaggerToJsonWorker(StringBuffer txt, String filePath,
                                      String txtPath, StringBuffer xml,TextAnalyzer textAnalyzer) {

            this.txt = txt;
            this.txtPath = txtPath;
            this.filePath = filePath;
            this.textAnalyzer = textAnalyzer;
            this.xml = xml;
        }

        @Override
        public void run() {
            LOGGER.info("new treetagger to json task started");
            TreeTaggerToJson treeTaggerToJson = new TreeTaggerToJson(
                    txt,
                    txtPath,
                    filePath,
                    treeTaggerHome,
                    lang,
                    textAnalyzer
            );

            LOGGER.info("treetagger to json task ended");

            try {
                treeTaggerToJson.execute();
                LOGGER.info("tokenize xml body started");
                File json = new File(txtPath);
                SyntaxGenerator syntaxGenerator = new SyntaxGenerator(
                        json,txt,xml
                );
                syntaxGenerator.execute();
                tokenizeTeiBody.put(json.getName().replace(".json",""), syntaxGenerator.getTokenizeBody());
                morphoSyntaxStandOff.put(json.getName().replace(".json",""), syntaxGenerator.getOffsetId());
                LOGGER.info("tokenize xml body ended");
            } catch (IOException e) {
                LOGGER.info("error during parsing TreeTagger data", e);
            } catch (InterruptedException e) {
                LOGGER.info("error during Tree Tagger Process");
            }
        }
    }
}
