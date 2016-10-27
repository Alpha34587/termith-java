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

    private TermsuiteJsonReader _termsuiteJsonReader;
    private StringBuilder _txt;
    private StringBuilder _xml;
    private StringBuilder _tokenizeBody = new StringBuilder();
    private StringBuilder _standoff = new StringBuilder();
    private List<MorphoSyntaxOffsetId> _offsetId = new ArrayList<>();

    public SyntaxGenerator(File json, StringBuilder txt, StringBuilder xml) {
        _xml = xml;
        _txt = txt;
        _termsuiteJsonReader = new TermsuiteJsonReader(json);
        _termsuiteJsonReader.parsing();
    }

    public StringBuilder get_tokenizeBody() {
        return _tokenizeBody;
    }

    public List<MorphoSyntaxOffsetId> get_offsetId() {
        return _offsetId;
    }

    public void execute() throws Exception {

        SyntaxParser syntaxParser = new SyntaxParser(_txt, _xml, _termsuiteJsonReader, _offsetId);
        syntaxParser.execute();
        _tokenizeBody = syntaxParser.get_tokenizeBuffer();
        _offsetId = syntaxParser.get_offsetId();
    }
}
