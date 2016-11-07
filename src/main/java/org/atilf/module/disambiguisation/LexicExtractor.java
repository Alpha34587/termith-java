package org.atilf.module.disambiguisation;

import org.atilf.models.disambiguisation.GlobalLexic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;

import java.io.IOException;

import static org.atilf.models.disambiguisation.ContextResources.NAMESPACE_CONTEXT;

/**
 * @author Simon Meoni
 *         Created on 20/10/16.
 */
public class LexicExtractor implements Runnable{
    private GlobalLexic _disambGlobalCorpus;
    private DocumentBuilderFactory _dbFactory = DocumentBuilderFactory.newInstance();
    private DocumentBuilder _dBuilder;
    private Document _doc;
    private XPath _xpath = XPathFactory.newInstance().newXPath();
    private NodeList _spanNode;
    private static final Logger LOGGER = LoggerFactory.getLogger(LexicExtractor.class.getName());
    private XPathExpression _eLemma;
    private XPathExpression _ePos;
    private XPathExpression _eSpanWordForms;
    private String _p;

    public LexicExtractor(String p, GlobalLexic disambGlobalCorpus) {
        _disambGlobalCorpus = disambGlobalCorpus;
        _xpath.setNamespaceContext(NAMESPACE_CONTEXT);
        _dbFactory.setNamespaceAware(true);
        _p = p;
        try {
            _dBuilder = _dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            LOGGER.error("error during the creation of documentBuilder object : ", e);
        }
        try {
            _doc = _dBuilder.parse(p);
        } catch (SAXException | IOException e) {
            LOGGER.error("error during the parsing of document", e);
        }
        try {
            _eLemma = _xpath.compile(".//tei:f[@name = 'lemma']/tei:string/text()");
            _ePos = _xpath.compile(".//tei:f[@name = 'pos']/tei:symbol/@value");
            _eSpanWordForms = _xpath.compile("//ns:standOff[@type = 'wordForms']/ns:listAnnotation/tei:span");
        } catch (XPathExpressionException e) {
            LOGGER.error("cannot compile xpath :",e);
        }
    }

    public void execute() {
        extractWords();
        addToGlobalCorpus();
    }

    private void addToGlobalCorpus() {
        try {
            for (int i = 0; i < _spanNode.getLength(); i++) {
            _disambGlobalCorpus.addEntry(_eLemma.evaluate(_spanNode.item(i), XPathConstants.STRING) + " "
                    + _ePos.evaluate(_spanNode.item(i), XPathConstants.STRING));
            }
        } catch (XPathExpressionException e) {
            LOGGER.error("error during the parsing of document", e);
        }
    }

    private void extractWords() {
        try {
            _spanNode = (NodeList) _eSpanWordForms.evaluate(_doc, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            LOGGER.error("error during the parsing of document",e);
        }
    }

    @Override
    public void run() {
        LOGGER.info("extract occurence from " + _p + " to global corpus lexic");
        LOGGER.info(_p + " added to global corpus");
        this.execute();
    }
}
