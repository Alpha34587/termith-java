package module.tei.morphology;

import module.termsuite.TermsuiteJsonReader;

import java.io.File;

/**
 * @author Simon Meoni
 * Created on 17/08/16.
 */
public class SyntaxGenerator {

    private TermsuiteJsonReader termsuiteJsonReader;
    private StringBuffer txt;
    private StringBuffer xml;
    private StringBuffer tokenizeBody;
    private StringBuffer standoff;

    public SyntaxGenerator(File json, StringBuffer txt, StringBuffer xml) {
        this.termsuiteJsonReader = new TermsuiteJsonReader(json);
        termsuiteJsonReader.parsing();
        this.txt = txt;
        this.xml = xml;
        this.tokenizeBody = new StringBuffer();
        this.standoff = new StringBuffer();
    }

    public StringBuffer getTokenizeBody() {
        return tokenizeBody;
    }

    public void execute() {
        SyntaxParser syntaxParser = new SyntaxParser(txt,xml,termsuiteJsonReader);
        syntaxParser.execute();
        this.tokenizeBody = syntaxParser.getTokenizeBuffer();
    }
}
