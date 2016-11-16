package org.atilf.module.tei.morphology;

import org.atilf.models.termsuite.MorphologyOffsetId;
import org.atilf.module.termsuite.morphology.MorphologyParser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * this class is a wrapper for the SyntaxParser class. it initialize all variables needed for SyntaxParser
 * and retrieve the result of syntaxWrapper
 * @author Simon Meoni
 * Created on 17/08/16.
 */
public class SyntaxParserWrapper {

    private MorphologyParser _morphologyParser;
    private StringBuilder _txt;
    private StringBuilder _xml;
    private StringBuilder _tokenizeBody = new StringBuilder();
    private List<MorphologyOffsetId> _offsetId = new ArrayList<>();

    /**
     * constructor of SyntaxWrapper
     * @param json the termsuite json file of a xml file. It contains the morphology information. This is based file
     *             for the tokenization and report of the POS tagging in the standOff morphology annotation in tei file
     * @param txt the extracted file of a xml file. it use to make some text alignment during tokenization proccessing
     * @param xml the xml file when the tokenization is performed
     * @see MorphologyParser
     */
    public SyntaxParserWrapper(File json, StringBuilder txt, StringBuilder xml) {
        _xml = xml;
        _txt = txt;
        /*
        deserialize json file
         */
        _morphologyParser = new MorphologyParser(json);
        _morphologyParser.execute();
    }

    /**
     * get the tokenize body
     * @return StringBuilder of tokenize body
     */
    public StringBuilder getTokenizeBody() {
        return _tokenizeBody;
    }

    /**
     * get the offset of each word in a text in order to report the terminology in the tei format and the morphology
     * @return the list of offset of each word in the file
     * @see MorphologyOffsetId
     */
    public List<MorphologyOffsetId> getOffsetId() {
        return _offsetId;
    }

    /**
     *  execute method of syntaxParser and retrieve the result of syntaxParser
     * @throws Exception catch exception of SyntaxParser object
     */
    public void execute() throws Exception {

        SyntaxParser syntaxParser = new SyntaxParser(_txt, _xml, _morphologyParser, _offsetId);
        syntaxParser.execute();
        _tokenizeBody = syntaxParser.getTokenizeBuffer();
        _offsetId = syntaxParser.getOffsetId();
    }
}
