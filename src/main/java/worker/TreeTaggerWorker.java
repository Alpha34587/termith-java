package worker;

import models.TermithIndex;
import module.tei.morphology.SyntaxGenerator;
import module.treetagger.CorpusAnalyzer;
import module.treetagger.TextAnalyzer;
import module.treetagger.TreeTaggerToJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import thread.AnalyzeThread;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import static models.TermithIndex.lang;
import static models.TermithIndex.treeTaggerHome;

/**
 * @author Simon Meoni
 *         Created on 16/09/16.
 */
public class TreeTaggerWorker implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyzeThread.class.getName());
    private final String txtPath;
    private TermithIndex termithIndex;
    private CountDownLatch jsonCnt;
    StringBuffer txt;
    String jsonPath;
    StringBuffer xml;
    TextAnalyzer textAnalyzer;

    public TreeTaggerWorker(TermithIndex termithIndex, CorpusAnalyzer corpusAnalyzer, String name,
                            CountDownLatch jsonCnt) {
        this.termithIndex = termithIndex;
        this.jsonCnt = jsonCnt;
        this.txt = termithIndex.getExtractedText().get(name);
        this.txtPath = termithIndex.getCorpus() + "/txt/" + name + ".txt";
        this.jsonPath = termithIndex.getCorpus() + "/json/" + name + ".json";
        this.textAnalyzer = corpusAnalyzer.getAnalyzedTexts().get(name);
        this.xml = termithIndex.getXmlCorpus().get(name);
    }

    @Override
    public void run() {
        TreeTaggerToJson treeTaggerToJson = new TreeTaggerToJson(
                txt,
                jsonPath,
                treeTaggerHome,
                lang,
                textAnalyzer
        );


        try {
            LOGGER.info("converting TreeTagger output started file : " + txtPath);
            treeTaggerToJson.execute();
            jsonCnt.countDown();
            LOGGER.info("converting TreeTagger output finished file : " + txtPath);

            LOGGER.info("tokenization and morphosyntax tasks started file : " + jsonPath);
            File json = new File(jsonPath);
            SyntaxGenerator syntaxGenerator = new SyntaxGenerator(
                    json,txt,xml
            );
            syntaxGenerator.execute();
            termithIndex.getTokenizeTeiBody().put(json.getName().replace(".json",""), syntaxGenerator.getTokenizeBody());
            termithIndex.getMorphoSyntaxStandOff().put(json.getName().replace(".json",""), syntaxGenerator.getOffsetId());
            LOGGER.info("tokenization and morphosyntax tasks finished file : " + jsonPath);

        } catch (IOException e) {
            LOGGER.info("error during parsing TreeTagger data", e);
        } catch (InterruptedException e) {
            LOGGER.info("error during Tree Tagger Process");
        }
    }
}
