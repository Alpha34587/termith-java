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
    private XPathExpression _eSpan;
    private XPathExpression _eTarget;
    private XPathExpression _eCorresp;
    private XPathExpression _eAna;
    private XPathExpression _ePos;
    private XPathExpression _eLemma;
    public EvaluationExtractor(String p , Map<String, EvaluationProfile> evaluationLexic) {

        super(p,null);
        _evaluationLexic = evaluationLexic;
        try {
            _eSpan = _xpath.compile(SPAN);
            _eTarget = _xpath.compile(TARGET);
            _eCorresp = _xpath.compile(CORRESP);
            _eAna = _xpath.compile(ANA);
            _eLemma = _xpath.compile(".//tei:f[@name = 'lemma']/tei:string/text()");
            _ePos = _xpath.compile(".//tei:f[@name = 'pos']/tei:symbol/@value");
        } catch (XPathExpressionException e) {
            LOGGER.error("cannot compile _xpath expression :", e);
        }
    }

    @Override
    public void extractTerms() {
        try {
            NodeList nodes = (NodeList) _eSpan.evaluate(_doc, XPathConstants.NODESET);

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
    protected String normalizeKey(String c, String l) {
        return c.substring(1,c.length()) + "_" + l.subSequence(1,l.length());
    }
}
