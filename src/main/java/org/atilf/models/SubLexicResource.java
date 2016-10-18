package org.atilf.models;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.atilf.module.termsuite.json.TermsuiteJsonReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.NamespaceContext;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;

/**
 * @author Simon Meoni
 *         Created on 18/10/16.
 */
public class SubLexicResource {
    private final static Logger LOGGER = LoggerFactory.getLogger(SubLexicResource.class);

    public static final NamespaceContext NAMESPACE_CONTEXT = new NamespaceContext() {
        @Override
        public String getNamespaceURI(String s) {
            String uri;
            if (s.equals("ns") || s.equals("ns2"))
                uri = "http://standoff.proposal";
            else
                uri = "http://www.tei-c.org/ns/1.0";
            return uri;        }

        @Override
        public String getPrefix(String s) {
            return null;
        }

        @Override
        public Iterator getPrefixes(String s) {
            return null;
        }
    };

    public static final String SPAN;
    public static final String TARGET;
    public static final String CORRESP;
    public static final String ANA;

    static {
        ObjectMapper mapper = new ObjectMapper();
        Map json = null;
        try {
            json = mapper.readValue(
                    new File("src/main/resources/disambiguisation/xpath-candidats-termes.json"),
                    Map.class);
        } catch (IOException e) {
            LOGGER.error("could not initialize sublexic resource",e);
        }

        SPAN = (String) json.get("span");
        TARGET = (String) json.get("target");
        CORRESP = (String) json.get("corresp");
        ANA = (String) json.get("ana");

    }

}
