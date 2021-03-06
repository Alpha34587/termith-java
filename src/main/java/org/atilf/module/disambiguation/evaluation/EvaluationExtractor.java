package org.atilf.module.disambiguation.evaluation;

import org.atilf.models.TermithIndex;
import org.atilf.models.disambiguation.ContextTerm;
import org.atilf.models.disambiguation.EvaluationProfile;
import org.atilf.module.disambiguation.contextLexicon.ContextExtractor;
import org.atilf.module.tools.FilesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Simon Meoni
 *         Created on 24/10/16.
 */
public class EvaluationExtractor extends ContextExtractor {

    private final Map<String, EvaluationProfile> _evaluationLexicon;
    private static final Logger LOGGER = LoggerFactory.getLogger(EvaluationExtractor.class.getName());

    public EvaluationExtractor(String p, TermithIndex termithIndex) {
        super(p,termithIndex.getContextLexicon(), null);
        termithIndex.getEvaluationLexicon().put(FilesUtils.nameNormalizer(p),new HashMap<>());
        _evaluationLexicon = termithIndex.getEvaluationLexicon().get(FilesUtils.nameNormalizer(p));
    }

    @Override
    protected void extractTerms(Attributes attributes) {
        String ana = attributes.getValue("ana");
        _terms.add(new ContextTerm(attributes.getValue("corresp"),
                ana,
                attributes.getValue("target")));
        LOGGER.debug("term extracted: " + attributes.getValue("corresp"));
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
        LOGGER.info("add {} to evaluation lexicon",_p);
        this.execute();
        LOGGER.info("{} added",_p);
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
