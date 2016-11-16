package org.atilf.module.tei.morphology;

import org.atilf.models.termsuite.MorphologyOffsetId;
import org.atilf.module.termsuite.morphology.MorphologyParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author Simon Meoni
 *         Created on 24/08/16.
 */
public class SyntaxParser {

    private final static Logger LOGGER = LoggerFactory.getLogger(SyntaxParser.class);
    private StringBuilder _xml;
    private StringBuilder _txt;
    private StringBuilder _tokenizeBuffer;
    private MorphologyParser _morphologyParser;
    private Queue<Character> xmlCharacterQueue;
    private Integer[] offset = new Integer[2];
    private List<MorphologyOffsetId> _offsetId;


    SyntaxParser(StringBuilder txt, StringBuilder xml, MorphologyParser morphologyParser,
                 List<MorphologyOffsetId> offsetId) {
        _xml = xml;
        _txt = txt;
        _morphologyParser = morphologyParser;
        _tokenizeBuffer = new StringBuilder();
        _offsetId = offsetId;
    }

    SyntaxParser(StringBuilder xml){
        this._xml = xml;
    }

    public StringBuilder getXml() {
        return _xml;
    }

    StringBuilder getTokenizeBuffer() {
        return _tokenizeBuffer;
    }

    public Integer[] getOffset() {
        return offset;
    }

    public List<MorphologyOffsetId> getOffsetId() { return _offsetId; }

    public void execute() throws Exception {
        teiBodySplit();
        teiWordTokenizer();
    }

    void teiBodySplit(){
        _xml = new StringBuilder(
                _xml.toString()
                        .split("(?=(<text>|<text\\s.*>))")[1]
                        .split("(?<=(</text>))")[0]
        );

    }

    private void fillXmlCharacterQueue() {
        xmlCharacterQueue = new LinkedList<>();
        for (Character c : _xml.toString().toCharArray()){
            this.xmlCharacterQueue.add(c);
        }
    }

    void teiWordTokenizer() throws Exception {
        LOGGER.debug("Tokenization Started");
        offset[0] = 0;
        offset[1] = 1;
        int id = 1;
        _morphologyParser.pollToken();
        Character ch;
        fillXmlCharacterQueue();
        try {
            while (!xmlCharacterQueue.isEmpty()) {
                ch = xmlCharacterQueue.poll();
                if (ch == '<') {
                    id = waitUntilTagEnd(ch, id);
                } else {
                    checkTextAlignment(ch);
                    id = tokenInjector(ch, id);
                    countOffset();
                }

                if (offset[0] > _morphologyParser.getCurrentTokenEnd()) {
                    _morphologyParser.pollToken();
                }
            }
            LOGGER.debug("Tokenization Ended");
        }

        catch (Exception e){
            throw new Exception(e);
        }
    }

    private void countOffset() {
        offset[0] += 1;
        offset[1] += 1;
    }


    private int waitUntilTagEnd(Character ch, int id) {

        if (_morphologyParser.getCurrentTokenBegin() == -2){
            _tokenizeBuffer.append("</w>");
            id++;
            MorphologyOffsetId.addId(_offsetId,id);
        }

        while(ch != '>' ||
                (!xmlCharacterQueue.isEmpty() && xmlCharacterQueue.peek() == '<') ){
            _tokenizeBuffer.append(ch);
            ch = xmlCharacterQueue.poll();
        }

        _tokenizeBuffer.append(ch);
        checkIfSymbol(ch);

        if(_morphologyParser.getCurrentTokenBegin() == -2){
            _tokenizeBuffer.append("<w xml:id=\"" + "t").append(id).append("\">");
        }

        return id;
    }

    private void checkIfSymbol(Character ch) {
        if (ch == '&'){
            while ((ch = xmlCharacterQueue.poll()) != ';'){
                _tokenizeBuffer.append(ch);
            }
            _tokenizeBuffer.append(ch);
        }

    }

    private int tokenInjector(Character ch, int id) {

        if (offset[0] == _morphologyParser.getCurrentTokenBegin()){
            _tokenizeBuffer.append("<w xml:id=\"" + "t").append(id).append("\">");
            MorphologyOffsetId.addNewOffset(
                    _offsetId,
                    _morphologyParser.getCurrentTokenBegin(),
                    _morphologyParser.getCurrentTokenEnd(),
                    _morphologyParser.getCurrentLemma(),
                    _morphologyParser.getCurrentPos(),
                    id
            );
            _morphologyParser.setCurrentTokenBegin(-2);
        }

        _tokenizeBuffer.append(ch);
        checkIfSymbol(ch);

        if (offset[1] == _morphologyParser.getCurrentTokenEnd()){
            _tokenizeBuffer.append("</w>");
            _morphologyParser.pollToken();
            return id + 1;
        }
        return id;

    }

    private void checkTextAlignment(Character ch){
        while (offset[0] < _txt.length() - 1 && _txt.charAt(offset[0]) == '\n' && _txt.charAt(offset[0]) != ch) {
                countOffset();
        }
    }
}
