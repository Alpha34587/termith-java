package module.tei.morphology;

import module.termsuite.TermsuiteJsonReader;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Simon Meoni
 *         Created on 24/08/16.
 */
public class SyntaxParser {

    private StringBuffer xml;
    private StringBuffer txt;
    private TermsuiteJsonReader termsuiteJsonReader;

    private StringBuffer tokenizeBuffer;
    private int offset = 0;


    private Queue<Character> xmlCharacterQueue;
    public SyntaxParser(StringBuffer xml){
        this.xml = xml;
    }

    public SyntaxParser(StringBuffer txt, StringBuffer xml, TermsuiteJsonReader termsuiteJsonReader) {
        this.xml = xml;
        this.txt = txt;
        this.termsuiteJsonReader = termsuiteJsonReader;
        this.tokenizeBuffer = new StringBuffer();
    }

    public StringBuffer getXml() {
        return xml;
    }

    public StringBuffer getTokenizeBuffer() {
        return tokenizeBuffer;
    }

    public int getOffset() {
        return offset;
    }

    public void execute(){
        teiBodyspliter();
        teiWordTokenizer();
    }

    public void teiBodyspliter(){
        xml = new StringBuffer(xml.toString().split("(?=(<text>|<text\\s.*>))")[1]);

    }

    private void fillXmlCharacterQueue() {
        xmlCharacterQueue = new LinkedList<>();
        for (Character c : xml.toString().toCharArray()){
            this.xmlCharacterQueue.add(c);
        }
    }

    public void teiWordTokenizer() {
        int id = 1;
        termsuiteJsonReader.pollToken();
        Character ch;
        fillXmlCharacterQueue();
        while (!xmlCharacterQueue.isEmpty()) {
            ch = xmlCharacterQueue.poll();
            if (tokenizeBuffer.length() > offset
                    && xmlCharacterQueue.peek() == '!'
                    && ch == '<') {
                waitUntilCommentEnd(ch);
            }

            if (ch == '<') {
                id = waitUntilTagEnd(ch,offset,id);
            }

            else {
                checkTextAlignment(ch);
                id = tokenInjector(ch, offset, id);
                offset++;
            }
        }
    }

    private void waitUntilCommentEnd(Character ch) {
        String str = "";
        while (str.contains("-->")) {
            tokenizeBuffer.append(ch);
            str += ch;
            xmlCharacterQueue.poll();
        }
    }

    public int waitUntilTagEnd(Character ch, int offset,int id) {

        if (termsuiteJsonReader.isTokenQueueEmpty()){
            tokenInjector(ch,offset,id);
        }

        else if (termsuiteJsonReader.getCurrentTokenBegin() == -1 &&
                tokenizeBuffer.charAt(tokenizeBuffer.length() -1) != '>'){
            id = forceCloseInjection(ch, id,offset);
        }
        else {
            tokenizeBuffer.append(ch);
        }
        while ((ch = xmlCharacterQueue.poll()) != '>'){
            tokenizeBuffer.append(ch);
        }

        if (termsuiteJsonReader.getCurrentTokenBegin() == -1
                && !termsuiteJsonReader.isTokenQueueEmpty()
                && xmlCharacterQueue.peek() != '<')
            forceOpenInjection(ch,id);
        else
            tokenizeBuffer.append(ch);

        return id;
    }

    private int forceCloseInjection(Character ch, int id, int offset) {
        tokenizeBuffer.append("</w>").append(ch);
        if (termsuiteJsonReader.getCurrentTokenEnd() == offset)
            termsuiteJsonReader.pollToken();
        return id + 1;
    }

    private void forceOpenInjection(Character ch, int id) {
        tokenizeBuffer.append(ch).append("<w xml:id=\"" + "t").append(id).append("\">");
    }

    public void checkIfSymbol(Character ch) {
        if (ch == '&'){
            while ((ch = xmlCharacterQueue.poll()) != ';'){
                tokenizeBuffer.append(ch);
            }
            tokenizeBuffer.append(ch);
        }

    }

    private int tokenInjector(Character ch, int offset, int id) {
        if (offset == termsuiteJsonReader.getCurrentTokenBegin()){
            tokenizeBuffer.append("<w xml:id=\"" + "t").append(id).append("\">").append(ch);
            termsuiteJsonReader.setCurrentTokenBegin(-1);
            checkIfSymbol(ch);
        }
        else if (offset == termsuiteJsonReader.getCurrentTokenEnd()){
            tokenizeBuffer.append("</w>").append(ch);
            checkIfSymbol(ch);
            termsuiteJsonReader.pollToken();
            return id + 1;
        } else {
            tokenizeBuffer.append(ch);
            checkIfSymbol(ch);
        }
        return id;

    }

    public void checkTextAlignment(Character ch){
        if (offset < txt.length() - 1 && txt.charAt(offset) == '\n') {
            while (ch != txt.charAt(offset))
                offset++;
            }
    }
}
