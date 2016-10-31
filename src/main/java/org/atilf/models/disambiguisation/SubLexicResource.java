package org.atilf.models.disambiguisation;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Simon Meoni
 *         Created on 18/10/16.
 */
public class SubLexicResource {
    public static final NamespaceContext NAMESPACE_CONTEXT = new NamespaceContext() {
        @Override
        public String getNamespaceURI(String s) {
            String uri;
            switch (s) {
                case "ns":
                case "ns2":
                    uri = "http://standoff.proposal";
                    break;
                case "xml":
                    uri = XMLConstants.XML_NS_URI;
                    break;
                default:
                    uri = "http://www.tei-c.org/ns/1.0";
                    break;
            }
            return uri;
        }

        @Override
        public String getPrefix(String s) {
            return null;
        }

        @Override
        public Iterator getPrefixes(String s) {
            return null;
        }
    };
    public static final String SPAN_T;
    public static final String TARGET_T;
    public static final String CORRESP_T;
    public static final String ANA_T;
    public static final String POS_W;
    public static final String SPAN_W;
    public static final String LEMMA_W;
    public static final String TARGET_W;
    public static final String FIRST_TEXT;
    public static final String NEXT_TEXT;
    public static final String MULTI_TEXT;
    public static final String SIMPLE_TEXT;
    private final static Logger LOGGER = LoggerFactory.getLogger(SubLexicResource.class);

    static {
        ObjectMapper mapper = new ObjectMapper();
        Map jsonTerm = null;
        Map jsonWordForm = null;
        Map jsonText = null;
        try {
            jsonTerm = mapper.readValue(
                    new File("src/main/resources/disambiguisation/json/xpath-candidats-termes.json"),
                    Map.class);
            jsonWordForm = mapper.readValue(
                    new File("src/main/resources/disambiguisation/json/xpath-wordforms.json"),
                    Map.class);
            jsonText = mapper.readValue(
                    new File("src/main/resources/disambiguisation/json/xpath-text.json"),
                    Map.class);
        } catch (IOException e) {
            LOGGER.error("could not initialize sublexic resource",e);
        }

        assert jsonTerm != null;
        SPAN_T = (String) jsonTerm.get("span");
        TARGET_T = (String) jsonTerm.get("target");
        CORRESP_T = (String) jsonTerm.get("corresp");
        ANA_T = (String) jsonTerm.get("ana");
        assert jsonWordForm != null;
        SPAN_W = (String) jsonWordForm.get("span");
        TARGET_W = (String) jsonWordForm.get("target");
        LEMMA_W = (String) jsonWordForm.get("lemma");
        POS_W = (String) jsonWordForm.get("pos");
        assert jsonText != null;
        FIRST_TEXT = (String) jsonText.get("first_id");
        NEXT_TEXT = (String) jsonText.get("next_id");
        SIMPLE_TEXT = (String) jsonText.get("simple_id");
        MULTI_TEXT = (String) jsonText.get("multi_id");
    }

}
