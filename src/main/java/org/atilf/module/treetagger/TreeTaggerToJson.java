package org.atilf.module.treetagger;

import org.atilf.models.termsuite.TextAnalyzer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Simon Meoni
 *         Created on 01/09/16.
 */
public class TreeTaggerToJson {

    private final Logger LOGGER = LoggerFactory.getLogger(TreeTaggerToJson.class.getName());
    private final StringBuilder _txt;
    private final String _jsonPath;
    private final String _treeTaggerHome;
    private final String _lang;
    private final TextAnalyzer _textAnalyzer;
    private final String _outputPath;

    public TreeTaggerToJson(StringBuilder txt, String jsonPath
            , String treeTaggerHome, String lang, TextAnalyzer textAnalyzer, String outputPath){
        _txt = txt;
        _jsonPath = jsonPath;
        _treeTaggerHome = treeTaggerHome;
        _lang = lang;
        _textAnalyzer = textAnalyzer;
        _outputPath = outputPath;
    }

    public void execute() throws IOException, InterruptedException {
        TreeTaggerWrapper treeTaggerWrapper = new TreeTaggerWrapper(_txt,
                new TreeTaggerParameter(false, _lang, _treeTaggerHome), _outputPath);
        treeTaggerWrapper.execute();
        JsonSerializer jsonSerializer = new JsonSerializer(treeTaggerWrapper.getTtOut(), _jsonPath, _txt, _textAnalyzer);
        jsonSerializer.execute();
        LOGGER.debug("write file " + _jsonPath);
    }
}
