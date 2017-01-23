package org.atilf.module.disambiguation;

import org.atilf.models.disambiguation.ContextTerm;
import org.atilf.models.disambiguation.EvaluationProfile;
import org.atilf.models.termith.TermithIndex;
import org.atilf.module.tools.FilesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.*;
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
    private CountDownLatch _extractorCounter;

    EvaluationExtractor(String p, TermithIndex termithIndex) {
        super(p,termithIndex.getContextLexicon(), null);
        _p = p;
        termithIndex.getEvaluationLexicon().put(FilesUtils.nameNormalizer(p),new HashMap<>());
        _evaluationLexicon = termithIndex.getEvaluationLexicon().get(FilesUtils.nameNormalizer(p));
    }

    public EvaluationExtractor(String p, TermithIndex termithIndex, CountDownLatch extractorCounter) {
        super(p,termithIndex.getContextLexicon(), null);
        _p = p;
        _extractorCounter = extractorCounter;
        termithIndex.getEvaluationLexicon().put(FilesUtils.nameNormalizer(p),new HashMap<>());
        _evaluationLexicon = termithIndex.getEvaluationLexicon().get(FilesUtils.nameNormalizer(p));
    }


    @Override
    protected void extractTerms(Attributes attributes) {
        String ana = attributes.getValue("ana");
        String corresp = attributes.getValue("corresp");
        if (ana.equals(NO_DM.getValue()) && inBothContextLexicon(corresp)) {
            _terms.add(new ContextTerm(attributes.getValue("corresp"),
                    ana,
                    attributes.getValue("target")));
            LOGGER.debug("term extracted: " + attributes.getValue("corresp"));
        }
        else if (inContextLexicon(corresp)){
            _evaluationLexicon.put(normalizeKey(corresp,ana),new EvaluationProfile());
        }
    }

    private boolean inBothContextLexicon(String corresp) {
        return _contextLexicon.containsKey(corresp.replace("#","") + "_lexOn") &&
                _contextLexicon.containsKey(corresp.replace("#","") + "_lexOff");
    }

    private boolean inContextLexicon(String corresp) {
        return _contextLexicon.containsKey(corresp.replace("#","") + "_lexOn") ||
                _contextLexicon.containsKey(corresp.replace("#","") + "_lexOff");
    }

    @Override
    public void run() {
        LOGGER.info("add " + _p + " to evaluation lexicon");
        this.execute();
        _extractorCounter.countDown();
        LOGGER.info(_p + " added");
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
            _contextStack.forEach(words -> words.put(_lastContextWord.getTarget(),_lastContextWord.getPosLemma()));
            LOGGER.debug("add pos lemma pair: "+ posLemma +" to corpus");
            _inW = false;
        }
    }

    @Override
    void addWordsToLexicon(ContextTerm term, TreeMap<Integer, String> context) {
        /*
        create new entry if the key not exists in the _contextLexicon field
         */
        String key = normalizeKey(term.getCorresp(), term.getAna());
        SortedMap<Integer, String> leftContextTarget = context.subMap(0, true,term.getBeginTag(),true);
        Map<Integer,String> contextTarget = new TreeMap<>(context.subMap(term.getEndTag(), true,context.lastKey(),true));
        contextTarget.putAll(leftContextTarget);

        if (!_targetContext.containsKey(key)) {
            _targetContext.put(key, new ArrayList<>());
        }


        if (!_evaluationLexicon.containsKey(key)) {
            _evaluationLexicon.put(key, new EvaluationProfile());
        }
        _targetContext.get(key).forEach(contextTarget::remove);
        _targetContext.get(key).addAll(new ArrayList<>(contextTarget.keySet()));
        _evaluationLexicon.get(key).addOccurrences(new ArrayList<>(contextTarget.values()));
    }

     @Override
    protected String normalizeKey(String c, String l) {
            return (c + "_" + l).replace("#", "");
    }
}
