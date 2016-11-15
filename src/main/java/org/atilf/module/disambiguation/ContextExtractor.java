package org.atilf.module.disambiguation;

import org.atilf.models.disambiguation.LexicalProfile;
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
 *         - the context extractor moudule extract the context of a terminology entries of the learning corpus.
 *         - the extraction is performed on the working xml files format converted with the
 *         DisambiguationXslTransformer module
 *         (there are several examples in the folder : src/test/resources/corpus/disambiguation/transform-tei).
 *         - the result of the process is retained on the _contextLexicon field. Each entry of this map is a pair
 *         of String/Multiset. The String corresponds to id of a term candidate suffixes with lexOn or lexOff :
 *         keys suffixes by lexOn is linked to the context (the lexicalProfile->multiset variable)
 *         in which the term candidate is a terminology whereas the key suffixe by lexOff is linked to the
 *         non-terminologic context
 *
 *         an example of the input :
 *           <?xml version="1.0" encoding="UTF-8"?>
 *               <TEI>
 *               <ns:standOff>
 *                   <span ana="#DM4" corresp="#entry-13471" target="#t9"/>
 *                   <span ana="#DM0" corresp="#entry-13471" target="#t13 #t14"/>
 *               </ns:standOff>
 *               <text>
 *                          <body>
 *                              <p>
 *                                  <p>
 *                                      <w xml:id="t1">ce PRO:DEM</w>
 *                                      <w xml:id="t2">article NOM</w>
 *                                      <w xml:id="t3">présenter VER:pres</w>
 *                                      <note>
 *                                          <w xml:id="t4">un DET:ART</w>
 *                                          <w xml:id="t5">étude NOM</w>
 *                                      </note>
 *                                      <w xml:id="t6">comparer VER:pper</w>
 *                                      <s>
 *                                           <w xml:id="t7">du PRP:det</w>
 *                                           <note><w xml:id="t8">donnée NOM</w></note>
 *                                      </s>
 *                                      <w xml:id="t9">archéo-ichtyofauniques ADJ</w>
 *                                      <w xml:id="t10">livrer VER:pper</w>
 *                                      <w xml:id="t11">par PRP</w>
 *                                  </p>
 *                                  <p>
 *                                      <w xml:id="t12">deux NUM</w>
 *                                      <w xml:id="t13">site NOM</w>
 *                                      <w xml:id="t14">de PRP</w>
 *                                      <head>
 *                                          <w xml:id="t15">le DET:ART</w>
 *                                      </head>
 *                                      <w xml:id="t16">âge NOM</w>
 *                                      <w xml:id="t17">du PRP:det</w>
 *                                 </p>
 *                              </p>
 *                          </body>
 *               </text>
 *               </TEI>
 *
 *         an exemple of the output after the extraction with the example from above :
 *         {
 *             "#entry-13471_lexOn" : {ce PRO:DEM, article NOM, présenter VER:pres, un DET:ART, étude NOM,
 *             comparer VER:pper, du PRP:det, donnée NOM, livrer VER:pper, par PRP}
 *
 *             "#entry-13471_lexOff" : {deux NUM, site NOM, de PRP, le DET:ART, âge NOM, du PRP:det}
 *         }
 *
 *        the principle is to find the parent node of the term candidate and extract all the w element except the
 *        node belongs to the occurence term candidate
 * @author Simon Meoni
 *         Created on 14/10/16.
 *
 */
public class ContextExtractor implements Runnable{
    private XPathExpression _simpleGetter;
    Deque<String> _target = new ArrayDeque<>();
    Deque<String> _corresp = new ArrayDeque<>();
    Deque<String> _lexAna = new ArrayDeque<>();
    Document _doc;
    XPathExpression _eSpanTerms;
    Map<String, LexicalProfile> _contextLexicon;
    private String _p;
    private XPathExpression _lastGetter;
    private XPathExpression _firstGetter;
    private XPathExpression _eTagsGetter;
    private DocumentBuilder _dBuilder;
    private Set<Node> _nodeSet = new HashSet<>();
    private Map<String, String> _xpathVariableMap = new HashMap<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(ContextExtractor.class.getName());


