package org.atilf.module.tei.morphology;

import org.atilf.models.MorphoSyntaxOffsetId;
import org.atilf.module.termsuite.json.TermsuiteJsonReader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Simon Meoni
 * Created on 17/08/16.
 */
public class SyntaxGenerator {

    private TermsuiteJsonReader termsuiteJsonReader;
    private StringBuilder txt;
    private StringBuilder xml;
    private StringBuilder tokenizeBody;
    private StringBuilder standoff;
    private List<MorphoSyntaxOffsetId> offsetId;

    public SyntaxGenerator(File json, StringBuilder txt, StringBuilder xml) {

        this.xml = xml;
        this.txt = txt;
        termsuiteJsonReader = new TermsuiteJsonReader(json);
        termsuiteJsonReader.parsing();
        tokenizeBody = new StringBuilder();
        standoff = new StringBuilder();
        offsetId = new ArrayList<>();

    }

    public StringBuilder getTokenizeBody() {
        return tokenizeBody;
    }

    public List<MorphoSyntaxOffsetId> getOffsetId() {
        return offsetId;
    }

    public void execute() throws Exception {

        SyntaxParser syntaxParser = new SyntaxParser(txt,xml,termsuiteJsonReader, offsetId);
        syntaxParser.execute();
        this.tokenizeBody = syntaxParser.getTokenizeBuffer();
        this.offsetId = syntaxParser.getOffsetId();
    }
}
