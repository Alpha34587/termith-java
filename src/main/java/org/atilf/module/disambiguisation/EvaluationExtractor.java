package org.atilf.module.disambiguisation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import java.util.Map;

import static org.atilf.models.disambiguisation.SubLexicResource.*;

/**
 * @author Simon Meoni
 *         Created on 24/10/16.
 */
public class EvaluationExtractor extends SubLexicExtractor{

    private final Map<String, EvaluationProfile> _evaluationLexic;
    private static final Logger LOGGER = LoggerFactory.getLogger(EvaluationExtractor.class.getName());
    public EvaluationExtractor(String p , Map<String, EvaluationProfile> evaluationLexic) {
        super(p,null);
        _evaluationLexic = evaluationLexic;
    }

    @Override
    public void extractTerms() {
        try {
            NodeList nodes = (NodeList) _eSpanTerms.evaluate(_doc, XPathConstants.NODESET);

            for (int i = 0; i < nodes.getLength(); i++) {
                String ana = _eAna.evaluate(nodes.item(i));
                if (!ana.isEmpty()){
                    _target.add(_eTarget.evaluate(nodes.item(i)));
                    _corresp.add(_eCorresp.evaluate(nodes.item(i)));
                    _lexAna.add(_eAna.evaluate(nodes.item(i)));
                }
            }
        } catch (XPathExpressionException e) {
            LOGGER.error("error during the parsing of document",e);
        }
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
