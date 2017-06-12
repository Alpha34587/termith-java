package org.atilf.models.enrichment;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Simon Meoni on 12/06/17.
 */
public class PhraseologyResources {

    private static Map<String,List<Integer>> phraseologyMap = new HashMap<>();
    private final Logger _logger = LoggerFactory.getLogger(getClass().getName());

    public PhraseologyResources(String lang) {
        initResource(lang);
    }

    public Map<String, List<Integer>> getPhraseologyMap() {
        return phraseologyMap;
    }

    public void initResource(String lang){
        switch (lang) {
            case "fr" :
                parseResource(
                        "module/enrichment/lexicalResourceProjection/phraseologyProjector/PhraseologyResource.json"
                );
                break;
            default:
                throw new IllegalArgumentException("this language is not support : " + lang);
        }
    }

    private void parseResource(String file) {
        boolean inWords = false;
        int memForm = 0;
        String memWord = "";
        try {
            JsonFactory jFactory = new JsonFactory();
            JsonParser jParser = jFactory.createParser(getClass().getClassLoader().getResourceAsStream(file));

            while (jParser.nextToken() != null) {
                if (jParser.getCurrentName() == "formeId") {
                    memForm = jParser.nextIntValue(0);
                }
                if (jParser.getCurrentName() == "words" && jParser.getCurrentToken() == JsonToken.START_ARRAY) {
                    inWords = true;
                    memWord = "";
                }
                if (jParser.getCurrentName() == "formeTreeTagger" && inWords) {
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
