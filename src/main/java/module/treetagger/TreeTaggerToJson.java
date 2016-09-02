package module.treetagger;

import java.io.IOException;

/**
 * @author Simon Meoni
 *         Created on 01/09/16.
 */
public class TreeTaggerToJson {

    private final StringBuffer txt;
    private final int totalSize;
    private String treeTaggerHome;
    private String lang;

    public TreeTaggerToJson(StringBuffer txt, int totalSize, String treeTaggerHome, String lang){
        this.txt = txt;
        this.totalSize = totalSize;
        this.treeTaggerHome = treeTaggerHome;
        this.lang = lang;
    }

    public void execute() throws IOException {
        TreeTaggerWrapper treeTaggerWrapper = new TreeTaggerWrapper(txt,treeTaggerHome,
                new TreeTaggerParameter(false,lang, treeTaggerHome));
        treeTaggerWrapper.execute();
        Serialize serialize = new Serialize(treeTaggerWrapper.getTtOut(),totalSize);
        serialize.execute();

    }
}
