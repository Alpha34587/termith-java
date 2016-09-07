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
    private final String filePath;
    private final String txtPath;
    private final String treeTaggerHome;
    private final String lang;
    private final CorpusAnalyzer corpusAnalyzer;

    public TreeTaggerToJson(StringBuffer txt, String filePath, String txtPath
            ,String treeTaggerHome, String lang, CorpusAnalyzer corpusAnalyzer){
        this.txt = txt;
        this.filePath = filePath;
        this.txtPath = txtPath;
        this.treeTaggerHome = treeTaggerHome;
        this.lang = lang;
        this.corpusAnalyzer = corpusAnalyzer;
    }

    public void execute() throws IOException, InterruptedException {
        TreeTaggerWrapper treeTaggerWrapper = new TreeTaggerWrapper(txt,treeTaggerHome,
                new TreeTaggerParameter(false,lang, treeTaggerHome));
        treeTaggerWrapper.execute();
        Serialize serialize = new Serialize(treeTaggerWrapper.getTtOut(),txtPath,filePath,txt,corpusAnalyzer);
        serialize.execute();
        LOGGER.info("write file " + filePath);
    }
}
