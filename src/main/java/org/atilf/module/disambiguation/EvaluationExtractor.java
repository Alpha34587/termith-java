package org.atilf.module.disambiguation;

import org.atilf.models.termith.TermithIndex;
import org.atilf.module.tools.FilesUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Simon Meoni
 *         Created on 24/10/16.
 */
public class EvaluationExtractor extends ContextExtractor {

    private final Map<String, EvaluationProfile> _evaluationLexicon;
    private static final Logger LOGGER = LoggerFactory.getLogger(EvaluationExtractor.class.getName());
    private String _p;

    public EvaluationExtractor(String p , Map<String, EvaluationProfile> evaluationLexicon) {
        super(p,null);
        _evaluationLexicon = evaluationLexicon;
    }

    public EvaluationExtractor(String p , Map<String, EvaluationProfile> evaluationLexicon, Map<String,LexicalProfile> specLexicon) {
        super(p,specLexicon);
        _evaluationLexicon = evaluationLexicon;
    }

    public EvaluationExtractor(String p , TermithIndex termithIndex) {
        super(p,termithIndex.getContextLexicon());
        _p = p;
        _evaluationLexicon = termithIndex.getEvaluationLexicon().put(FilesUtils.nameNormalizer(p),new HashMap<>());
    }

    @Override
    public void extractTerms() {
        try {
            NodeList nodes = (NodeList) _eSpanTerms.evaluate(_doc, XPathConstants.NODESET);
            if (_contextLexicon == null){
                extractWithNoLProfile(nodes);
            }
            else {
                extractWithLProfile(nodes);
            }
        } catch (XPathExpressionException e) {
            LOGGER.error("error during the parsing of document",e);
        }
    }

    private void extractWithNoLProfile(NodeList nodes) throws XPathExpressionException {
        for (int i = 0; i < nodes.getLength(); i++) {
            String ana = _eAna.evaluate(nodes.item(i));
            if (!ana.isEmpty()){
                addToTermsQueues(nodes.item(i), ana);
            }
        }
    }

    private void extractWithLProfile(NodeList nodes) throws XPathExpressionException {
        for (int i = 0; i < nodes.getLength(); i++) {
            String ana = _eAna.evaluate(nodes.item(i));
            String corresp = _eCorresp.evaluate(nodes.item(i));
            if (!ana.isEmpty() && containInSpecLexicon(corresp)){
                _target.add(_eTarget.evaluate(nodes.item(i)));
                _corresp.add(corresp);
                _lexAna.add(ana);
            }
        }
    }


    private boolean containInSpecLexicon(String corresp){
        return !(_contextLexicon.containsKey(corresp.substring(1) + "_lexOff") ||
                _contextLexicon.containsKey(corresp.substring(1) + "_lexOn"));
    }

    @Override
    protected void addOccToLexicalProfile(String spanValue, String c, String l){
            String key = normalizeKey(c,l);
            if (!_evaluationLexicon.containsKey(key)){
                _evaluationLexicon.put(key,new EvaluationProfile());
            }
            _evaluationLexicon.get(key).addOccurrence(spanValue);

    }

    @Override
    protected String normalizeKey(String c, String l) {
        return c.substring(1,c.length()) + "_" + l.subSequence(1,l.length());
    }

    @Override
    public void run() {
        LOGGER.debug("add " + _p + " to evaluation lexicon");
        this.execute();
        LOGGER.debug(_p + " added");
    }
}
