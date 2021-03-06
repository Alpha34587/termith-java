package org.atilf.module.disambiguation.contextLexicon;

import org.atilf.models.disambiguation.ContextTerm;
import org.atilf.models.disambiguation.ContextWord;
import org.atilf.models.disambiguation.CorpusLexicon;
import org.atilf.models.disambiguation.LexiconProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.*;

import static org.atilf.resources.disambiguation.AnnotationResources.*;

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
 *        node belongs to the occurrence term candidate
 * @author Simon Meoni
 *         Created on 14/10/16.
 *
 */
public class ContextExtractor extends DefaultHandler implements Runnable {

    protected Map<String, LexiconProfile> _contextLexicon;
    protected List<ContextTerm> _terms = new LinkedList<>();
    protected ContextWord _lastContextWord;
    protected Deque<TreeMap<Integer,String>> _contextStack = new ArrayDeque<>();

    private Deque<String> _elementsStack = new ArrayDeque<>();
    private List<String> _allowedElements = new ArrayList<>();

    private CorpusLexicon _corpusLexicon;
    protected String _p;
    private File _xml;
    private int _threshold = 0;
    protected String _currentPosLemma = "";
    private List<String> _authorizedPOSTag = new ArrayList<>();

    /*
    SAX condition
     */
    protected boolean _inW = false;
    private boolean _inText = false;
    private boolean _inStandOff = false;

    private static final Logger LOGGER = LoggerFactory.getLogger(ContextExtractor.class.getName());


    /**
     * constructor of contextExtractor
     * @param p the path of the xml file
     * @param contextLexicon the contextLexicon of the termithIndex of the process (all the context is retained
     *                       in this variable)
     * @param corpusLexicon
     * @see LexiconProfile
     */
    public ContextExtractor(String p, Map<String, LexiconProfile> contextLexicon, CorpusLexicon corpusLexicon){
        /*
        initialize _p and _contextLexicon fields
         */
        _p = p;
        _contextLexicon = contextLexicon;
        _corpusLexicon = corpusLexicon;
        _xml = new File(_p);
    }

    ContextExtractor(String p, Map<String, LexiconProfile> contextLexicon, CorpusLexicon corpusLexicon,
                     int threshold){
        this(p,contextLexicon,corpusLexicon);
        _threshold = threshold;
    }

    ContextExtractor(String p, Map<String, LexiconProfile> contextLexicon, CorpusLexicon corpusLexicon,
                     List<String> allowedElements){
        this(p,contextLexicon,corpusLexicon);
        _allowedElements = allowedElements;
    }

    ContextExtractor(String p, Map<String, LexiconProfile> contextLexicon, List<String> authorizedPOSTag,
                     CorpusLexicon corpusLexicon){
        this(p,contextLexicon,corpusLexicon);
        _authorizedPOSTag = authorizedPOSTag;

    }

    public ContextExtractor(String p, Map<String, LexiconProfile> contextLexicon, CorpusLexicon corpusLexicon,
                            int threshold, List<String> authorizedPOSTag){
        this(p,contextLexicon,corpusLexicon,threshold);
        _authorizedPOSTag = authorizedPOSTag;
    }

    public ContextExtractor(String p, Map<String, LexiconProfile> contextLexicon, CorpusLexicon corpusLexicon,
                            int threshold, List<String> authorizedPOSTag,List<String> allowedElements){
        this(p,contextLexicon,corpusLexicon,threshold);
        _authorizedPOSTag = authorizedPOSTag;
        _allowedElements =allowedElements;
    }

    /**
     * getter for _terms fields
     * @return return a list of ContextTerms
     */
    public List<ContextTerm> getTerms() {
        return _terms;
    }

