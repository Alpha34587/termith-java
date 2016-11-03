package org.atilf.worker;

import org.atilf.models.termith.TermithIndex;
import org.atilf.module.tei.morphology.SyntaxGenerator;
import org.atilf.module.tools.FilesUtils;
import org.atilf.module.treetagger.CorpusAnalyzer;
import org.atilf.models.termsuite.TextAnalyzer;
import org.atilf.module.treetagger.TreeTaggerToJson;
import org.atilf.thread.enrichment.AnalyzeThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.CountDownLatch;

/**
 * @author Simon Meoni
 *         Created on 16/09/16.
 */
public class TreeTaggerWorker implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(AnalyzeThread.class.getName());
    private final String txtPath;
    private TermithIndex termithIndex;
    private CountDownLatch jsonCnt;
    private StringBuilder txt;
    private String jsonPath;
    private StringBuilder xml;
    private TextAnalyzer textAnalyzer;

    public TreeTaggerWorker(TermithIndex termithIndex, CorpusAnalyzer corpusAnalyzer, String id,
                            CountDownLatch jsonCnt) {
        this.termithIndex = termithIndex;
        this.jsonCnt = jsonCnt;
        this.txt = (StringBuilder) FilesUtils.readObject(termithIndex.getExtractedText().get(id));
        this.txtPath = termithIndex.getCorpus() + "/txt/" + id + ".txt";
        this.jsonPath = termithIndex.getCorpus() + "/json/" + id + ".json";
        this.textAnalyzer = corpusAnalyzer.get_analyzedTexts().get(id);
        this.xml = FilesUtils.readFile(termithIndex.getXmlCorpus().get(id));

        try {
            Files.delete(termithIndex.getExtractedText().get(id));
        } catch (IOException e) {
            LOGGER.error("could not delete file",e);
        }
    }

    @Override
    public void run() {
        TreeTaggerToJson treeTaggerToJson = new TreeTaggerToJson(
                txt,
                jsonPath,
                TermithIndex.getTreeTaggerHome(),
                TermithIndex.getLang(),
                textAnalyzer,
                TermithIndex.getOutputPath().toString()
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
                    FilesUtils.writeObject(syntaxGenerator.getTokenizeBody(),termithIndex.getCorpus()));
            termithIndex.getMorphoSyntaxStandOff().put(json.getName().replace(".json",""),
                    FilesUtils.writeObject(syntaxGenerator.getOffsetId(),termithIndex.getCorpus()));
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
