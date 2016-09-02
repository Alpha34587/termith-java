package module.treetagger;

/**
 * @author Simon Meoni
 *         Created on 01/09/16.
 */
public class TreeTaggerToJson {

    private final StringBuffer txt;
    private final int totalSize;

    public TreeTaggerToJson(StringBuffer txt, int totalSize){
        this.txt = txt;
        this.totalSize = totalSize;
    }

    public void execute(){
        TreeTaggerWrapper treeTaggerWrapper = new TreeTaggerWrapper(txt);
        treeTaggerWrapper.execute();
        Serialize serialize = new Serialize(treeTaggerWrapper.getTtOut(),totalSize);

        serialize.execute();

    }
}
