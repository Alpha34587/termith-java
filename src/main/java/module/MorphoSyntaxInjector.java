package module;

import java.io.File;

/**
 * @author Simon Meoni
 * Created on 17/08/16.
 */
public class MorphoSyntaxInjector {

    private File json;
    private StringBuffer txt;
    private StringBuffer xml;

    public MorphoSyntaxInjector(File json, StringBuffer txt, StringBuffer xml) {
        this.json = json;
        this.txt = txt;
        this.xml = xml;
    }

    public void execute() {
    }
}
