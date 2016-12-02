package org.atilf.module.disambiguation;

import org.atilf.models.disambiguation.LexiconProfile;
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

import static org.atilf.models.disambiguation.AnnotationResources.DM4;
import static org.atilf.models.disambiguation.AnnotationResources.NO_DM;

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
    protected List<Terms> _terms = new ArrayList<>();
    Map<String, LexiconProfile> _contextLexicon;
    private String _p;
    private File _xml;

    /*
    variable used during SAX parsing
     */
    private Word _lastWord;
    private Terms _currentTerm = null;
    private Stack<List<Word>> _wordsStack = new Stack<>();
    /*
    SAX condition
     */
    private boolean _inW = false;
    private boolean _inText = false;
    private boolean _inStandOff = false;

    private static final Logger LOGGER = LoggerFactory.getLogger(ContextExtractor.class.getName());


    /**
     * constructor of contextExtractor
     * @param p the path of the xml file
     * @param contextLexicon the contextLexicon of the termithIndex of the process (all the context is retained
     *                       in this variable)
     * @see LexiconProfile
     */
    public ContextExtractor(String p, Map<String, LexiconProfile> contextLexicon){
        /*
        initialize _p and _contextLexicon fields
         */
        _p = p;
        _contextLexicon = contextLexicon;
        _xml = new File(_p);
    }

    public List<Terms> getTerms() {
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

    /*
    override SaxHandler
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        switch (qName) {
            case "ns:standOff":
                _inStandOff = true;
                break;
            case "text":
                _inText = true;
                break;
            case "w":
                _inW = true;
                break;
        }
        if (_inStandOff && qName.equals("span")){
            extractTerms(attributes);
        }
        else if (_inText && !_inW && !qName.equals("text")){
            _wordsStack.push(new ArrayList<>());
        }

        else if (_inW){
            _lastWord = new Word(attributes.getValue("xml:id"));
            _wordsStack.forEach(words -> words.add(_lastWord));
        }
    }

    @Override
    public void endElement(String uri,
                           String localName, String qName) throws SAXException {
        switch (qName) {
            case "ns:standOff":
                _inStandOff = false;
                break;
            case "text":
                _inText = false;
                break;
            case "w":
                _inW = false;
                break;
        }
        if (_inText && !qName.equals("w")){
            searchTermsInContext();
        }
    }

    private void searchTermsInContext() {
        Iterator<Terms> termsIt = _terms.iterator();
        while (inContext(termsIt)){
            if (inWords()) {
                addWordsToLexicalProfile(
                        normalizeKey(_currentTerm.getCorresp(), _currentTerm.getAna()),
                        _wordsStack.peek()
                );
                termsIt.remove();
            }
        }
        _wordsStack.pop();
    }

    private boolean inContext(Iterator<Terms> termsIt) {
        List<Word> words = _wordsStack.peek();
        if (termsIt.hasNext() && !words.isEmpty()){
            _currentTerm = termsIt.next();
            int firstTermTarget = Integer.parseInt(_currentTerm.getTarget().get(0).replace("t", ""));
            int lastStackTarget = Integer.parseInt(words.get(words.size()-1).getTarget().replace("t", ""));
            return firstTermTarget <= lastStackTarget;
        }
        else {
            return false;
        }

    }

    private boolean inWords(){
        List<String> stackTargets = new ArrayList<>();
        _wordsStack.peek().forEach(word -> stackTargets.add(word.getTarget()));
        return stackTargets.containsAll(_currentTerm.getTarget());
    }

    @Override
    public void characters(char ch[],
                           int start, int length) throws SAXException {
        if (_inW){
            _lastWord.setPosLemma(new String(ch,start,length));
            _inW = false;
        }
    }

    private void extractTerms(Attributes attributes) {
        String ana = attributes.getValue("ana");
        if (!ana.equals(NO_DM.getValue())) {
            _terms.add(new Terms(attributes.getValue("corresp"),
                    ana,
                    attributes.getValue("target")));
        }
    }

    /**
     * add to lexicalProfile a context for terminology entry
     * @param key the term id entry suffixes by _lexOn or _lexOff
     */
    private void addWordsToLexicalProfile(String key,List<Word> context) {

        /*
        create new entry if the key not exists in the _contextLexicon field
         */
        context.forEach(
                word -> {
                    if (!_currentTerm.getTarget().contains(word.getTarget())){
                        if (!_contextLexicon.containsKey(key)){
                            _contextLexicon.put(key,new LexiconProfile());
                        }
                        _contextLexicon.get(key).addOccurrence(word.getPosLemma());
                    }
                }
        );
    }

    /**
     * normalize the key with suffix and remove '#' character
     * @param c the term id entry
     * @param l ana value
     * @return the normalized key
     */
    protected String normalizeKey(String c, String l) {
        if (DM4.getValue().equals(l)) {
            return (c + "_lexOn").replace("#", "");
        } else {
            return (c + "_lexOff").replace("#", "");
        }
    }

/**
 * inner terms class contains corresp, ana & target
 */
class Terms {
    private String _corresp;
    private String _ana;
    private List<String> _target;

    Terms(String corresp, String ana, String target) {
        _corresp = corresp;
        _ana = ana;
        _target = Arrays.asList(target.replace("#","").split(" "));
    }

    public String getCorresp() {
        return _corresp;
    }

    public String getAna() {
        return _ana;
    }

    public List<String> getTarget() {
        return _target;
    }
}

class Word {
    private String _target;
    private String _posLemma;

    Word(String target) {
        _target = target;
    }

    void setPosLemma(String posLemma) {
        _posLemma = posLemma;
    }

    public String getTarget() {
        return _target;
    }

    String getPosLemma() {
        return _posLemma;
    }
}
}


