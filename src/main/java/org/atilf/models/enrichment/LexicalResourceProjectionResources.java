package org.atilf.models.enrichment;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * Created by Simon Meoni on 12/06/17.
 */
public class LexicalResourceProjectionResources {

    public static final String PH_TYPE = "ph";
    public static final String LST_TYPE = "lst";
    private Map<String,List<Integer>> phraseologyIdMap = new HashMap<>();
    private Map<String,String> multiWordsMap = new HashMap<>();
    private final Logger _logger = LoggerFactory.getLogger(getClass().getName());

    public LexicalResourceProjectionResources(String lang, String type) {
        initResource(lang, type);
    }

    public Map<String, List<Integer>> getResourceMap() {
        return phraseologyIdMap;
    }

    public Map<String, String> getMultiWordsMap() {
        return multiWordsMap;
    }

    public void initResource(String type, String resourcePath){
                parseResource(resourcePath,type);
    }

    private void parseResource(String file, String type) {

        if (type == LST_TYPE){
            parseLst(file);
        }
        else {
            parsePh(file);
        }
    }

    private void parsePh(String file) {
        boolean inWords = false;
        int memForm = 0;
        String memWord = "";
        String memLibelle = "";
        try {

            JsonFactory jFactory = new JsonFactory();
            JsonParser jParser = jFactory.createParser(getClass().getClassLoader().getResourceAsStream(file));

            while (jParser.nextToken() != null) {

                if (Objects.equals(jParser.getCurrentName(), "phraseoEntryId")) {
                    memForm = Integer.parseInt(jParser.nextTextValue().split("_")[1]);
                }
                else if (Objects.equals(jParser.getCurrentName(), "libelle")) {
                    memLibelle = jParser.nextTextValue();
                }
                else if (Objects.equals(jParser.getCurrentName(), "words") && jParser.getCurrentToken() == JsonToken
                        .START_ARRAY) {
                    inWords = true;
                    memWord = "";
                }
                else if (Objects.equals(jParser.getCurrentName(), "formeTreeTagger") && inWords) {
                    memWord += jParser.nextTextValue() + " ";
                }
                else if(inWords && jParser.getCurrentToken() == JsonToken.END_ARRAY) {
                    inWords = false;
                    addToPhraseologyMap(memForm, memWord, memLibelle);
                }
            }

            jParser.close();

        }
        catch (IOException e) {
            _logger.error("cannot parse json file",e);
        }
    }

    private void addToPhraseologyMap(int memForm, String memWord, String memLibelle) {
        if (!phraseologyIdMap.containsKey(memWord.trim())){
            phraseologyIdMap.put(memWord.trim(),new ArrayList<>());
            multiWordsMap.put(memWord.trim(),memLibelle);
        }
        if (!phraseologyIdMap.get(memWord.trim()).contains(memForm)){
            phraseologyIdMap.get(memWord.trim()).add(memForm);
        }
    }

    private void parseLst(String file) {
        boolean inWords = false;
        int memForm = 0;
        String memWord = "";
        String memLibelle = "";
        try {

            JsonFactory jFactory = new JsonFactory();
            JsonParser jParser = jFactory.createParser(getClass().getClassLoader().getResourceAsStream(file));

            while (jParser.nextToken() != null) {

                if (Objects.equals(jParser.getCurrentName(), "formeId")) {
                    memForm = jParser.nextIntValue(0);
                }

                else if (Objects.equals(jParser.getCurrentName(), "libelle")) {
                    memLibelle = jParser.nextTextValue();
                }
                else if (Objects.equals(jParser.getCurrentName(), "words") && jParser.getCurrentToken() == JsonToken
                        .START_ARRAY) {
                    inWords = true;
                    memWord = "";
                }
                else if (Objects.equals(jParser.getCurrentName(), "formeTreeTagger") && inWords) {
                    inWords = true;
                    memWord += jParser.nextTextValue() + " ";
                }
                else if(inWords && jParser.getCurrentToken() == JsonToken.END_ARRAY) {
                    inWords = false;
                }
                else if(!inWords && jParser.getCurrentToken() == JsonToken.END_OBJECT){
                    inWords = false;
                    addToPhraseologyMap(memForm, memWord, memLibelle);
                }
            }

            jParser.close();

        }
        catch (IOException e) {
            _logger.error("cannot parse json file",e);
        }
    }
}
