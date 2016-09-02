package module.treetagger;

/**
 * @author Simon Meoni
 *         Created on 01/09/16.
 */
public class TreeTaggerWrapper {

    private final StringBuffer txt;

    private final StringBuilder ttOut;

    public TreeTaggerWrapper(StringBuffer txt) {

        this.txt = txt;
        this.ttOut = new StringBuilder();
    }

    public StringBuilder getTtOut() {
        return ttOut;
    }

    public void execute() {

    }
}
