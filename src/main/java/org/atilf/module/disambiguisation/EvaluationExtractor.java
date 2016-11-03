package org.atilf.module.disambiguisation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.util.Map;

/**
 * @author Simon Meoni
 *         Created on 24/10/16.
 */
public class EvaluationExtractor extends ContextExtractor {

    private final Map<String, EvaluationProfile> _evaluationLexic;
    private static final Logger LOGGER = LoggerFactory.getLogger(EvaluationExtractor.class.getName());

    public EvaluationExtractor(String p , Map<String, EvaluationProfile> evaluationLexic) {
        super(p,null);
        _evaluationLexic = evaluationLexic;
    }

    public EvaluationExtractor(String p , Map<String, EvaluationProfile> evaluationLexic, Map<String,LexicalProfile> specLexic) {
        super(p,specLexic);
        _evaluationLexic = evaluationLexic;
    }

    @Override
    public void extractTerms() {
        try {
            NodeList nodes = (NodeList) _eSpanTerms.evaluate(_doc, XPathConstants.NODESET);
            if (_subLexics == null){
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
            if (!ana.isEmpty() && containInSpecCoeffLexic(corresp)){
                _target.add(_eTarget.evaluate(nodes.item(i)));
                _corresp.add(corresp);
                _lexAna.add(ana);
            }
        }
    }


    private boolean containInSpecCoeffLexic(String corresp){
        return !(_subLexics.containsKey(corresp.substring(1) + "_lexOff") ||
                _subLexics.containsKey(corresp.substring(1) + "_lexOn"));
    }

    @Override
    protected void addOccToLexicalProfile(String spanValue, String c, String l){
            String key = normalizeKey(c,l);
            if (!_evaluationLexic.containsKey(key)){
                _evaluationLexic.put(key,new EvaluationProfile());
            }
            _evaluationLexic.get(key).addOccurrence(spanValue);

    }

    @Override
    protected String normalizeKey(String c, String l) {
        return c.substring(1,c.length()) + "_" + l.subSequence(1,l.length());
    }
}
