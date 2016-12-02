package org.atilf.module.disambiguation;

import org.atilf.models.disambiguation.ContextTerm;
import org.atilf.models.disambiguation.ContextWord;
import org.atilf.models.disambiguation.EvaluationProfile;
import org.atilf.models.termith.TermithIndex;
import org.atilf.module.tools.FilesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import static org.atilf.models.disambiguation.AnnotationResources.NO_DM;

/**
 * @author Simon Meoni
 *         Created on 24/10/16.
 */
public class EvaluationExtractor extends ContextExtractor {

    private final Map<String, EvaluationProfile> _evaluationLexicon;
    private static final Logger LOGGER = LoggerFactory.getLogger(EvaluationExtractor.class.getName());
    private String _p;
    private CountDownLatch _extactorCounter;

    public EvaluationExtractor(String p, TermithIndex termithIndex) {
        super(p,termithIndex.getContextLexicon(), null);
        _p = p;
        termithIndex.getEvaluationLexicon().put(FilesUtils.nameNormalizer(p),new HashMap<>());
        _evaluationLexicon = termithIndex.getEvaluationLexicon().get(FilesUtils.nameNormalizer(p));
    }

    public EvaluationExtractor(String p, TermithIndex termithIndex, CountDownLatch extactorCounter) {
        super(p,termithIndex.getContextLexicon(), null);
        _p = p;
        _extactorCounter = extactorCounter;
        termithIndex.getEvaluationLexicon().put(FilesUtils.nameNormalizer(p),new HashMap<>());
        _evaluationLexicon = termithIndex.getEvaluationLexicon().get(FilesUtils.nameNormalizer(p));
    }


    @Override
    protected void extractTerms(Attributes attributes) {
        String ana = attributes.getValue("ana");
        String corresp = attributes.getValue("corresp");
        if (ana.equals(NO_DM.getValue()) && InContextLexicon(corresp)) {
            _terms.add(new ContextTerm(attributes.getValue("corresp"),
                    ana,
                    attributes.getValue("target")));
            LOGGER.debug("term extracted: " + attributes.getValue("corresp"));
        }
    }

    protected void addWordToLexicon(String key, ContextWord contextWord){
        if (!_evaluationLexicon.containsKey(key)){
            _evaluationLexicon.put(key,new EvaluationProfile());
        }
        _evaluationLexicon.get(key).addOccurrence(contextWord.getPosLemma());
    }

    private boolean InContextLexicon(String corresp) {
        return _contextLexicon.containsKey(corresp.replace("#","") + "_lexOn") ||
                _contextLexicon.containsKey(corresp.replace("#","") + "_lexOff");
    }

    @Override
    public void run() {
        LOGGER.info("add " + _p + " to evaluation lexicon");
        this.execute();
        _extactorCounter.countDown();
        LOGGER.info(_p + " added");
    }

    /**
     * the character event is used to extract the Pos/Lemma pair of a w element
     */
    @Override
    public void characters(char ch[],
                           int start, int length) throws SAXException {
        if (_inW){
            String word = new String(ch,start,length);
            _lastContextWord.setPosLemma(word);
            _inW = false;
        }
    }

    @Override
    protected String normalizeKey(String c, String l) {
            return (c + "_" + l).replace("#", "");
    }
}
