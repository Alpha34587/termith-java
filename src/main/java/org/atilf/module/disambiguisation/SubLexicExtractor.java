package org.atilf.module.disambiguisation;

import com.google.common.collect.HashMultiset;
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
import java.util.*;

import static org.atilf.models.SubLexicResource.*;

/**
 * @author Simon Meoni
 *         Created on 14/10/16.
 */
public class SubLexicExtractor {
    private Map<String, LexicalProfile> subLexics;
    private Deque<String> target;
    private Deque<String> corresp;
    private Deque<String> lexAna;
    private DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    private DocumentBuilder dBuilder;
    private Document doc;
    private XPath xpath;
    private static final Logger LOGGER = LoggerFactory.getLogger(SubLexicExtractor.class.getName());

    public SubLexicExtractor(String p, Map<String, LexicalProfile> subLexics){
        this.subLexics = subLexics;
        xpath = XPathFactory.newInstance().newXPath();
        xpath.setNamespaceContext(NAMESPACE_CONTEXT);
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
            doc = dBuilder.parse(p);
        } catch (SAXException | IOException e) {
            LOGGER.error("error during the parsing of document",e);
        }
    }

    public Deque<String> getTarget() {
        return target;
    }

    public Deque<String> getCorresp() {
        return corresp;
    }

    public Deque<String> getLexAna() {
        return lexAna;
    }

    public void execute() {
        extractTerms();
        extractSubCorpus();
    }

    public void extractSubCorpus() {
        while (!target.isEmpty()){
            String t = target.poll();
            String c = corresp.poll();
            String l = lexAna.poll();
            XPathExpression xPathExpression;

            try {
                List<String> tags = Arrays.asList(t.split(" "));
                Set<Node> nodeSet = new HashSet<>();
                for (String tag : tags) {
                    tag = tag.substring(1);
                    xPathExpression = xpath.compile(
                            ("//tei:text//tei:w" +
                                    "[./following-sibling::tei:w[@xml:id = '@id'] or" +
                                    " ./preceding-sibling::tei:w[@xml:id = '@id']]")
                                    .replace("@id", tag)
                    );
                    NodeList nodes = (NodeList) xPathExpression.evaluate(doc, XPathConstants.NODESET);
                    addToNodeSet(nodeSet, nodes);
                }

                for (Node node : nodeSet) {
                    extractWordForms(c, l, tags, node);
                }


            } catch (XPathExpressionException e) {
                LOGGER.error("error during the parsing of document",e);
            }
        }
    }

    private void addToNodeSet(Set<Node> nodeSet, NodeList nodes) {
        for(int i = 0; i < nodes.getLength(); i ++) {
            nodeSet.add(nodes.item(i));
        }
    }

    private void extractWordForms(String c, String l, List<String> tags, Node node) throws XPathExpressionException {
        String id = node.getAttributes().getNamedItem("xml:id").getNodeValue();
        if (!tags.contains("#"+id)){
            XPathExpression sXpath  = xpath.compile("//ns:standOff[@type = 'wordForms']" +
                    "/ns:listAnnotation/tei:span[@target = '@id']"
                            .replace(
                                    "@id",
                                    "#"+ node.getAttributes().getNamedItem("xml:id")
                                            .getNodeValue()
                            )
            );
            mapToMultiset((Node) sXpath.evaluate(doc, XPathConstants.NODE),c,l);
        }
    }

    public void extractTerms() {
        XPathExpression eSpan;
        XPathExpression eTarget;
        XPathExpression eCorresp;
        XPathExpression eAna;

        try {
            eSpan = xpath.compile(SPAN);
            eTarget = xpath.compile(TARGET);
            eCorresp = xpath.compile(CORRESP);
            eAna = xpath.compile(ANA);

            NodeList nodes = (NodeList) eSpan.evaluate(doc, XPathConstants.NODESET);

            for (int i = 0; i < nodes.getLength(); i++) {
                String ana = eAna.evaluate(nodes.item(i));
                if (!"#noDM".equals(ana) && !ana.isEmpty()){
                    target.add(eTarget.evaluate(nodes.item(i)));
                    corresp.add(eCorresp.evaluate(nodes.item(i)));
                    lexAna.add(eAna.evaluate(nodes.item(i)));
                }
            }
        } catch (XPathExpressionException e) {
            LOGGER.error("error during the parsing of document",e);
        }
    }

    private void mapToMultiset(Node span, String c, String l){
        try {
            XPathExpression eLemma = xpath.compile(".//tei:f[@name = 'lemma']/tei:string/text()");
            XPathExpression ePos = xpath.compile(".//tei:f[@name = 'pos']/tei:symbol/@value");

            Node lemmaNode = (Node) eLemma.evaluate(span, XPathConstants.NODE);
            Node posNode = (Node) ePos.evaluate(span, XPathConstants.NODE);
            String lemmaValue = lemmaNode.getNodeValue().trim();
            String posValue = posNode.getNodeValue().trim();
            String key = normalizeKey(c,l);
            if (subLexics.containsKey(key)){
                subLexics.get(key).getLexicalTable().add(lemmaValue + " " + posValue);
            }
            else{
                Multiset<String> multiset = HashMultiset.create();
                multiset.add(lemmaValue + " " + posValue);
                subLexics.put(key,new LexicalProfile(multiset));

            }
        } catch (XPathExpressionException e) {
            LOGGER.error("error during the parsing of document",e);
        }
    }

    private String normalizeKey(String c, String l) {
        if ("#DM0".equals(l)) {
            return (c + "_lexOff").replace("#", "");
        } else {
            return (c + "_lexOn").replace("#", "");
        }
    }
}
