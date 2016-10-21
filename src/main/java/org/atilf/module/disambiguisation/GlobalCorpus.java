package org.atilf.module.disambiguisation;

import com.google.common.collect.Multiset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;

import java.io.IOException;

import static org.atilf.models.SubLexicResource.NAMESPACE_CONTEXT;

/**
 * @author Simon Meoni
 *         Created on 20/10/16.
 */
public class GlobalCorpus {
    private final String p;
    private final Multiset disambGlobalCorpus;
    private DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    private DocumentBuilder dBuilder;
    private Document doc;
    private XPath xpath;
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalCorpus.class.getName());
    private NodeList spanNode;

    public GlobalCorpus(String p, Multiset disambGlobalCorpus) {
        this.p = p;
        this.disambGlobalCorpus = disambGlobalCorpus;
        xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(NAMESPACE_CONTEXT);
        try {
            dbFactory.setNamespaceAware(true);
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            LOGGER.error("error during the creation of documentBuilder object : ", e);
        }
        try {
            doc = dBuilder.parse(p);
        } catch (SAXException | IOException e) {
            LOGGER.error("error during the parsing of document", e);
        }
    }

    public void execute() {
        extractWords();
        addToGlobalCorpus();
    }

    private void addToGlobalCorpus() {
        try {
            XPathExpression eLemma = xpath.compile(".//tei:f[@name = 'lemma']/tei:string/text()");
            XPathExpression ePos = xpath.compile(".//tei:f[@name = 'pos']/tei:symbol/@value");
            for (int i = 0; i < spanNode.getLength(); i++) {
            disambGlobalCorpus.add(eLemma.evaluate(spanNode.item(i), XPathConstants.STRING) + " "
                    + ePos.evaluate(spanNode.item(i), XPathConstants.STRING));
            }

        } catch (XPathExpressionException e) {
            LOGGER.error("error during the parsing of document", e);
        }
    }

    private void extractWords() {
        XPathExpression xPathExpression;
        try {
            xPathExpression = xpath.compile("//ns:standOff[@type = 'wordForms']/ns:listAnnotation/tei:span");
            spanNode = (NodeList) xPathExpression.evaluate(doc, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            LOGGER.error("error during the parsing of document",e);
        }
    }
}
