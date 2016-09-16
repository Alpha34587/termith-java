package module.treetagger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * @author Simon Meoni
 *         Created on 01/09/16.
 */
public class TreeTaggerToJson {

    private final Logger LOGGER = LoggerFactory.getLogger(TreeTaggerToJson.class.getName());
    private final StringBuffer txt;
    private final String jsonPath;
    private final String treeTaggerHome;
    private final String lang;
    private final TextAnalyzer textAnalyzer;

    public TreeTaggerToJson(StringBuffer txt, String jsonPath
            , String treeTaggerHome, String lang, TextAnalyzer textAnalyzer){
        this.txt = txt;
        this.jsonPath = jsonPath;
        this.treeTaggerHome = treeTaggerHome;
        this.lang = lang;
        this.textAnalyzer = textAnalyzer;
    }

    public void execute() throws IOException, InterruptedException {
        TreeTaggerWrapper treeTaggerWrapper = new TreeTaggerWrapper(txt,treeTaggerHome,
                new TreeTaggerParameter(false,lang, treeTaggerHome));
        treeTaggerWrapper.execute();
        Serialize serialize = new Serialize(treeTaggerWrapper.getTtOut(),jsonPath,txt, textAnalyzer);
        serialize.execute();
        LOGGER.info("write file " + jsonPath);
    }
}
