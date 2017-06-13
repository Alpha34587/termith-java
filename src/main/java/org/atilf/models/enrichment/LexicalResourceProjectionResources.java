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

    private static Map<String,List<Integer>> phraseologyMap = new HashMap<>();
    private final Logger _logger = LoggerFactory.getLogger(getClass().getName());
    public final static String PH_TYPE = "ph";
    public final static String LST_TYPE = "lst";

    public LexicalResourceProjectionResources(String lang, String type) {
        initResource(lang, type);
    }

    public Map<String, List<Integer>> getResourceMap() {
        return phraseologyMap;
    }

    public void initResource(String lang, String type){
        String langType = lang + " " + type;
        switch (langType) {
            case "fr " + PH_TYPE :
                parseResource(
                        "models/enrichment/lexicalResourceProjection/PhraseologyResource.json",type);
                break;
            case "fr " + LST_TYPE :
                parseResource(
                        "models/enrichment/lexicalResourceProjection/TransdisciplinaryResource.json",type);
                break;
            default:
                throw new IllegalArgumentException("this language is not support : " + lang);

        }
    }

    private void parseResource(String file, String type) {
        boolean inWords = false;
        int memForm = 0;
        String memWord = "";
        try {
            JsonFactory jFactory = new JsonFactory();
            JsonParser jParser = jFactory.createParser(getClass().getClassLoader().getResourceAsStream(file));

            while (jParser.nextToken() != null) {

                if (type.equals(LST_TYPE)) {
                    if (Objects.equals(jParser.getCurrentName(), "formeId")) {
                        memForm = jParser.nextIntValue(0);
                    }
                }
                else {
                    if (Objects.equals(jParser.getCurrentName(), "phraseoEntryId")) {
                        memForm = Integer.parseInt(jParser.nextTextValue().split("_")[1]);
                    }
                }
                if (Objects.equals(jParser.getCurrentName(), "words") && jParser.getCurrentToken() == JsonToken.START_ARRAY) {
                    inWords = true;
                    memWord = "";
                }
                if (Objects.equals(jParser.getCurrentName(), "formeTreeTagger") && inWords) {
                    inWords = true;
                    memWord += jParser.nextTextValue() + " ";
                }
                if(inWords && jParser.getCurrentToken() == JsonToken.END_ARRAY){
                    inWords = false;
                    if (!phraseologyMap.containsKey(memWord.trim())){
                        phraseologyMap.put(memWord.trim(),new ArrayList<>());

                    }
                    phraseologyMap.get(memWord.trim()).add(memForm);
                }
            }

            jParser.close();

        }
        catch (IOException e) {
            _logger.error("cannot parse json file",e);
        }
    }
}
