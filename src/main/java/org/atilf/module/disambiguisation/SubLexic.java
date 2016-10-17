package org.atilf.module.disambiguisation;

import com.google.common.collect.Multiset;
import org.atilf.worker.SubLexicWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.Map;

/**
 * @author Simon Meoni
 *         Created on 14/10/16.
 */
public class SubLexic {
    private String lexOn;
    private Map<String, Multiset> subLexics;
    private ArrayDeque<String> target;
    private ArrayDeque<String> termsId;
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder dBuilder;
    Document doc;
    private static final Logger LOGGER = LoggerFactory.getLogger(SubLexic.class.getName());

    public SubLexic(String p, Map<String, Multiset> subLexics){
        this.subLexics = subLexics;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            LOGGER.error("error during the creation of documentBuilder object : ", e);
        }
        try {
            doc = dBuilder.parse(new File(p));
        } catch (SAXException | IOException e) {
            LOGGER.error("error during the parsing of document");
        }
    }

    public void execute() {
        extractTerms();
        extractSubCorpus();
    }

    private void extractSubCorpus() {

    }

    private void extractTerms() {

    }

    private void mapToMultiset(){

    }
}
