package org.atilf.module.disambiguisation;

import com.google.common.collect.Multiset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.*;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;

import static org.atilf.models.SubLexicResource.NAMESPACE_CONTEXT;

/**
 * @author Simon Meoni
 *         Created on 14/10/16.
 */
public class SubLexic {
    private Map<String, Multiset> subLexics;
    private Deque<String> target;
    private Deque<String> corresp;
    private Deque<String> lexAna;
    private DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    private DocumentBuilder dBuilder;
    private Document doc;
    private static final Logger LOGGER = LoggerFactory.getLogger(SubLexic.class.getName());

    public Deque<String> getTarget() {
        return target;
    }

    public Deque<String> getCorresp() {
        return corresp;
    }

    public Deque<String> getLexAna() {
        return lexAna;
    }

    public SubLexic(String p, Map<String, Multiset> subLexics){
        this.subLexics = subLexics;
        target = new ArrayDeque<>();
        corresp = new ArrayDeque<>();
        lexAna = new ArrayDeque<>();
        try {
            dbFactory.setNamespaceAware(true);
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            LOGGER.error("error during the creation of documentBuilder object : ", e);
        }
        try {
            doc = dBuilder.parse("file://"+p);
        } catch (SAXException | IOException e) {
            LOGGER.error("error during the parsing of document",e);
        }
    }

    public void execute() {
        extractTerms();
        extractSubCorpus();
    }

    public void extractSubCorpus() {


    }

    public void extractTerms() {

        XPath xpath = XPathFactory.newInstance().newXPath();
        XPathExpression eSpan;
        XPathExpression eTarget;
        XPathExpression eCorresp;
        XPathExpression eAna;
        xpath.setNamespaceContext(NAMESPACE_CONTEXT);

        try {
            eSpan = xpath.compile(
                    "//ns:standOff[@type = 'candidatsTermes']/ns:listAnnotation/tei:span"
            );
            eTarget = xpath.compile("@target");
            eCorresp = xpath.compile("@corresp");
            eAna = xpath.compile("@ana");

            NodeList nodes = (NodeList) eSpan.evaluate(doc, XPathConstants.NODESET);

            for (int i = 0; i < nodes.getLength(); i++) {
                target.add(eTarget.evaluate(nodes.item(i)));
                corresp.add(eCorresp.evaluate(nodes.item(i)));
                lexAna.add(eAna.evaluate(nodes.item(i)));
            }
        } catch (XPathExpressionException e) {
            LOGGER.error("error during the parsing of document",e);
        }
    }

    private void mapToMultiset(){

    }
}
