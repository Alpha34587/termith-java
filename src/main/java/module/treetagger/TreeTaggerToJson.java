package module.treetagger;

/**
 * @author Simon Meoni
 *         Created on 01/09/16.
 */
public class TreeTaggerToJson {

    public TreeTaggerToJson(){}

    public void execute(){
        TreeTaggerWrapper treeTaggerWrapper = new TreeTaggerWrapper();
        Serialize serialize = new Serialize();

        treeTaggerWrapper.execute();
        serialize.execute();

    }
}
