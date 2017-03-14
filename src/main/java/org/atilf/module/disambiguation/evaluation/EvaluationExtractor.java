package org.atilf.module.disambiguation.evaluation;

import org.atilf.models.TermithIndex;
import org.atilf.models.disambiguation.ContextTerm;
import org.atilf.models.disambiguation.EvaluationProfile;
import org.atilf.module.disambiguation.contextLexicon.ContextExtractor;
import org.atilf.tools.FilesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;

import java.util.ArrayList;
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

    @Override
    protected void addContextToLexicon(String key, Map<Integer, String> contextTarget) {
        if (!_evaluationLexicon.containsKey(key)) {
            _evaluationLexicon.put(key, new EvaluationProfile());
        }
        _evaluationLexicon.get(key).addOccurrences(new ArrayList<>(contextTarget.values()));

    }

    @Override
    protected String normalizeKey(String c, String l) {
        return (c + "_" + l).replace("#", "");
    }
}
