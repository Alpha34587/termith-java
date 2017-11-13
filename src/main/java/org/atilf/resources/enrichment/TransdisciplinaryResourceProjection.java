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
        boolean inWords = false;
        int memForm = 0;
        String memWord = "";
        String memLibelle = "";
        try {

            JsonFactory jFactory = new JsonFactory();
            JsonParser jParser = jFactory.createParser(getClass().getClassLoader().getResourceAsStream(resourcePath));

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
