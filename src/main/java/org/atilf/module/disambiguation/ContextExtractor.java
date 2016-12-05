package org.atilf.module.disambiguation;

import com.google.common.collect.Lists;
import org.atilf.models.disambiguation.ContextTerm;
import org.atilf.models.disambiguation.CorpusLexicon;
import org.atilf.models.disambiguation.LexiconProfile;
import org.atilf.models.disambiguation.ContextWord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.atilf.models.disambiguation.AnnotationResources.*;

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
public class ContextExtractor extends DefaultHandler implements Runnable {

    Map<String, LexiconProfile> _contextLexicon;
    private Map<String,List<String>> _targetContext = new HashMap<>();
    private CorpusLexicon _corpusLexicon;
    protected List<ContextTerm> _terms = new ArrayList<>();
    private String _p;
    private File _xml;

    /*
    variable used during SAX parsing
     */
    ContextTerm _currentTerm = null;
    ContextWord _lastContextWord;
    private Stack<List<ContextWord>> _contextStack = new Stack<>();
    /*
    SAX condition
     */
    boolean _inW = false;
    private boolean _inText = false;
    protected boolean _inStandOff = false;

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
            e.printStackTrace();
        }
    }

    /**
     * run method of Runnable class
     */
    @Override
    public void run() {
        LOGGER.info("extract contexts from " + _p );
        this.execute();
        try {
            Files.delete(Paths.get(_p));
        } catch (IOException e) {
            LOGGER.error("cannot delete file", e);
        }
        LOGGER.info("all contexts in " + _p + "has been extracted");
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
        if (_inStandOff && qName.equals("span")){
            extractTerms(attributes);
        }
        else if (_inText && !_inW && !qName.equals("text")){
            _contextStack.push(new ArrayList<>());
        }

        else if (_inW){
            _lastContextWord = new ContextWord(attributes.getValue("xml:id"));
            _contextStack.forEach(words -> words.add(_lastContextWord));
        }
    }

    @Override
    public void endElement(String uri,
                           String localName, String qName) throws SAXException {
        switch (qName) {
            case "ns:standOff":
                _inStandOff = false;
                LOGGER.info("term extraction finished");
                break;
            case "text":
                _inText = false;
                LOGGER.info("context extraction finished");
                break;
            case "w":
                _inW = false;
                break;
        }


        if (_inText && !qName.equals("w")){
            searchTermsInContext();
        }
        else if (!_inStandOff){
            _terms.sort((o1, o2) -> {
                int t1 = Integer.parseInt(o1.getTarget().get(0).replace("t", ""));
                int t2 = Integer.parseInt(o2.getTarget().get(0).replace("t", ""));
                int comp = Integer.compare(t1,t2);
                if (comp == 0) {
                    comp = ((Integer) o1.getTarget().size()).compareTo(o2.getTarget().size()) * -1;
                }
                return comp;
            });
        }

    }

    /**
     * search if the context of the top of the stack contains a term. The context is remove if it not contains a term
     */
    private void searchTermsInContext() {
        Iterator<ContextTerm> termsIt = _terms.iterator();
        while (inContext(termsIt)){
            if (inWords()) {
                addWordsToLexicon(
                        normalizeKey(_currentTerm.getCorresp(), _currentTerm.getAna()),
                        _contextStack.peek()
                );
                termsIt.remove();
            }
        }
        _contextStack.pop();
    }

    /**
     * this method is used to check if the current context can probably contains the terms.
     * @param termsIt the iterator of _terms
     * @return return true if the first target word of the term is inferior or equal to the last target word.
     */
    private boolean inContext(Iterator<ContextTerm> termsIt) {
        List<ContextWord> contextWords = _contextStack.peek();
        if (termsIt.hasNext() && !contextWords.isEmpty()){
            _currentTerm = termsIt.next();
            int firstTermTarget = Integer.parseInt(_currentTerm.getTarget().get(0).replace("t", ""));
            int lastStackTarget = Integer.parseInt(contextWords.get(contextWords.size()-1).getTarget().replace("t", ""));
            return firstTermTarget <= lastStackTarget;
        }
        else {
            return false;
        }

    }

    /**
     * check if an term occurrence is contained by a context
     * @return true if _currentTerm is contained / false if _currentTerm is not contained
     */
    private boolean inWords(){
        List<String> stackTargets = new ArrayList<>();
        /*
        fill stackTargets with all target of each words contains in the context on top of the stack
         */
        _contextStack.peek().forEach(contextWord -> stackTargets.add(contextWord.getTarget()));
        return stackTargets.containsAll(_currentTerm.getTarget());
    }

    /**
     * the character event is used to extract the Pos/Lemma pair of a w element
     */
    @Override
    public void characters(char ch[],
                           int start, int length) throws SAXException {
        if (_inW){
            String posLemma = new String(ch,start,length);
            _lastContextWord.setPosLemma(posLemma);
            _corpusLexicon.addOccurrence(posLemma);
            LOGGER.debug("add pos lemma pair: "+ posLemma +" to corpus");
            _inW = false;
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
     * @param key the term id entry suffixes by _lexOn or _lexOff
     */
    private void addWordsToLexicon(String key, List<ContextWord> context) {

        /*
        create new entry if the key not exists in the _contextLexicon field
         */
        context.forEach(
                contextWord -> {
                    String target = contextWord.getTarget();
                    if (!_currentTerm.inTerm(target) && !inTargetContext(key,target)){
                        addWordToLexicon(key,contextWord);
                    }
                }
        );
    }

    protected void addWordToLexicon(String key, ContextWord contextWord){
        if (!_contextLexicon.containsKey(key)){
            _contextLexicon.put(key,new LexiconProfile());
        }
        _contextLexicon.get(key).addOccurrence(contextWord.getPosLemma());
        LOGGER.debug("add words to term: " + key);
    }


    protected boolean inTargetContext(String key, String target){
        if (!_targetContext.containsKey(key)){
            _targetContext.put(key, Lists.newArrayList(target));
            return false;
        }
        else {
            List<String> context = _targetContext.get(key);
            if (context.contains(target)){
                return true;
            }
            else {
                context.add(target);
                return false;
            }
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


