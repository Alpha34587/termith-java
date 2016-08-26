package module.tei.morphology;

import module.termsuite.TermsuiteJsonReader;

import java.util.LinkedList;
import java.util.Objects;
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
        int offset = 0;
        int xmlIndex = 0;
        termsuiteJsonReader.pollToken();
        Character ch;
        fillXmlCharacterQueue();
        while (!xmlCharacterQueue.isEmpty()) {
            ch = xmlCharacterQueue.poll();
            if (tokenizeBuffer.length() != 0
                    && tokenizeBuffer.charAt(tokenizeBuffer.length() - 1) == '<'
                    && ch == '!')
                waitUntilCommentEnd(xmlIndex);

            else if (ch == '<'){
                id = waitUntilTagEnd(ch,offset,id);
            }

            else {
                id = tokenInjector(ch, offset, id);
                offset++;
            }
        }
    }

    private void waitUntilCommentEnd(int xmlIndex) {

        while (xmlCharacterQueue.poll() != '>' &&
                Objects.equals(xml.substring(xmlIndex, xmlIndex + 3), "-->")){

        }
    }

    public int waitUntilTagEnd(Character ch, int offset,int id) {
        if (termsuiteJsonReader.isTokenQueueEmpty()){
            tokenInjector(ch,offset,id);
        }

        else if (termsuiteJsonReader.getCurrentTokenBegin() == -1){
            id = forceCloseInjection(ch, id,offset);
        }
        else {
            tokenizeBuffer.append(ch);
        }
        while ((ch = xmlCharacterQueue.poll()) != '>'){
            tokenizeBuffer.append(ch);
        }

        if (termsuiteJsonReader.getCurrentTokenBegin() == -1
                && !termsuiteJsonReader.isTokenQueueEmpty())
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

    public void  waitUntilSymbolEnd(){

    }

    private int tokenInjector(Character ch, int offset, int id) {
        if (offset == termsuiteJsonReader.getCurrentTokenBegin()){
            tokenizeBuffer.append("<w xml:id=\"" + "t").append(id).append("\">").append(ch);
            termsuiteJsonReader.setCurrentTokenBegin(-1);
        }
        else if (offset == termsuiteJsonReader.getCurrentTokenEnd()){
            tokenizeBuffer.append("</w>").append(ch);
            termsuiteJsonReader.pollToken();
            return id + 1;
        }
        else
            tokenizeBuffer.append(ch);
        return id;

    }

    public void checkTextAlignment(){

    }
}
