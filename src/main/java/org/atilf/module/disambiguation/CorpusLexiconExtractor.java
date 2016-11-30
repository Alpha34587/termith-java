package org.atilf.module.disambiguation;

import org.atilf.models.disambiguation.CorpusLexicon;
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

import static org.atilf.models.disambiguation.ContextResources.NAMESPACE_CONTEXT;
import static org.atilf.models.disambiguation.ContextResources.TAG_GETTER;

/**
 * extract all lemma/POS pairs of a file and add it to the CorpusLexicon of a termithIndex
 * @author Simon Meoni
 *         Created on 20/10/16.
 */
public class CorpusLexiconExtractor implements Runnable{
    private CorpusLexicon _CorpusLexicon;
    private DocumentBuilder _dBuilder;
    private Document _doc;
    private NodeList _wElements;
    private static final Logger LOGGER = LoggerFactory.getLogger(CorpusLexiconExtractor.class.getName());
    private XPathExpression _eTagsGetter;
    private String _p;

    /**
     * constructor of contextExtractor
     * @param p the path of the xml file
     * @param corpusLexicon the corpusLexicon of the termithIndex of the process (all the context is retained
     *                       in this variable)
     * @see CorpusLexicon
     */
    public CorpusLexiconExtractor(String p, CorpusLexicon corpusLexicon) {

        /*
        initialize the path and all the necessary fields needed to parse xml file
         */

        /*
        initialize _p and _corpusLexicon fields
         */
        _CorpusLexicon = corpusLexicon;
        _p = p;
        XPath xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(NAMESPACE_CONTEXT);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        dbFactory.setNamespaceAware(true);
        try {
            _dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            LOGGER.error("error during the creation of documentBuilder object : ", e);
        }
        try {
            /*
            execute document
             */
            _doc = _dBuilder.parse(p);
        } catch (SAXException | IOException e) {
            LOGGER.error("error during the execute of document", e);
        }
        try {
            /*
            extract all w elements from a node
             */
            _eTagsGetter = xpath.compile(TAG_GETTER);
        } catch (XPathExpressionException e) {
            LOGGER.error("cannot compile xpath :",e);
        }
    }

    /**
     * call extractWords to extract all w elements on the xml file and add to to the corpusLexicon with
     * addToCorpus method
     */
    public void execute() {
        extractWords();
        addToCorpus();
    }

    /**
     * add each pair of lemma/POS to the _corpusLexicon field
     * @see CorpusLexicon
     */
    private void addToCorpus() {
        LOGGER.debug("add all w elements to corpus : ", _p);
        for (int i = 0; i < _wElements.getLength(); i++) {
            _CorpusLexicon.addOccurrence(_wElements.item(i).getTextContent());
        }
    }

    /**
     * extract all the w elements of the file
     */
    private void extractWords() {
        try {
            _wElements = (NodeList) _eTagsGetter.evaluate(_doc, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            LOGGER.error("error during the execute of document",e);
        }
    }

    /**
     * run override method call execute method
     */
    @Override
    public void run() {
        LOGGER.info("extract occurrence from " + _p + " to global corpus lexicon");
        execute();
        LOGGER.info(_p + " added to global corpus");
    }
}
