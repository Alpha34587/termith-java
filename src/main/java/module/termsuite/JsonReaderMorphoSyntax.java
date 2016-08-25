package module.termsuite;

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
class JsonReaderMorphoSyntax {

    boolean inWa = false;
    private Queue<Token> tokenQueue;
    private Token token;

    private final static Logger LOGGER = LoggerFactory.getLogger(JsonReaderMorphoSyntax.class);
    JsonToken jsonToken = null;
    static JsonFactory factory = new JsonFactory();


    public JsonReaderMorphoSyntax() {
        this.tokenQueue = new LinkedList<>();
    }

    public JsonReaderMorphoSyntax(Queue<Token> tokenQueue) {
        this.tokenQueue = tokenQueue;
    }

    Queue<Token> getTokenQueue() {
        return tokenQueue;
    }

    public void setTokenQueue(Queue<Token> tokenQueue) {
        this.tokenQueue = tokenQueue;
    }

    public void parsing(File file) {
        try {
            token = new Token();
            JsonParser parser = factory.createParser(file);
            browseJson(parser);

        } catch (IOException e) {
            LOGGER.error("An error occurred during TermSuite Json Cas parsing", e);
        }
    }

    private void browseJson(JsonParser parser) throws IOException {
        while ((jsonToken = parser.nextToken()) != null) {

            if (inWa){
                if (jsonToken == JsonToken.END_ARRAY)
                    break;
                else if (jsonToken == JsonToken.END_OBJECT) {
                    tokenQueue.add(token);
                    token = new Token();
                }
                fillTokenStack(parser, jsonToken);
            }

            else if ("word_annotations".equals(parser.getParsingContext().getCurrentName())) {
                inWa = true;
            }
        }
    }

    private void fillTokenStack(JsonParser parser, JsonToken jsonToken) throws IOException {
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

    public void clean(){
//        for (JsonReaderMorphoSyntax.Token tag : tokenQueue) {
//
//            int index = tokenQueue.indexOf(tag);
//            if (index > 0) {
//                if (tag.begin == tags.get(index - 1).end) {
//                    tags.get(index - 1).end--;
//                }
//                if (tag.begin < tags.get(index - 1).end) {
//                    tag.begin = tags.get(index - 1).end + 1;
//                }
//            }
//        }

    }

    static class Token {
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