    public void execute() {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(_xml,this);
        } catch (Exception e) {
            LOGGER.error("cannot parse xml",e);
        }
    }

    /**
     * run method of Runnable class
     */
    @Override
    public void run() {
        LOGGER.info("extract contexts from {}",_p );
        this.execute();
        LOGGER.info("all contexts in {} has been extracted",_p);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        switch (qName) {
            case "ns:standOff":
                _inStandOff = true;
                LOGGER.info("term extraction started");
                break;
            case "text":
                _inText = true;
                LOGGER.info("context extraction started");
                break;
            case "w":
                _inW = true;
                break;
        }
        if (_inStandOff && "span".equals(qName)){
            extractTerms(attributes);
        }
        else if (_inText && !_inW && !"text".equals(qName)){
            _elementsStack.push(qName);
            _contextStack.push(new TreeMap<>());
        }
        else if (_inW) {
            _lastContextWord = new ContextWord(attributes.getValue("xml:id"));
        }
    }


    @Override
    public void endElement(String uri,
                           String localName, String qName) throws SAXException {
        switch (qName) {
            case "ns:standOff":
                _inStandOff = false;
                LOGGER.info("term extraction finished");
                _terms.sort((o1, o2) -> {
                    int comp = Integer.compare(o1.getBeginTag(),o2.getBeginTag());
                    if (comp == 0) {
                        comp = Integer.compare(o1.getEndTag(),o2.getEndTag());
                    }
                    return comp;
                });
                break;
            case "text":
                _inText = false;
                LOGGER.info("context extraction finished");
                break;
            case "w":
                _inW = false;
                break;
        }
        if (_inText && !"w".equals(qName)){
            searchTermsInContext();
        }
    }

    /**
     * search if the context of the top of the stack contains a term. The context is remove if it not contains a term
     */
    private void searchTermsInContext() {
        TreeMap<Integer,String> words = _contextStack.pop();
        Deque<ContextTerm> termStack = termAlignment(words);
        Deque<ContextTerm> termStackTemp = new ArrayDeque<>();
        if (termStack != null) {
            words.forEach(
                    (key,value) -> searchTermInContext(words, termStack, termStackTemp, key)
            );
        }
        _elementsStack.pop();
    }

    /**
     * this method finds in the current context all terms who contains in this context. For each word in the context,
     * the method check if the term of the top of the deque
     * has a begin target equals to the current word. it this condition is satisfied
     * the context is associated to this term. Finally, the term is removed to the _term parameter.
     * @param words the current context
     * @param termDeque the Deque of terms that we want to search in the words variable
     * @param termDequeTemp the temporary Deque used to track the multi words terms
     * @param target current browsed word
     */
    private void searchTermInContext(TreeMap<Integer, String> words, Deque<ContextTerm> termDeque, Deque<ContextTerm> termDequeTemp, Integer target) {
        if (!termDeque.isEmpty() && target == termDeque.peek().getBeginTag()) {
            if (termDeque.peek().getBeginTag() != termDeque.peek().getEndTag()) {
                termDequeTemp.add(termDeque.peek());
            } else if (isContext()) {
                addContextToLexiconMap(termDeque.peek(), words);
                _terms.remove(termDeque.peek());
            }
            termDeque.pop();
            searchTermInContext(words, termDeque, termDequeTemp, target);
        }

        if (!termDequeTemp.isEmpty() && termDequeTemp.peek().getEndTag() == target) {
            if (isContext()) {
                addContextToLexiconMap(termDequeTemp.peek(), words);
                _terms.remove(termDequeTemp.peek());
            }
            termDequeTemp.pop();
            searchTermInContext(words, termDeque, termDequeTemp, target);
        }
    }

    private boolean isContext(){
        if (_allowedElements.isEmpty()){
            return true;
        }

        if (_allowedElements.contains(_elementsStack.peek())){
            return true;
        }

        return false;

    }
    /**
     * get a sublist of _term field and return a Deque with these elements. The first element of the sublist has
     * a target value inferior to the target value of the last words of the context (the variable words)
     * @param words the current context
     * @return a Deque with terms
     */
    private Deque<ContextTerm> termAlignment(TreeMap<Integer, String> words) {
        Deque<ContextTerm> termDeque = new ArrayDeque<>();
        if (words.size() > 0) {
            Optional<ContextTerm> term = _terms.stream()
                    .filter(t -> t.getBeginTag() >= words.firstKey() && t.getBeginTag() <= words.lastKey())
                    .findFirst();
            if (term.isPresent()) {
                _terms.subList(_terms.indexOf(term.get()),_terms.size()).forEach(termDeque::add);
                return termDeque;
            }
        }
        return new ArrayDeque<>();
    }


    /**
     * the character event is used to extract the Pos/Lemma pair of a w element
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String posLemma = _currentPosLemma.concat(new String(ch,start,length));
        if (_inW) {
            if (posLemma.contains(" ")){
                _lastContextWord.setPosLemma(posLemma);
                _contextStack.forEach(words -> words.put(_lastContextWord.getTarget(), _lastContextWord.getPosLemma()));
                LOGGER.debug("add pos lemma pair: {} to corpus",posLemma);
                _inW = false;
                _currentPosLemma = "";
            }
            else {
                _currentPosLemma += new String(ch,start,length);
            }
        }
    }

    /**
     * parse the attribute of a span element and initialize a new ContextTerm object and add it to the _terms field
     * @param attributes the attributes of a span element
     */
    protected void extractTerms(Attributes attributes) {
        String ana = attributes.getValue("ana");
        if (!ana.equals(NO_DM.getValue())) {
            _terms.add(new ContextTerm(attributes.getValue("corresp"),
                    ana,
                    attributes.getValue("target")));
            LOGGER.debug("term extracted: " + attributes.getValue("corresp"));
        }
    }

    /**
     * add to lexicalProfile a context for terminology entry
     * @param term the term id entry suffixes by _lexOn or _lexOff
     */
    private void addContextToLexiconMap(ContextTerm term, TreeMap<Integer, String> words) {
        /*
        create new entry if the key not exists in the _contextLexicon field
         */
        String key = normalizeKey(term.getCorresp(), term.getAna());
        Map<Integer,String> contextTarget = filterPos(contextThreshold(term,words));

        if (contextTarget.size() != 0 && words.size() != term.getSize()) {
            addContextToLexicon(key, contextTarget);
        }
    }

    protected void addContextToLexicon(String key, Map<Integer, String> contextTarget) {
        if (!_contextLexicon.containsKey(key)) {
            _contextLexicon.put(key, new LexiconProfile());
        }
        _corpusLexicon.addContext(new ArrayList<>(contextTarget.values()));
        _contextLexicon.get(key).addOccurrences(new ArrayList<>(contextTarget.values()));
    }

    private Map<Integer,String> filterPos(Map<Integer, String> contextTarget) {

        if (!_authorizedPOSTag.isEmpty()) {
            contextTarget.entrySet().removeIf(entry -> !_authorizedPOSTag.contains(
                    entry.getValue().split(" ")[1])
            );
        }
        return contextTarget;
    }

    private Map<Integer,String> contextThreshold(ContextTerm term, TreeMap<Integer, String> words){
        if (_threshold == 0){
            return new HashMap<>(words);
        }
        else {
            SortedMap<Integer, String> thresholdContext = new TreeMap<>(words);

            if (term.getBeginTag()  > _threshold) {
                thresholdContext = thresholdContext.subMap(term.getBeginTag() - _threshold, words.lastKey() + 1);
            }
            if (term.getEndTag() + _threshold < words.lastKey()){
                thresholdContext = thresholdContext.subMap(thresholdContext.firstKey(), term.getEndTag() +
                        _threshold + 1 );
            }
            return thresholdContext;
        }
    }

    /**
     * normalize the key with suffix and remove '#' character
     * @param c the term id entry
     * @param l ana value
     * @return the normalized key
     */
    protected String normalizeKey(String c, String l) {
        if (DM4.getValue().equals(l)) {
            return (c + LEX_ON.getValue()).replace("#", "");
        } else {
            return (c + LEX_OFF.getValue()).replace("#", "");
        }
    }
}
