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
    XPathExpression _eSpanTerms;
    XPathExpression _eTarget;
    XPathExpression _eCorresp;
    XPathExpression _eAna;
    protected Map<String, LexicalProfile> _subLexics;
    private XPathExpression _eSpanWordForms;
    private XPathExpression _eSpanTarget;
    private XPathExpression _eSpanLemma;
    private XPathExpression _eSpanPos;
    private XPathExpression _eFirstId;
    private XPathExpression _eNextId;
    private XPathExpression _eMultiTagsGetter;
    private XPathExpression _eSimpleTagGetter;
    private DocumentBuilder _dBuilder;
    private Map<String, String> _xpathVariableMap = new HashMap<>();
    private Map<String, String> _targetSpanMap = new HashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(SubLexicExtractor.class.getName());

    public SubLexicExtractor(String p, Map<String, LexicalProfile> subLexics){
        XPath xpath = XPathFactory.newInstance().newXPath();
        XpathMapVariableResolver xpathMapVariableResolver = new XpathMapVariableResolver();
        _subLexics = subLexics;
        xpath.setNamespaceContext(NAMESPACE_CONTEXT);
        xpath.setXPathVariableResolver(xpathMapVariableResolver);
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
            _eSpanTerms = xpath.compile(SPAN_T);
            _eTarget = xpath.compile(TARGET_T);
            _eCorresp = xpath.compile(CORRESP_T);
            _eSpanTarget = xpath.compile(TARGET_W);
            _eAna = xpath.compile(ANA_T);
            _eSpanWordForms = xpath.compile(SPAN_W);
            _eSpanLemma = xpath.compile(LEMMA_W);
            _eSpanPos = xpath.compile(POS_W);
            _eFirstId = xpath.compile(FIRST_TEXT);
            _eNextId = xpath.compile(NEXT_TEXT);
            _eMultiTagsGetter = xpath.compile(MULTI_TEXT);
            _eSimpleTagGetter = xpath.compile(SIMPLE_TEXT);
        } catch (XPathExpressionException e) {
            LOGGER.error("cannot compile xpath expression",e);
        }
    }

    Deque<String> get_target() {
        return _target;
    }

    Deque<String> get_corresp() {
        return _corresp;
    }

    Deque<String> get_lexAna() {
        return _lexAna;
    }

    public void execute() {
        extractTerms();
        extractSubCorpus();
    }

    void extractSubCorpus() {
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
                        nodes.item(i).getAttributes().getNamedItem("target").getNodeValue(),
                        nodes.item(i).getTextContent().trim() + " " + pos.getNodeValue()
                );
            }
        } catch (XPathExpressionException e) {
            LOGGER.error("error during the parsing of document", e);

        }
    }

    private void singleWordExtractor(String corresp, String lex, String tag) throws XPathExpressionException {
        NodeList nodes;
        _xpathVariableMap.put("c_id", tag);
        nodes = (NodeList) _eSimpleTagGetter.evaluate(_doc, XPathConstants.NODESET);
        extractWordForms(corresp, lex, nodes);
    }

    private void multiWordsExtractor(String corresp, String lex, List<String> tags) throws XPathExpressionException {
            _xpathVariableMap.put("first", tags.get(0));
            _xpathVariableMap.put("last", tags.get(tags.size() - 1));
            NodeList nodes = (NodeList) _eMultiTagsGetter.evaluate(_doc, XPathConstants.NODESET);
            extractWordForms(corresp, lex, nodes);
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
                String ana = nodes.item(i).getAttributes().getNamedItem("ana").getNodeValue();
                if (!"#noDM".equals(ana) && !ana.isEmpty()){
                    addToTermsQueues(nodes,ana,i);
                }
            }
        } catch (XPathExpressionException e) {
            LOGGER.error("error during the parsing of document",e);
        }
    }

    public void addToTermsQueues(NodeList nodes, String ana, int i) throws XPathExpressionException {
        _target.add(nodes.item(i).getAttributes().getNamedItem("target").getNodeValue());
        _corresp.add(nodes.item(i).getAttributes().getNamedItem("corresp").getNodeValue());
        _lexAna.add(ana);
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
