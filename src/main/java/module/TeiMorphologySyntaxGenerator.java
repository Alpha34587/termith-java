package module;

import java.io.File;

/**
 * @author Simon Meoni
 * Created on 17/08/16.
 */
public class TeiMorphologySyntaxGenerator {

    private File json;
    private StringBuffer txt;
    private StringBuffer xml;
    private StringBuffer tokenizeBody;
    private StringBuffer standoff;
    private TermsuiteJsonReader termsuiteJsonReader;

    public TeiMorphologySyntaxGenerator(File json, StringBuffer txt, StringBuffer xml) {
        this.json = json;
        this.txt = txt;
        this.xml = xml;
        this.tokenizeBody = new StringBuffer();
        this.standoff = new StringBuffer();
    }

    public void execute() {

    }
}
