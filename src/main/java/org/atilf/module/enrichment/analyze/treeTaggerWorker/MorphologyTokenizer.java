package org.atilf.module.enrichment.analyze.treeTaggerWorker;

import org.atilf.models.MorphologyParser;
import org.atilf.models.termsuite.MorphologyOffsetId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * the MorphologyTokenizer tokenize the text element of the xml file according to the _morphologyParser and _txt fields.
 * the result of this module is the tokenize text element of xml file and list of id associated for all words
 * in the text element.
 * @author Simon Meoni
 *         Created on 24/08/16.
 */
public class MorphologyTokenizer {

    private final static Logger LOGGER = LoggerFactory.getLogger(MorphologyTokenizer.class);
    private StringBuilder _xml;
    private StringBuilder _txt;
    private MorphologyParser _morphologyParser;
    private Queue<Character> _xmlCharacterQueue = new LinkedList<>();
    private List<MorphologyOffsetId> _offsetId = new ArrayList<>();
    private Integer[] _offset = new Integer[2];
    private StringBuilder _tokenizeBuffer = new StringBuilder();


    /**
     * constructor for MorphologyTokenizer
     * @param txt the extracted text of xml file
     * @param xml the xml file
     * @param json json file associated to the xml file
     */
    public MorphologyTokenizer(StringBuilder txt, StringBuilder xml, File json) {
        _xml = xml;
        _txt = txt;
        _morphologyParser = new MorphologyParser(json);
        _morphologyParser.execute();
    }

    /**
     * constructor for MorphologyTokenizer
     * @param txt the extracted text of xml file
     * @param xml the xml file
     * @param parser the morphology parser object when the execution of parsing is externalized
     */
    MorphologyTokenizer(StringBuilder txt, StringBuilder xml, MorphologyParser parser) {
        _xml = xml;
        _txt = txt;
        _morphologyParser = parser;
    }

    /**
     * constructor for MorphologyTokenizer
     * @param xml the xml file
     */
    MorphologyTokenizer(StringBuilder xml){
        _xml = xml;
    }

    /**
     * getter for xml file
     * @return xml file content
     */
    public StringBuilder getXml() {
        return _xml;
    }

    /**
     * getter for tokenize text
     * @return tokenize text
     */
    public StringBuilder getTokenizeBuffer() {
        return _tokenizeBuffer;
    }


    /**
     * getter for _offsetId
     * @return return List<MorphologyOffsetId>
     */
    public List<MorphologyOffsetId> getOffsetId() { return _offsetId; }

    /**
     * call teiTextSplitter and teiWordTokenizer
     * @throws Exception thrown teiWordTokenizer method exception
     */
    public void execute() throws Exception {
        teiTextSplitter();
        teiWordTokenizer();
    }

    /**
     * keep only the text element from the xml file
     */
    void teiTextSplitter(){
        _xml = new StringBuilder(
                _xml.toString()
                        .split("(?=(<text>|<text\\s.*>))")[1]
                        .split("(?<=(</text>))")[0]
        );

    }

    /**
     * take all the character from text element and add it to _xmlCharacterQueue
     */
    private void fillXmlCharacterQueue() {
        for (Character c : _xml.toString().toCharArray()){
            _xmlCharacterQueue.add(c);
        }
    }

    /**
     * tokenize words in text element. the method includes xml w tag(s) around each words. it supports  words splits by
     * several xml tags.
     * @throws Exception thrown teiWordTokenizer method exception
     */
    void teiWordTokenizer() throws Exception {
        LOGGER.debug("Tokenization Started");
        /*
         initialize offset and id
         */
        _offset[0] = 0;
        _offset[1] = 1;
        int id = 1;
        /*
         get the first word of morphologyParser
         */
        _morphologyParser.pollToken();
        /*
         the text is converted into Queue of character
         */
        Character ch;
        fillXmlCharacterQueue();
        try {
            /*
            browse of character queue
             */
            while (!_xmlCharacterQueue.isEmpty()) {
                ch = _xmlCharacterQueue.poll();
                /*
                if the character belongs to a xml tag
                 */
                if (ch == '<') {
                    id = waitUntilTagEnd(ch, id);
                }
                /*
                update offset & check alignment of with the plain text
                the offset is updated if the xmlCharacter is not equals to the txt character at this offset.
                 */
                else {
                    checkTextAlignment(ch);
                    /*
                    try to inject w element
                     */
                    id = tokenInjector(ch, id);
                    countOffset();
                }

                if (_offset[0] > _morphologyParser.getCurrentTokenEnd()) {
                    /*
                    get next token if the begin offset is superior to the ending offset of
                     */
                    _morphologyParser.pollToken();
                }
            }
            LOGGER.debug("Tokenization Ended");
        }

        catch (Exception e){
            throw new Exception(e);
        }
    }

    /**
     * increment offset
     */
    private void countOffset() {
        _offset[0] += 1;
        _offset[1] += 1;
    }


    /**
     * ignore xml tag and insert w tags if there is an non-closed w tag element wrote previously
     * @param ch the current character
     * @param id the current id of the current w element
     * @return the id of the current w element
     */
    private int waitUntilTagEnd(Character ch, int id) {

        /*
        closed w tag
         */
        if (_morphologyParser.getCurrentTokenBegin() == -2){
            _tokenizeBuffer.append("</w>");
            id++;
            MorphologyOffsetId.addId(_offsetId,id);
        }

        /*
        write xml tag until the end of the tag is met
         */
        while(ch != '>' ||
                (!_xmlCharacterQueue.isEmpty() && _xmlCharacterQueue.peek() == '<') ){
            _tokenizeBuffer.append(ch);
            ch = _xmlCharacterQueue.poll();
        }

        /*
        write last character and check if it is not a xml special character
         */
        _tokenizeBuffer.append(ch);
        checkIfSpecialChar(ch);

        /*
        write a new w tag
         */
        if(_morphologyParser.getCurrentTokenBegin() == -2){
            _tokenizeBuffer.append("<w xml:id=\"" + "t").append(id).append("\">");
        }

        return id;
    }

    /*
    ignore xml special character in order to not misaligned the offset
     */
    private void checkIfSpecialChar(Character ch) {
        if (ch == '&'){
            while ((ch = _xmlCharacterQueue.poll()) != ';'){
                _tokenizeBuffer.append(ch);
            }
            _tokenizeBuffer.append(ch);
        }

    }

    /**
     * add a new w xml tag or closed an xml tag
     * @param ch the current character
     * @param id the current id of w element
     * @return return the w id
     */
    private int tokenInjector(Character ch, int id) {

        /*
        write the open w tag
         */
        if (_offset[0] == _morphologyParser.getCurrentTokenBegin()){
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

        /*
        write the character
         */
        _tokenizeBuffer.append(ch);
        checkIfSpecialChar(ch);

        /*
        write the closed w tag
         */
        if (_offset[1] == _morphologyParser.getCurrentTokenEnd()){
            _tokenizeBuffer.append("</w>");
            _morphologyParser.pollToken();
            return id + 1;
        }
        return id;

    }

    /**
     * check if the offset is aligned according to the _txt field
     * @param ch the current character
     */
    private void checkTextAlignment(Character ch){
        while (_offset[0] < _txt.length() - 1 && _txt.charAt(_offset[0]) == '\n' && _txt.charAt(_offset[0]) != ch) {
                countOffset();
        }
    }
}
