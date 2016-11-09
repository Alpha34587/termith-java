package org.atilf.module.disambiguation;

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

import static org.atilf.models.disambiguation.ContextResources.*;

/**
 * @author Simon Meoni
 *         Created on 14/10/16.
 */
public class ContextExtractor implements Runnable{
    private XPathExpression _simpleGetter;
    Deque<String> _target = new ArrayDeque<>();
    Deque<String> _corresp = new ArrayDeque<>();
    Deque<String> _lexAna = new ArrayDeque<>();
    Document _doc;
    XPathExpression _eSpanTerms;
    XPathExpression _eTarget;
    XPathExpression _eCorresp;
    XPathExpression _eAna;
    Map<String, LexicalProfile> _contextLexicon;
    private String _p;
    private XPathExpression _lastGetter;
    private XPathExpression _firstGetter;
    private XPathExpression _eTagsGetter;
    private DocumentBuilder _dBuilder;
    private Set<Node> _nodeSet = new HashSet<>();
    private Map<String, String> _xpathVariableMap = new HashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(ContextExtractor.class.getName());

    public ContextExtractor(String p, Map<String, LexicalProfile> contextLexicon){
        _p = p;
        _contextLexicon = contextLexicon;
        XpathMapVariableResolver xpathMapVariableResolver = new XpathMapVariableResolver();
        XPath xpath = XPathFactory.newInstance().newXPath();
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
            _eSpanTerms = xpath.compile(SPAN);
            _eTarget = xpath.compile(TARGET);
            _eCorresp = xpath.compile(CORRESP);
            _eTagsGetter = xpath.compile(TAG_GETTER);
            _simpleGetter = xpath.compile(CONTEXT_GETTER_SIMPLE);
            _firstGetter = xpath.compile(CONTEXT_GETTER_FIRST);
            _lastGetter = xpath.compile(CONTEXT_GETTER_LAST);
        } catch (XPathExpressionException e) {
            LOGGER.error("cannot compile xpath expression",e);
        }
    }

    Deque<String> getTarget() {
        return _target;
    }

    Deque<String> getCorresp() {
        return _corresp;
    }

    Deque<String> getLexAna() {
        return _lexAna;
    }

    public void execute() {
        extractTerms();
        extractContext();
    }

    void extractContext() {
        while (!_target.isEmpty()) {
            String t = _target.poll();
            String c = _corresp.poll();
            String l = _lexAna.poll();
            try {
                List<String> tags = Arrays.asList(t.replace("#", "").split(" "));
                if (tags.size() > 1) {
                    multiWordsExtractor(c, l, tags);
                }
                else
                    singleWordExtractor(c, l, tags.get(0));

                LOGGER.debug(("add words to the term : " + c + "-" + l).replace("#",""));
            } catch (XPathExpressionException e) {
                LOGGER.error("error during the parsing of document", e);
            }
        }
    }

    private void singleWordExtractor(String corresp, String lex, String tag) throws XPathExpressionException {
        _xpathVariableMap.put("c_id", tag);

        Node node = (Node) _simpleGetter.evaluate(_doc, XPathConstants.NODE);
        addNodeList((NodeList) _eTagsGetter.evaluate(node, XPathConstants.NODESET));
        extractWordForms(corresp, lex, tag);
        _nodeSet.clear();
    }

    private void multiWordsExtractor(String corresp, String lex, List<String> tags) throws XPathExpressionException {
        _xpathVariableMap.put("first", tags.get(0));
        _xpathVariableMap.put("last", tags.get(tags.size() - 1));
        Node firstParentNode = (Node) _firstGetter.evaluate(_doc, XPathConstants.NODE);
        Node lastParentNode = (Node) _lastGetter.evaluate(_doc, XPathConstants.NODE);
        addNodeList((NodeList) _eTagsGetter.evaluate(firstParentNode, XPathConstants.NODESET));

        if (!firstParentNode.equals(lastParentNode)) {
            addNodeList((NodeList) _eTagsGetter.evaluate(lastParentNode, XPathConstants.NODESET));
        }
        extractWordForms(corresp, lex, tags);
        _nodeSet.clear();
    }

    private void addNodeList(NodeList nodes) {
        for (int i = 0; i < nodes.getLength(); i++){
            _nodeSet.add(nodes.item(i));
        }
    }

    private void extractWordForms(String c, String l, String tag) throws XPathExpressionException {
        ArrayList<String> list = new ArrayList<>();
        list.add(tag);
        extractWordForms(c, l, list);
    }

    private void extractWordForms(String c, String l, List<String> tags) throws XPathExpressionException {
        List<String> duplicateTags = new ArrayList<>();
        _nodeSet.forEach(
                el -> {
                    String id = el.getAttributes().getNamedItem("xml:id").getNodeValue();
                    if (!tags.contains(id) && !duplicateTags.contains(id)) {
                        addOccToLexicalProfile(el.getTextContent(), c, l);
                        duplicateTags.add(id);
                    }
                }
        );
    }

    public void extractTerms() {
        try {
            NodeList nodes = (NodeList) _eSpanTerms.evaluate(_doc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++) {
                String ana = nodes.item(i).getAttributes().getNamedItem("ana").getNodeValue();
                if (!"#noDM".equals(ana) && !ana.isEmpty()){
                    addToTermsQueues(nodes.item(i),ana);
                }
            }
        } catch (XPathExpressionException e) {
            LOGGER.error("error during the parsing of document",e);
        }
    }

    void addToTermsQueues(Node node, String ana) throws XPathExpressionException {
        _target.add(node.getAttributes().getNamedItem("target").getNodeValue());
        _corresp.add(node.getAttributes().getNamedItem("corresp").getNodeValue());
        _lexAna.add(ana);
    }

    protected void addOccToLexicalProfile(String spanValue, String c, String l) {
        String key = normalizeKey(c, l);
        if (!_contextLexicon.containsKey(key)){
            _contextLexicon.put(key,new LexicalProfile());
        }
        _contextLexicon.get(key).addOccurrence(spanValue);
    }

    protected String normalizeKey(String c, String l) {
        if ("#DM0".equals(l)) {
            return (c + "_lexOff").replace("#", "");
        } else {
            return (c + "_lexOn").replace("#", "");
        }
    }

    @Override
    public void run() {
        LOGGER.info("add " + _p + " to sub lexicon");
        this.execute();
        LOGGER.info(_p + " added");
    }

    private class XpathMapVariableResolver implements XPathVariableResolver {

        @Override
        public Object resolveVariable(QName qName) {
            return _xpathVariableMap.get(qName.getLocalPart());
        }
    }
}
