package module.tei.morphology;

import models.OffsetId;
import module.termsuite.TermsuiteJsonReader;
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
    private StringBuffer xml;
    private StringBuffer txt;
    private StringBuffer tokenizeBuffer;
    private TermsuiteJsonReader termsuiteJsonReader;
    private Queue<Character> xmlCharacterQueue;
    private Integer[] offset = new Integer[2];
    private List<OffsetId> offsetId;


    //TODO return a list of tag with {begin, end and the Tag id for each token
    SyntaxParser(StringBuffer txt, StringBuffer xml, TermsuiteJsonReader termsuiteJsonReader, List<OffsetId> offsetId) {
        this.xml = xml;
        this.txt = txt;
        this.termsuiteJsonReader = termsuiteJsonReader;
        this.tokenizeBuffer = new StringBuffer();
        this.offsetId = offsetId;
    }

    SyntaxParser(StringBuffer xml){
        this.xml = xml;
    }

    public StringBuffer getXml() {
        return xml;
    }

    StringBuffer getTokenizeBuffer() {
        return tokenizeBuffer;
    }

    public Integer[] getOffset() {
        return offset;
    }

    public List<OffsetId> getOffsetId() { return offsetId; }

    public void execute(){
        teiBodyspliter();
        teiWordTokenizer();
    }

    void teiBodyspliter(){
        xml = new StringBuffer(xml.toString().split("(?=(<text>|<text\\s.*>))")[1]);

    }

    private void fillXmlCharacterQueue() {
        xmlCharacterQueue = new LinkedList<>();
        for (Character c : xml.toString().toCharArray()){
            this.xmlCharacterQueue.add(c);
        }
    }

    void teiWordTokenizer() {
        LOGGER.info("Tokenization Started");
        offset[0] = 0;
        offset[1] = 1;
        int id = 1;
        termsuiteJsonReader.pollToken();
        Character ch;
        fillXmlCharacterQueue();
        while (!xmlCharacterQueue.isEmpty()) {
            ch = xmlCharacterQueue.poll();
            if (ch == '<') {
               id = waitUntilTagEnd(ch,id);
            }
            else {
                checkTextAlignment(ch);
                id = tokenInjector(ch, id);
                countOffset();

            }

            if (offset[0] > termsuiteJsonReader.getCurrentTokenEnd()){
                termsuiteJsonReader.pollToken();
            }
        }
        LOGGER.info("Tokenization Ended");
    }

    private void countOffset() {
            offset[0] += 1;
            offset[1] += 1;
    }


    private int waitUntilTagEnd(Character ch, int id) {

        if (termsuiteJsonReader.getCurrentTokenBegin() == -2){
            tokenizeBuffer.append("</w>");
            id++;
            OffsetId.addId(offsetId,id);
        }

        while(ch != '>' ||
                (!xmlCharacterQueue.isEmpty() && xmlCharacterQueue.peek() == '<') ){
            tokenizeBuffer.append(ch);
            ch = xmlCharacterQueue.poll();
        }

        tokenizeBuffer.append(ch);
        checkIfSymbol(ch);

        if(termsuiteJsonReader.getCurrentTokenBegin() == -2){
            tokenizeBuffer.append("<w xml:id=\"" + "t").append(id).append("\">");
        }

        return id;
    }

    private void checkIfSymbol(Character ch) {
        if (ch == '&'){
            while ((ch = xmlCharacterQueue.poll()) != ';'){
                tokenizeBuffer.append(ch);
            }
            tokenizeBuffer.append(ch);
        }

    }

    private int tokenInjector(Character ch, int id) {

        if (offset[0] == termsuiteJsonReader.getCurrentTokenBegin()){
            tokenizeBuffer.append("<w xml:id=\"" + "t").append(id).append("\">");
            OffsetId.addNewOffset(
                    offsetId,
                    termsuiteJsonReader.getCurrentTokenBegin(),
                    termsuiteJsonReader.getCurrentTokenEnd(),
                    id
            );
            termsuiteJsonReader.setCurrentTokenBegin(-2);
        }

        tokenizeBuffer.append(ch);
        checkIfSymbol(ch);

        if (offset[1] == termsuiteJsonReader.getCurrentTokenEnd()){
            tokenizeBuffer.append("</w>");
            termsuiteJsonReader.pollToken();
            return id + 1;
        }
        return id;

    }

    private void checkTextAlignment(Character ch){
        if (offset[0] < txt.length() - 1 && txt.charAt(offset[0]) == '\n') {
            while (ch != txt.charAt(offset[0]))
                countOffset();
            }
    }
}
