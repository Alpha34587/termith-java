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
    private final int docIndex;
    private final int documentOffset;
    private final int numOfDocs;
    private final int cumulDocSize;
    private final boolean lastDoc;

    public TreeTaggerToJson(StringBuffer txt, String treeTaggerHome, String lang,
                            int totalSize, int docIndex, int documentOffset,
                            int numOfDocs, int cumulDocSize, boolean lastDoc){
        this.txt = txt;
        this.totalSize = totalSize;
        this.treeTaggerHome = treeTaggerHome;
        this.lang = lang;
        this.docIndex = docIndex;
        this.documentOffset = documentOffset;
        this.numOfDocs = numOfDocs;
        this.cumulDocSize = cumulDocSize;
        this.lastDoc = lastDoc;
    }

    public void execute() throws IOException {
        TreeTaggerWrapper treeTaggerWrapper = new TreeTaggerWrapper(txt,treeTaggerHome,
                new TreeTaggerParameter(false,lang, treeTaggerHome));
        treeTaggerWrapper.execute();
        Serialize serialize = new Serialize(treeTaggerWrapper.getTtOut(),totalSize);
        serialize.execute();

    }
}
