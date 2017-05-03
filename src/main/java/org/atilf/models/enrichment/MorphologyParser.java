package org.atilf.models.enrichment;

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
 * parse morphology json file
 * @author Simon Meoni
 *         Created on 25/08/16.
 */
public class MorphologyParser {

    private Queue<Token> _tokenQueue = new LinkedList<>();
    private File _file;
    private Token _pollToken;
    private final JsonFactory _factory = new JsonFactory();
    private final static Logger LOGGER = LoggerFactory.getLogger(MorphologyParser.class);


    /**
     * constructor for MorphologyParser
     * @param file the file to parse
     */
    public MorphologyParser(File file) {
        _file = file;
    }

    /**
     * getter for _tokenQueue field
     * @return return Queue<Token>
     */
    public Queue<Token> getTokenQueue() {
        return _tokenQueue;
    }

    /**
     * parse json file with streaming library and get in word_annotations the words annotations like : _lemma, _pos,
     * the ending offset and the beginning offset
     */
    public void execute() {
        try {
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
                    setToken(parser, jsonToken, token);
                }

                else if ("word_annotations".equals(parser.getParsingContext().getCurrentName())) {
                    inWa = true;
                }
            }
            LOGGER.debug("Json browsing ended");
        } catch (IOException e) {
            LOGGER.error("An error occurred during TermSuite Json Cas execute - Tokenization Ended", e);
        }
    }

    /**
     * get next token from _tokenQueue field
     */
    public void pollToken(){
        _pollToken =  _tokenQueue.poll();
    }

    /**
     * get _end of the current token
     * @return the ending offset
     */
    public int getCurrentTokenEnd(){
        if (_pollToken != null)
            return _pollToken.getEnd();
        else
            return -1;
    }

    /**
     * set the current _begin offset
     * @param i the value to set
     */
    public void setCurrentTokenBegin(int i){
        if (_pollToken != null)
            _pollToken.setBegin(i);
    }

    /**
     * get the _lemma of the current token
     * @return return the _lemma
     */
    public String getCurrentLemma(){
        if (_pollToken != null)
            return _pollToken.getLemma();
        else
            return "";
    }

    /**
     * get the _pos of the current token
     * @return return the _pos
     */
    public String getCurrentPos(){
        if (_pollToken != null)
            return _pollToken.getPos();
        else
            return "";
    }


    /**
     * get _begin of the current token
     * @return the ending offset
     */
    public int getCurrentTokenBegin(){
        if (_pollToken != null)
            return _pollToken.getBegin();
        else
            return -1;
    }

    /**
     * set a token object with an element in json
     * @param parser the current parser
     * @param jsonToken the current jsonToken
     * @param token the token to set
     * @throws IOException Thrown exeception if token meet problems during parsing
     */
    private void setToken(JsonParser parser, JsonToken jsonToken, Token token) throws IOException {
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

    /**
     * create new token on _tokenQueue
     * @param pos _pos value
     * @param lemma _lemma value
     * @param begin beginning offset
     * @param end ending offset
     */
    public void createToken(String pos, String lemma, int begin, int end){
        _tokenQueue.add(new Token(pos, lemma, begin, end));
    }

    /**
     * inner Token class used by _TokenQueue field
     */
    public class Token {

        private String _pos;
        private String _lemma;
        private int _begin;
        private int _end;

        /**
         * constructor for Token class
         * @param pos _pos value
         * @param lemma _lemma value
         * @param begin beginning offset
         * @param end ending offset
         */
        Token(String pos, String lemma, int begin, int end) {
            _pos = pos;
            _lemma = lemma;
            _begin = begin;
            _end = end;
        }

        /**
         * empty constructor
         */
        Token(){}

        /**
         * setter for _pos field
         * @param pos pos value
         */
        public void setPos(String pos) {
            _pos = pos;
        }
        
        /**
         * setter for _lemma field
         * @param lemma lemma value
         */
        public void setLemma(String lemma) {
            _lemma = lemma;
        }
        
        /**
         * setter for _begin field
         * @param begin begin value
         */
        public void setBegin(int begin) {
            _begin = begin;
        }
        
        /**
         * setter for _end field
         * @param end end value
         */
        public void setEnd(int end) {
            _end = end;
        }

        /**
         * getter for _pos field
         * @return return _pos
         */
        public String getPos() {
            return _pos;
        }

        /**
         * getter for _lemma field
         * @return return _lemma
         */
        public String getLemma() {
            return _lemma;
        }

        /**
         * getter for _begin field
         * @return return _begin
         */
        public int getBegin() {
            return _begin;
        }

        /**
         * getter for _end field
         * @return return _end
         */
        public int getEnd() {
            return _end;
        }
    }

}
