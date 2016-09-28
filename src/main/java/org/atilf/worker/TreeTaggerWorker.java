package org.atilf.worker;

import org.atilf.models.TermithIndex;
import org.atilf.module.tei.morphology.SyntaxGenerator;
import org.atilf.module.tools.FilesUtilities;
import org.atilf.module.treetagger.CorpusAnalyzer;
import org.atilf.module.treetagger.TextAnalyzer;
import org.atilf.module.treetagger.TreeTaggerToJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.atilf.thread.AnalyzeThread;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;

import static org.atilf.models.TermithIndex.lang;
import static org.atilf.models.TermithIndex.treeTaggerHome;

/**
 * @author Simon Meoni
 *         Created on 16/09/16.
 */
public class TreeTaggerWorker implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyzeThread.class.getName());
    private final String txtPath;
    private final String name;
    private TermithIndex termithIndex;
    private CountDownLatch jsonCnt;
    StringBuilder txt;
    String jsonPath;
    StringBuilder xml;
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
        this.name = name;
    }

    @Override
    public void run() {
        TreeTaggerToJson treeTaggerToJson = new TreeTaggerToJson(
                txt,
                jsonPath,
                treeTaggerHome,
                lang,
                textAnalyzer,
                TermithIndex.outputPath.toString()
        );


        try {
            LOGGER.debug("converting TreeTagger output started file : " + txtPath);
            treeTaggerToJson.execute();
            jsonCnt.countDown();
            termithIndex.getSerializeJson().add(Paths.get(jsonPath));
            LOGGER.debug("converting TreeTagger output finished file : " + txtPath);

            LOGGER.debug("tokenization and morphosyntax tasks started file : " + jsonPath);
            File json = new File(jsonPath);
            SyntaxGenerator syntaxGenerator = new SyntaxGenerator(json,txt,xml);
            syntaxGenerator.execute();
            termithIndex.getTokenizeTeiBody().put(json.getName().replace(".json",""),
                    FilesUtilities.writeObject(syntaxGenerator.getTokenizeBody(),termithIndex.getCorpus()));
            termithIndex.getMorphoSyntaxStandOff().put(json.getName().replace(".json",""),
                    FilesUtilities.writeObject(syntaxGenerator.getOffsetId(),termithIndex.getCorpus()));
            termithIndex.getExtractedText().remove(name);
            LOGGER.debug("tokenization and morphosyntax tasks finished file : " + jsonPath);

        } catch (IOException e) {
            LOGGER.error("error during parsing TreeTagger data", e);
        } catch (InterruptedException e) {
            LOGGER.error("error during Tree Tagger Process : ",e);
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            LOGGER.error("error during xml tokenization parsing",e);
        }
    }
}