    /**
     * constructor of contextExtractor
     * @param p the path of the xml file
     * @param contextLexicon the contextLexicon of the termithIndex of the process (all the context is retained
     *                       in this variable)
     * @see LexicalProfile
     */
    public ContextExtractor(String p, Map<String, LexicalProfile> contextLexicon){

        /*
        initialize the path and all the necessary fields needed to parse xml file
         */

        /*
        initialize _p and _contextLexicon fields
         */
        _p = p;
        _contextLexicon = contextLexicon;
        /*
        The xpathMapVariableResolver is an object that it used to change xsl variable during dom parsing
         */
        XpathMapVariableResolver xpathMapVariableResolver = new XpathMapVariableResolver();
        XPath xpath = XPathFactory.newInstance().newXPath();
        /*
        set the namespaceContext of the parser and set the XpathVariableResolver fields
        with the xpathMapVariableResolver
         */
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
            /*
            parsing document
             */
            _doc = _dBuilder.parse(p);
        } catch (SAXException | IOException e) {
            LOGGER.error("error during the parsing of document",e);
        }
        try {
            /*
            compiling all the xpath Expression needed for the execute method
             */
            /*
            extract span term candidate element
             */
            _eSpanTerms = xpath.compile(SPAN);
            /*
            extract all w elements from a node
             */
            _eTagsGetter = xpath.compile(TAG_GETTER);
            /*
            get the parent node of an term candidate occurrence who have a size of one
            (only one w element belongs to this term candidate occurrence )
             */
            _simpleGetter = xpath.compile(CONTEXT_GETTER_SIMPLE);
            /*
            get the parent node of the first w element belongs to a term candidate occurrence who have a size
            superior to one
             */
            _firstGetter = xpath.compile(CONTEXT_GETTER_FIRST);
            /*
            get the parent node of the last w element belongs to a term candidate occurrence who have a size
            superior to one
             */
            _lastGetter = xpath.compile(CONTEXT_GETTER_LAST);
        } catch (XPathExpressionException e) {
            LOGGER.error("cannot compile xpath expression",e);
        }
    }

    /**
     * getter of _target
     * @return return the targets attributes value of extracted term candidates
     */
    Deque<String> getTarget() {
        return _target;
    }

    /**
     * getter of _corresp
     * @return return the corresp attributes value of extracted term candidates
     */
    Deque<String> getCorresp() {
        return _corresp;
    }

    /**
     * getter of _lexAna
     * @return return the ana attributes value of term candidates
     */
    Deque<String> getLexAna() {
        return _lexAna;
    }


    /**
     * the execute method call two methods :
     *      - the extractTerms method : extract terms candidates of the document and put the result into Deque :
     *      _target, _corresp and _lexAna. The _target Deque retained the xml:id value who belong to a term candidate
     *      occurrence (e.g #t1, #t2 ...).
     *      The _corresp Deque retained the terminology entry of a term candidate
     *      occurrence (e.g #entry-13471).
     *      The _lexAna Deque retained the manual terminology annotation of a term candidate occurrence
     *      (e.g #DM4, #DM0 ...). this annotation is used to determine if a context belongs to terminology
     *      or non-terminology context of term candidate
     */
    public void execute() {
        extractTerms();
        extractContext();
    }

    /*
    the extractContext method extract a context of term candidate occurrence. at each extracted term candidate,
    this method called multiWordExtractor if the occurrence term candidate have a size superior of one. Otherwise
    it calls the singleWordExtractor method
     */
    void extractContext() {

        while (!_target.isEmpty()) {
            /*
            get the current term candidate
             */
            String t = _target.poll();
            String c = _corresp.poll();
            String l = _lexAna.poll();
            try {
                /*
                remove '#' character and convert t variable into list of tag
                 */
                List<String> tags = Arrays.asList(t.replace("#", "").split(" "));
                if (tags.size() > 1) {
                    /*
                    call multiWordsExtractor method
                     */
                    multiWordsExtractor(c, l, tags);
                }
                else
                    /*
                    call singleWordExtractor method
                     */
                    singleWordExtractor(c, l, tags.get(0));
                LOGGER.debug(("add words to the term : " + c + "-" + l).replace("#",""));
            } catch (XPathExpressionException e) {
                LOGGER.error("error during the parsing of document", e);
            }
        }
    }

    private void singleWordExtractor(String corresp, String lex, String tag) throws XPathExpressionException {
        /*
        the _xpathVariableMap is updated : the c_id xpath variable is used by the _simpleGetter xpath expression.
         */
        _xpathVariableMap.put("c_id", tag);

        /*
        find the parent node of the current term candidate.
         */
        Node node = (Node) _simpleGetter.evaluate(_doc, XPathConstants.NODE);

        /*
        adding to the _nodeSet field the w element descendant of the variable node.
         */
        addNodeList((NodeList) _eTagsGetter.evaluate(node, XPathConstants.NODESET));
        /*
        extract the text content of the tei w element (the text content of a tei w element is the lemmatised form
        and the POS-tagging separate with a space)
         */
        extractWordForms(corresp, lex, tag);
        /*
        clear _nodeSet field after extracting : all the context words is retained on the lexicalProfile of the current
          occurrence term candidate
         */
        _nodeSet.clear();
    }

    private void multiWordsExtractor(String corresp, String lex, List<String> tags) throws XPathExpressionException {
        /*
        the _xpathVariableMap is updated : the first xpath variable is used by the _firstGetter xpath expression.
        And the last xpath variable is used by _lastGetter xpath expression
         */
        _xpathVariableMap.put("first", tags.get(0));
        _xpathVariableMap.put("last", tags.get(tags.size() - 1));
        /*
        find the parent node of the first tag 
         */
        Node firstParentNode = (Node) _firstGetter.evaluate(_doc, XPathConstants.NODE);
        /*
        find the parent node of the last tag
         */
        Node lastParentNode = (Node) _lastGetter.evaluate(_doc, XPathConstants.NODE);
        /*
        extract the text content of the tei w element (the text content of a tei w element is the lemmatised form
        and the POS-tagging separate with a space)
         */
        addNodeList((NodeList) _eTagsGetter.evaluate(firstParentNode, XPathConstants.NODESET));

        /*
        check if the first tag and the last tag have the same parent. This fragment has been written to avoid this case :
          <p>
              <note>
                   <w xml:id="t12">deux NUM</w>
                   <w xml:id="t13">site NOM</w>
                   <w xml:id="t14">de PRP</w>
                   <w xml:id="t15">le DET:ART</w>
             </note>
             <note>
                   <w xml:id="t16">âge NOM</w>
                   <w xml:id="t17">du PRP:det</w>
            <note>
            <p>
            .......
            </p>
        </p>

        where t14,t15,t16 is the term candidate.
        Without this test the context the word of t17 element is missing

         */
        if (!firstParentNode.equals(lastParentNode)) {
            /*
            add to nodeSet the extracted w element
             */
            addNodeList((NodeList) _eTagsGetter.evaluate(lastParentNode, XPathConstants.NODESET));
        }
        /*
        extract the text content of the tei w element (the text content of a tei w element is the lemmatised form
        and the POS-tagging separate with a space)
         */
        extractWordForms(corresp, lex, tags);
        /*
        clear _nodeSet field after extracting : all the context words is retained on the lexicalProfile of the current
          occurrence term candidate
         */
        _nodeSet.clear();
    }

    /**
     * add to _nodeSet field the extracted node of a nodeList
     * @param nodes the result nodeList object of an evaluate xpath expression
     */
    private void addNodeList(NodeList nodes) {
        for (int i = 0; i < nodes.getLength(); i++){
            _nodeSet.add(nodes.item(i));
        }
    }

    /**
     * this extractWordForms method for an occurrence term candidate who has the size of one
     * @param c the term entry id of a term
     * @param l the manual annotation value of an occurrence term candidate
     * @param tag the only tag of the occurrence term candidate
     */
    private void extractWordForms(String c, String l, String tag) {
        ArrayList<String> list = new ArrayList<>();
        list.add(tag);
        extractWordForms(c, l, list);
    }

    /**
     * this method extract the text content of w element belongs to a context of an occurrence term candidate
     * and exclude w element who are include in a term candidate
     * @param c the term entry id of a term
     * @param l the manual annotation value of an occurrence term candidate
     * @param tags the list of tag of the occurrence term candidate
     */
    private void extractWordForms(String c, String l, List<String> tags){
        _nodeSet.forEach(
                el -> {
                    /*
                    get the id of the current w element
                     */
                    String id = el.getAttributes().getNamedItem("xml:id").getNodeValue();
                    /*
                    check if the tag is contained on the term occurrence candidate
                     */
                    if (!tags.contains(id)) {
                        /*
                        add the text content of an element into the multiset of the current term entry
                         */
                        addOccToLexicalProfile(el.getTextContent(), c, l);
                    }
                }
        );
    }

    /**
     * extract occurrence term candidate of a xml file
     */
    public void extractTerms() {
        try {
            /*
             * get all the span into the ns:standOff element
             */
            NodeList nodes = (NodeList) _eSpanTerms.evaluate(_doc, XPathConstants.NODESET);
            for (int i = 0; i < nodes.getLength(); i++) {
                /*
                 * get the ana value
                 */
                String ana = nodes.item(i).getAttributes().getNamedItem("ana").getNodeValue();
                /*
                 * if the ana value has been annotated, the term is added on the queue.
                   * the #noDM attribute means that the term has not annotation and the context is undefined
                   * between the terminology context and the non-terminology context
                 */
                if (!"#noDM".equals(ana) && !ana.isEmpty()){
                    /*
                    add term occurrence candidate to queue
                     */
                    addToTermsQueues(nodes.item(i),ana);
                }
            }
        } catch (XPathExpressionException e) {
            LOGGER.error("error during the parsing of document",e);
        }
    }

    /**
     * add to queue a term candidate
     * @param node the current span node (it is an occurrence term candidate)
     * @param ana the ana value of the current node
     * @throws XPathExpressionException
     */
    void addToTermsQueues(Node node, String ana) throws XPathExpressionException {
        _target.add(node.getAttributes().getNamedItem("target").getNodeValue());
        _corresp.add(node.getAttributes().getNamedItem("corresp").getNodeValue());
        _lexAna.add(ana);
    }

    /**
     * add to lexicalProfile a context for terminology entry
     * @param word add pair of lemma/POS into lexicalProfile multiset
     * @param c the term id entry
     * @param l the annotation of a term occurrence annotation
     */
    protected void addOccToLexicalProfile(String word, String c, String l) {
        /*
        determine which suffix will be added to term entry.
        _lexOff if l variable is equals to #DM0 and _lexOn if l variable is equals to #DM1, #DM2, #DM3, #DM4
         */
        String key = normalizeKey(c, l);
        /*
        create new entry if the key not exists in the _contextLexicon field
         */
        if (!_contextLexicon.containsKey(key)){
            _contextLexicon.put(key,new LexicalProfile());
        }
        _contextLexicon.get(key).addOccurrence(word);
    }

    /**
     * normalize the key with suffix and remove '#' character
     * @param c the term id entry
     * @param l ana value
     * @return the normalized key
     */
    protected String normalizeKey(String c, String l) {
        if ("#DM0".equals(l)) {
            return (c + "_lexOff").replace("#", "");
        } else {
            return (c + "_lexOn").replace("#", "");
        }
    }

    /**
     * run method of Runnable class
     */
    @Override
    public void run() {
        LOGGER.info("add " + _p + " to sub lexicon");
        this.execute();
        LOGGER.info(_p + " added");
    }

    /**
     * the XpathMapVariableResolver is a class who can permits to change value of xpath variable
     */
    private class XpathMapVariableResolver implements XPathVariableResolver {

        @Override
        public Object resolveVariable(QName qName) {
            return _xpathVariableMap.get(qName.getLocalPart());
        }
    }
}
