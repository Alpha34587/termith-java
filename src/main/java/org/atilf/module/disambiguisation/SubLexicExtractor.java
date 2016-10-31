package org.atilf.module.disambiguisation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.namespace.QName;
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
    XPathExpression _eSpanWordForms;
    XPathExpression _eSpanTarget;
    XPathExpression _eSpanLemma;
    XPathExpression _eSpanPos;
    XPathExpression _eSpanTerms;
    XPathExpression _eTarget;
    XPathExpression _eCorresp;
    XPathExpression _eFirstId;
    XPathExpression _eNextId;
    XPathExpression _eAna;
    private Map<String, LexicalProfile> _subLexics;
    private DocumentBuilder _dBuilder;
    private XpathMapVariableResolver _xpathMapVariableResolver = new XpathMapVariableResolver();
    private Map<String, String> _xpathVariableMap = new HashMap<>();
    private Map<String, String> _targetSpanMap = new HashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(SubLexicExtractor.class.getName());

    public SubLexicExtractor(String p, Map<String, LexicalProfile> subLexics){
        _subLexics = subLexics;
        _xpath = XPathFactory.newInstance().newXPath();
        _xpath.setNamespaceContext(NAMESPACE_CONTEXT);
        _xpath.setXPathVariableResolver(_xpathMapVariableResolver);
        try {
            DocumentBuilderFactory _dbFactory = DocumentBuilderFactory.newInstance();
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
            _eSpanTerms = _xpath.compile(SPAN);
            _eTarget = _xpath.compile(TARGET);
            _eCorresp = _xpath.compile(CORRESP);
            _eAna = _xpath.compile(ANA);
            _eSpanWordForms = _xpath.compile("//ns:standOff[@type = 'wordForms']" +
                    "/ns:listAnnotation/tei:span");
            _eSpanLemma = _xpath.compile(".//tei:f[@name = 'lemma']/tei:string/text()");
            _eSpanPos = _xpath.compile(".//tei:f[@name = 'pos']/tei:symbol/@value");
            _eFirstId = _xpath.compile(".//tei:text//tei:w[@xml:id = $id]/@xml:id");
            _eNextId = _xpath.compile(".//tei:text//tei:w[@xml:id = $id]/following::tei:w[1]/@xml:id");
            _eSpanTarget = _xpath.compile("@target");
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
        if (!_target.isEmpty()) {
            fillTargetSpanMap();
            while (!_target.isEmpty()) {
                String t = _target.poll();
                String c = _corresp.poll();
                String l = _lexAna.poll();
                try {
                    List<String> tags = Arrays.asList(t.replace("#", "").split(" "));
                    if (tags.size() > 1) {
                        multiWordsExtractor(c, l, tags);
                    } else
                        singleWordExtractor(c, l, tags.get(0));
                } catch (XPathExpressionException e) {
                    LOGGER.error("error during the parsing of document", e);
                }
            }
        }
    }

    private void fillTargetSpanMap() {
        try {
            NodeList nodes = (NodeList) _eSpanWordForms.evaluate(_doc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength();i++){
                Node target = (Node) _eSpanTarget.evaluate(nodes.item(i), XPathConstants.NODE);
                Node lemma = (Node) _eSpanLemma.evaluate(nodes.item(i), XPathConstants.NODE);
                Node pos = (Node) _eSpanPos.evaluate(nodes.item(i), XPathConstants.NODE);
                _targetSpanMap.put(
                        target.getNodeValue(),
                        lemma.getNodeValue() + " " + pos.getNodeValue()
                );
            }
        } catch (XPathExpressionException e) {
            LOGGER.error("error during the parsing of document", e);

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
        if (equalsToCorrespTarget(tags)){
            XPathExpression eTargetsTermGetter = _xpath.compile(
                    ("//tei:text//tei:w" +
                            "[./preceding-sibling::tei:w[@xml:id = '@id_last'] or" +
                            " ./following-sibling::tei:w[@xml:id = '@id_first']]")
                            .replace("@id_first", tags.get(0)).replace("@id_last",tags.get(tags.size()-1))
            );
            NodeList nodes = (NodeList) eTargetsTermGetter.evaluate(_doc, XPathConstants.NODESET);
            extractWordForms(corresp, lex, nodes);
        }
    }

    private boolean equalsToCorrespTarget(List<String> tags) {
        Node firstNode;
        Node nextNode;
        for(int i = 0; i < tags.size(); i ++) {
            try {
                if (i == 0) {
                    _xpathVariableMap.put("id", "t" +  Integer.parseInt(tags.get(0).substring(1)));
                    firstNode = (Node) _eFirstId.evaluate(_doc, XPathConstants.NODE);
                    if (!(Objects.equals(tags.get(0), firstNode.getNodeValue())))
                        return false;
                }
                else {
                    _xpathVariableMap.put("id", "t" +  Integer.parseInt(tags.get(i-1).substring(1)));
                    nextNode = (Node) _eNextId.evaluate(_doc, XPathConstants.NODE);

                    if (!Objects.equals(tags.get(i), nextNode.getNodeValue())) {
                        return false;
                    }
                }
            } catch (XPathExpressionException e) {
                LOGGER.error("error during the parsing of document",e);
            }
        }
        return true;
    }

    private void extractWordForms(String c, String l, NodeList nodes) throws XPathExpressionException {
        for (int i = 0; i < nodes.getLength(); i++) {
            String targetValue = "#" + nodes.item(i).getAttributes().getNamedItem("xml:id").getNodeValue();
            addOccToLexicalProfile(_targetSpanMap.get(targetValue), c, l);
        }
    }

    public void extractTerms() {
        try {
            NodeList nodes = (NodeList) _eSpanTerms.evaluate(_doc, XPathConstants.NODESET);
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

    protected void addOccToLexicalProfile(String spanValue, String c, String l) {
        String key = normalizeKey(c, l);
        if (!_subLexics.containsKey(key)){
            _subLexics.put(key,new LexicalProfile());
        }
        _subLexics.get(key).addOccurrence(spanValue);
    }

    protected String normalizeKey(String c, String l) {
        if ("#DM0".equals(l)) {
            return (c + "_lexOff").replace("#", "");
        } else {
            return (c + "_lexOn").replace("#", "");
        }
    }

    private class XpathMapVariableResolver implements XPathVariableResolver {

        @Override
        public Object resolveVariable(QName qName) {
            return _xpathVariableMap.get(qName.getLocalPart());
        }
    }
}
