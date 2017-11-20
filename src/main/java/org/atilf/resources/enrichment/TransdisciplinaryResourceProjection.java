package org.atilf.resources.enrichment;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import java.io.IOException;
import java.util.Objects;

public class TransdisciplinaryResourceProjection extends ResourceProjection {
    public TransdisciplinaryResourceProjection(String resourcePath) {
        super(resourcePath);
    }

    @Override
    protected void parseResource(String resourcePath) {
        boolean inTtComponents = false;
        boolean inForm = false;
        int memId = 0;
        String memTt = "";
        String memLemma = "";
        try {

            JsonFactory jFactory = new JsonFactory();
            JsonParser jParser = jFactory.createParser(getClass().getClassLoader().getResourceAsStream(resourcePath));

            while (jParser.nextToken() != null) {

                if (Objects.equals(jParser.getCurrentName(), "form")) {
                    inForm = true;
                }

                if (inForm && Objects.equals(jParser.getCurrentName(), "id")) {
                    memId = jParser.nextIntValue(0);
                }

                else if (inForm && Objects.equals(jParser.getCurrentName(), "lemma")) {
                    memLemma = jParser.nextTextValue();
                }
                else if (Objects.equals(jParser.getCurrentName(), "tree_tagger_components")
                        && jParser.getCurrentToken() == JsonToken.START_ARRAY) {
                    inTtComponents = true;
                    inForm = false;
                    memTt = "";
                }
                else if (Objects.equals(jParser.getCurrentName(), "lemma") && inTtComponents) {
                    memTt += jParser.nextTextValue() + " ";
                }
                else if (inTtComponents && jParser.getCurrentToken() == JsonToken.END_ARRAY) {
                    inTtComponents = false;
                }
                else if (!inForm && !inTtComponents && jParser.getCurrentToken() == JsonToken.END_OBJECT){
                    inTtComponents = false;
                    addToPhraseologyMap(memId, memTt, memLemma);
                }
            }

            jParser.close();

        }
        catch (IOException e) {
            _logger.error("cannot parse json file",e);
        }
    }
}
