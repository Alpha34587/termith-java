package org.atilf.models.enrichment;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * normalize treetagger tags during json serialization in english or french
 * @author Simon Meoni
 *         Created on 08/09/16.
 */
public class TagNormalizer {

    private static Map<String,String> ttTag;
    private static final Logger _LOGGER = LoggerFactory.getLogger(TagNormalizer.class);


    public static void initTag(String lang){
        switch (lang) {
            case "en" :
                ttTag = parseResource("/models/enrichment/tagNormalizer/treeTaggerMultexTagEn.json");
                break;
            case "fr" :
                ttTag = parseResource("/models/enrichment/tagNormalizer/treeTaggerMultexTagFr.json");
                break;
            default:
                throw new IllegalArgumentException("this language is not support : " + lang);
        }
    }

    private static Map<String,String> parseResource(String file) {
        Map<String,String> result = new HashMap<>();
        boolean inTag = false;
        try {
            JsonFactory jFactory = new JsonFactory();
            JsonParser jParser = jFactory.createParser(TagNormalizer.class.getResourceAsStream(file));

            while (jParser.nextToken() != null) {
                if (jParser.getCurrentName() == "tag") {
                    inTag = true;
                }
                else if (inTag && jParser.getCurrentToken() == JsonToken.END_OBJECT) {
                    break;
                }
                else if (inTag){
                    result.put(jParser.getCurrentName(),jParser.getValueAsString());
                }
            }

            jParser.close();

        }
        catch (IOException e) {
            _LOGGER.error("cannot parse json file");
        }
        return result;
    }

    public static String normalize(String token) {
        return ttTag.get(token);
    }
}
