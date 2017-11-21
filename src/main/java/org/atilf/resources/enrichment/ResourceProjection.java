package org.atilf.resources.enrichment;

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
public class ResourceProjection {

    private Map<String,List<Integer>> entryIdMap = new HashMap<>();
    private Map<String,String> multiWordsMap = new HashMap<>();
    protected final Logger _logger = LoggerFactory.getLogger(getClass().getName());

    public ResourceProjection(String resourcePath) {
        parseResource(resourcePath);
    }

    public Map<String, List<Integer>> getResourceMap() {
        return entryIdMap;
    }

    public Map<String, String> getMultiWordsMap() {
        return multiWordsMap;
    }

    protected void parseResource(String resourcePath) {
        boolean inTtComponents = false;
        boolean inForm = false;
        int memId = 0;
        String memTtComponents = "";
        String memTextualTt = "";
        try {

            JsonFactory jFactory = new JsonFactory();
            JsonParser jParser = jFactory.createParser(getClass().getClassLoader().getResourceAsStream(resourcePath));

            while (jParser.nextToken() != null) {

                if (Objects.equals(jParser.getCurrentName(), "form")) {
                    inForm = true;
                }

                if (Objects.equals(jParser.getCurrentName(), "id") && inForm) {
                    memId = jParser.nextIntValue(0);
                }
                else if (Objects.equals(jParser.getCurrentName(), "textual_treetagger_form")) {
                    memTextualTt = jParser.nextTextValue();
                }
                else if (Objects.equals(jParser.getCurrentName(), "tree_tagger_components") && jParser.getCurrentToken() == JsonToken
                        .START_ARRAY) {
                    inTtComponents = true;
                    memTtComponents = "";
                }
                else if (Objects.equals(jParser.getCurrentName(), "lemma") && inTtComponents) {
                    memTtComponents += jParser.nextTextValue() + " ";
                }
                else if(inTtComponents && jParser.getCurrentToken() == JsonToken.END_ARRAY) {
                    inForm = false;
                    inTtComponents = false;
                    addToPhraseologyMap(memId, memTtComponents, memTextualTt);
                }
            }
            jParser.close();
        }
        catch (IOException e) {
            _logger.error("cannot parse json file",e);
        }
    }

    void addToPhraseologyMap(int memForm, String memWord, String memLibelle) {
        if (!entryIdMap.containsKey(memWord.trim())){
            entryIdMap.put(memWord.trim(),new ArrayList<>());
            multiWordsMap.put(memWord.trim(),memLibelle);
        }
        if (!entryIdMap.get(memWord.trim()).contains(memForm)){
            entryIdMap.get(memWord.trim()).add(memForm);
        }
    }
}
