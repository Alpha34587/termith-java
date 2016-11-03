package org.atilf.module.termsuite.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import static eu.project.ttc.readers.JsonCasConstants.*;

/**
 * @author Simon Meoni
 *         Created on 25/08/16.
 */
public class TermsuiteJsonReader {

    private Queue<Token> _tokenQueue = new LinkedList<>();
    private File _file;
    private Token _pollToken;
    private final JsonFactory _factory = new JsonFactory();
    private final static Logger LOGGER = LoggerFactory.getLogger(TermsuiteJsonReader.class);


    public TermsuiteJsonReader(){}

    public TermsuiteJsonReader(File file) {this(new LinkedList<>(), file);}

    private TermsuiteJsonReader(Queue<Token> tokenQueue, File file) {
        _tokenQueue = tokenQueue;
        _file = file;
    }

    public boolean isTokenQueueEmpty(){
        return _pollToken == null;
    }

    public Queue<Token> getTokenQueue() {
        return _tokenQueue;
    }

    void setTokenQueue(Queue<Token> _tokenQueue) {
        this._tokenQueue = _tokenQueue;
    }

    public void parsing() {
        try {
            browseJson();
        } catch (IOException e) {
            LOGGER.error("An error occurred during TermSuite Json Cas parsing - Tokenization Ended", e);
        }
    }

    private void browseJson() throws IOException {
        LOGGER.debug("Json browsing started");
        JsonParser parser = _factory.createParser(_file);

        JsonToken jsonToken;
        Token token = new Token();
        boolean inWa = false;

        while ((jsonToken = parser.nextToken()) != null) {

            if (inWa){
                if (jsonToken == JsonToken.END_ARRAY)
                    break;
                else if (jsonToken == JsonToken.END_OBJECT) {
                    _tokenQueue.add(token);
                    token = new Token();
                }
                fillTokenStack(parser, jsonToken, token);
            }

            else if ("word_annotations".equals(parser.getParsingContext().getCurrentName())) {
                inWa = true;
            }
        }
        LOGGER.debug("Json browsing ended");

    }

    public void pollToken(){
        _pollToken =  _tokenQueue.poll();
    }

    public int getCurrentTokenEnd(){
        if (_pollToken != null)
            return _pollToken.getEnd();
        else
            return -1;
    }

    public void setCurrentTokenBegin(int i){
        if (_pollToken != null)
            _pollToken.setBegin(i);
    }

    public String getCurrentLemma(){
        if (_pollToken != null)
            return _pollToken.getLemma();
        else
            return "";
    }

    public String getCurrentPos(){
        if (_pollToken != null)
            return _pollToken.getPos();
        else
            return "";
    }

    public void setCurrentTokenEnd(int i){
        if (_pollToken != null)
            _pollToken.setBegin(i);
    }

    public int getCurrentTokenBegin(){
        if (_pollToken != null)
            return _pollToken.getBegin();
        else
            return -1;
    }

    private void fillTokenStack(JsonParser parser, JsonToken jsonToken, Token token) throws IOException {
        if (jsonToken.equals(JsonToken.FIELD_NAME)){
            switch (parser.getCurrentName()){
                case F_LEMMA :
                    token.setLemma(parser.nextTextValue());
                    break;
                case F_TAG :
                    token.setPos(parser.nextTextValue());
                    break;
                case F_BEGIN :
                    token.setBegin(parser.nextIntValue(0));
                    break;
                case F_END :
                    token.setEnd(parser.nextIntValue(0));
                    break;
                default:
                    break;
            }
        }
    }

    public void createToken(String pos, String lemma, int begin, int end){
        _tokenQueue.add(new Token(pos, lemma, begin, end));
    }

    public class Token {

        public String pos;
        private String lemma;
        private int begin;
        private int end;

        Token(String pos, String lemma, int begin, int end) {
            this.pos = pos;
            this.lemma = lemma;
            this.begin = begin;
            this.end = end;
        }

        Token(){}

        public void setPos(String pos) {
            this.pos = pos;
        }

        public void setLemma(String lemma) {
            this.lemma = lemma;
        }

        public void setBegin(int begin) {
            this.begin = begin;
        }

        public void setEnd(int end) {
            this.end = end;
        }

        public String getPos() {
            return pos;
        }

        public String getLemma() {
            return lemma;
        }

        public int getBegin() {
            return begin;
        }

        public int getEnd() {
            return end;
        }
    }
}
