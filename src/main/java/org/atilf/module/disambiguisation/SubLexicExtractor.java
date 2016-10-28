package org.atilf.module.disambiguisation;

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

import static org.atilf.models.disambiguisation.SubLexicResource.*;

/**
 * @author Simon Meoni
 *         Created on 14/10/16.
 */
public class SubLexicExtractor {
    Deque<String> _target = new ArrayDeque<>();
    Deque<String> _corresp = new ArrayDeque<>();
    Deque<String> _lexAna = new ArrayDeque<>();
    Document _doc;
    XPath _xpath;
    private XPathExpression _eTargetTermGetter;
    private XPathExpression _eSpan;
    private XPathExpression _eTarget;
    private XPathExpression _eCorresp;
    private XPathExpression _eAna;
    private Map<String, LexicalProfile> _subLexics;
    private DocumentBuilderFactory _dbFactory = DocumentBuilderFactory.newInstance();
    private DocumentBuilder _dBuilder;
    private static final Logger LOGGER = LoggerFactory.getLogger(SubLexicExtractor.class.getName());

    public SubLexicExtractor(String p, Map<String, LexicalProfile> subLexics){
        _subLexics = subLexics;
        _xpath = XPathFactory.newInstance().newXPath();
        _xpath.setNamespaceContext(NAMESPACE_CONTEXT);
        try {
            _dbFactory.setNamespaceAware(true);
            _dBuilder = _dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            LOGGER.error("error during the creation of documentBuilder object : ", e);
        }
        try {
            _doc = _dBuilder.parse(p);
        } catch (SAXException | IOException e) {
            LOGGER.error("error during the parsing of document",e);
        }
        try {
            _eSpan = _xpath.compile(SPAN);
            _eTarget = _xpath.compile(TARGET);
            _eCorresp = _xpath.compile(CORRESP);
            _eAna = _xpath.compile(ANA);
        } catch (XPathExpressionException e) {
            LOGGER.error("cannot compile xpath expression",e);
        }
    }

    public Deque<String> get_target() {
        return _target;
    }

    public Deque<String> get_corresp() {
        return _corresp;
    }

    public Deque<String> get_lexAna() {
        return _lexAna;
    }

    public void execute() {
        extractTerms();
        extractSubCorpus();
    }

    public void extractSubCorpus() {
        while (!_target.isEmpty()){
            String t = _target.poll();
            String c = _corresp.poll();
            String l = _lexAna.poll();
            NodeList nodes;
            try {
                List<String> tags = Arrays.asList(t.replace("#","").split(" "));
                if (tags.size() > 1) {
                    multiWordsExtractor(c, l, tags);
                }
                else
                    singleWordExtractor(c,l,tags.get(0));
            } catch (XPathExpressionException e) {
                LOGGER.error("error during the parsing of document",e);
            }
        }
    }

    private void singleWordExtractor(String corresp, String lex, String tag) throws XPathExpressionException {
        XPathExpression xPathExpression;
        NodeList nodes;
        xPathExpression = _xpath.compile(
                ("//tei:text//tei:w" +
                        "[./preceding-sibling::tei:w[@xml:id = '@id'] or" +
                        " ./following-sibling::tei:w[@xml:id = '@id']]")
                        .replace("@id", tag)
        );
        nodes = (NodeList) xPathExpression.evaluate(_doc, XPathConstants.NODESET);
        extractWordForms(corresp, lex, nodes);
    }

    private void multiWordsExtractor(String corresp, String lex, List<String> tags) throws XPathExpressionException {
        NodeList nodes;
        XPathExpression eTargetsTermGetter = _xpath.compile(
                ("//tei:text//tei:w[./preceding::tei:w[@xml:id = '@id_first'] and " +
                        "./following::tei:w[@xml:id = '@id_last']]")
                        .replace("@id_first", tags.get(0))
                        .replace("@id_last", tags.get(tags.size() - 1))
        );
        nodes = (NodeList) eTargetsTermGetter.evaluate(_doc, XPathConstants.NODESET);
        if (equalsToCorrespTarget(tags,nodes)){
            eTargetsTermGetter = _xpath.compile(
                    ("//tei:text//tei:w" +
                            "[./preceding-sibling::tei:w[@xml:id = '@id_last'] or" +
                            " ./following-sibling::tei:w[@xml:id = '@id_first']]")
                            .replace("@id_first", tags.get(0)).replace("@id_last",tags.get(tags.size()-1))
            );
            nodes = (NodeList) eTargetsTermGetter.evaluate(_doc, XPathConstants.NODESET);
            extractWordForms(corresp, lex, nodes);
        }
    }

    private boolean equalsToCorrespTarget(List<String> tags, NodeList nodes) {
        List<String> nodeId = new ArrayList<>();
        for(int i = 0; i < nodes.getLength(); i ++) {
            nodeId.add(nodes.item(i).getAttributes().getNamedItem("xml:id").getNodeValue());
        }
        return tags.subList(1,tags.size()-1).equals(nodeId);
    }

    private void extractWordForms(String c, String l, NodeList nodes) throws XPathExpressionException {
        for (int i = 0; i < nodes.getLength(); i++) {
            XPathExpression sXpath = _xpath.compile("//ns:standOff[@type = 'wordForms']" +
                    "/ns:listAnnotation/tei:span[@target = '@id']"
                            .replace(
                                    "@id",
                                    "#" + nodes.item(i).getAttributes().getNamedItem("xml:id")
                                            .getNodeValue()
                            )
            );
            mapToMultiset((Node) sXpath.evaluate(_doc, XPathConstants.NODE), c, l);
        }
    }

    public void extractTerms() {
        try {
            NodeList nodes = (NodeList) _eSpan.evaluate(_doc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++) {
                String ana = _eAna.evaluate(nodes.item(i));
                if (!"#noDM".equals(ana) && !ana.isEmpty()){
                    _target.add(_eTarget.evaluate(nodes.item(i)));
                    _corresp.add(_eCorresp.evaluate(nodes.item(i)));
                    _lexAna.add(_eAna.evaluate(nodes.item(i)));
                }
            }
        } catch (XPathExpressionException e) {
            LOGGER.error("error during the parsing of document",e);
        }
    }

    protected void mapToMultiset(Node span, String c, String l){
        try {
            XPathExpression eLemma = _xpath.compile(".//tei:f[@name = 'lemma']/tei:string/text()");
            XPathExpression ePos = _xpath.compile(".//tei:f[@name = 'pos']/tei:symbol/@value");

            Node lemmaNode = (Node) eLemma.evaluate(span, XPathConstants.NODE);
            Node posNode = (Node) ePos.evaluate(span, XPathConstants.NODE);
            String lemmaValue = lemmaNode.getNodeValue().trim();
            String posValue = posNode.getNodeValue().trim();
            String key = normalizeKey(c,l);
            if (!_subLexics.containsKey(key)){
                _subLexics.put(key,new LexicalProfile());
            }
            _subLexics.get(key).addOccurrence(lemmaValue + " " + posValue);
        } catch (XPathExpressionException e) {
            LOGGER.error("error during the parsing of document :",e);
        }
    }

    protected String normalizeKey(String c, String l) {
        if ("#DM0".equals(l)) {
            return (c + "_lexOff").replace("#", "");
        } else {
            return (c + "_lexOn").replace("#", "");
        }
    }
}
