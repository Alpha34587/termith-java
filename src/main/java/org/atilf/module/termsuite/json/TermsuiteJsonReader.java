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

    private final static Logger LOGGER = LoggerFactory.getLogger(TermsuiteJsonReader.class);
    private final JsonFactory factory = new JsonFactory();
    private Queue<Token> tokenQueue;
    private File file;
    private Token pollToken;


    public TermsuiteJsonReader(){
        this.tokenQueue = new LinkedList<>();
    }

    public TermsuiteJsonReader(File file) {this(new LinkedList<>(), file);}

    private TermsuiteJsonReader(Queue<Token> tokenQueue, File file) {
        this.tokenQueue = tokenQueue;
        this.file = file;
    }

    public boolean isTokenQueueEmpty(){
        return pollToken == null;
    }

    public Queue<Token> getTokenQueue() {
        return tokenQueue;
    }

    void setTokenQueue(Queue<Token> tokenQueue) {
        this.tokenQueue = tokenQueue;
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
        JsonParser parser = factory.createParser(file);

        JsonToken jsonToken;
        Token token = new Token();
        boolean inWa = false;

        while ((jsonToken = parser.nextToken()) != null) {

            if (inWa){
                if (jsonToken == JsonToken.END_ARRAY)
                    break;
                else if (jsonToken == JsonToken.END_OBJECT) {
                    tokenQueue.add(token);
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
        this.pollToken =  tokenQueue.poll();
    }

    public int getCurrentTokenEnd(){
        if (pollToken != null)
            return pollToken.getEnd();
        else
            return -1;
    }

    public void setCurrentTokenBegin(int i){
        if (pollToken != null)
            pollToken.setBegin(i);
    }

    public String getCurrentLemma(){
        if (pollToken != null)
            return pollToken.getLemma();
        else
            return "";
    }

    public String getCurrentPos(){
        if (pollToken != null)
            return pollToken.getPos();
        else
            return "";
    }

    public void setCurrentTokenEnd(int i){
        if (pollToken != null)
            pollToken.setBegin(i);
    }

    public int getCurrentTokenBegin(){
        if (pollToken != null)
            return pollToken.getBegin();
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
        tokenQueue.add(new Token(pos, lemma, begin, end));
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
