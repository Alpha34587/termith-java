package org.atilf.module.treetagger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;

import static org.atilf.models.TermithIndex.outputPath;

/**
 * @author Simon Meoni
 *         Created on 01/09/16.
 */
public class TreeTaggerToJson {

    private final Logger LOGGER = LoggerFactory.getLogger(TreeTaggerToJson.class.getName());
    private final StringBuilder txt;
    private final String jsonPath;
    private final String treeTaggerHome;
    private final String lang;
    private final TextAnalyzer textAnalyzer;
    private final String outputPath;

    public TreeTaggerToJson(StringBuilder txt, String jsonPath
            , String treeTaggerHome, String lang, TextAnalyzer textAnalyzer, String outputPath){
        this.txt = txt;
        this.jsonPath = jsonPath;
        this.treeTaggerHome = treeTaggerHome;
        this.lang = lang;
        this.textAnalyzer = textAnalyzer;
        this.outputPath = outputPath;
    }

    public void execute() throws IOException, InterruptedException {
        TreeTaggerWrapper treeTaggerWrapper = new TreeTaggerWrapper(txt,treeTaggerHome,
                new TreeTaggerParameter(false,lang, treeTaggerHome), outputPath);
        treeTaggerWrapper.execute();
        Serialize serialize = new Serialize(treeTaggerWrapper.getTtOut(),jsonPath,txt, textAnalyzer);
        serialize.execute();
        LOGGER.debug("write file " + jsonPath);
    }
}
