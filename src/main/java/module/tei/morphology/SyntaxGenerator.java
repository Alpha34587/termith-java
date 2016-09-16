package module.tei.morphology;

import models.MorphoSyntaxOffsetId;
import module.termsuite.json.TermsuiteJsonReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
    private List<MorphoSyntaxOffsetId> offsetId;

    public SyntaxGenerator(File json, StringBuffer txt, StringBuffer xml) {

        this.xml = xml;
        this.txt = txt;
        termsuiteJsonReader = new TermsuiteJsonReader(json);
        termsuiteJsonReader.parsing();
        tokenizeBody = new StringBuffer();
        standoff = new StringBuffer();
        offsetId = new ArrayList<>();

    }

    public StringBuffer getTokenizeBody() {
        return tokenizeBody;
    }

    public List<MorphoSyntaxOffsetId> getOffsetId() {
        return offsetId;
    }

    public void execute() {

        SyntaxParser syntaxParser = new SyntaxParser(txt,xml,termsuiteJsonReader, offsetId);
        syntaxParser.execute();
        this.tokenizeBody = syntaxParser.getTokenizeBuffer();
        this.offsetId = syntaxParser.getOffsetId();
    }
}
