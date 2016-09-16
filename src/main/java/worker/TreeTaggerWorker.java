package worker;

import models.TermithIndex;
import module.tei.morphology.SyntaxGenerator;
import module.treetagger.TreeTaggerToJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import thread.JsonWriterInjector;

import java.io.File;
import java.io.IOException;

import static models.TermithIndex.lang;
import static models.TermithIndex.treeTaggerHome;

/**
 * @author Simon Meoni
 *         Created on 16/09/16.
 */
public class TreeTaggerWorker implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(JsonWriterInjector.class.getName());
    private final String txtPath;
    StringBuffer txt;
    String filePath;
    private TermithIndex termithIndex;
    StringBuffer xml;

    public TreeTaggerWorker(StringBuffer txt, String filePath,
                                  String txtPath, StringBuffer xml,TermithIndex termithIndex) {

        this.txt = txt;
        this.txtPath = txtPath;
        this.filePath = filePath;
        this.termithIndex = termithIndex;
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
                termithIndex.getTextAnalyzer()
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
            termithIndex.getTokenizeTeiBody()
                    .put(json.getName().replace(".json",""), syntaxGenerator.getTokenizeBody());
            termithIndex.getMorphoSyntaxStandOff()
                    .put(json.getName().replace(".json",""), syntaxGenerator.getOffsetId());

            LOGGER.info("tokenize xml body ended");
        } catch (IOException e) {
            LOGGER.info("error during parsing TreeTagger data", e);
        } catch (InterruptedException e) {
            LOGGER.info("error during Tree Tagger Process");
        }
    }
}
