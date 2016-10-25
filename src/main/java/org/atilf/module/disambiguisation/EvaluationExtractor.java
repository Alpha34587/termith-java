package org.atilf.module.disambiguisation;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import java.util.Map;

import static org.atilf.models.SubLexicResource.*;

/**
 * @author Simon Meoni
 *         Created on 24/10/16.
 */
public class EvaluationExtractor extends SubLexicExtractor{
    private final Map<String, EvaluationProfile> evaluationLexic;
    private static final Logger LOGGER = LoggerFactory.getLogger(EvaluationExtractor.class.getName());

    public EvaluationExtractor(String p , Map<String, EvaluationProfile> evaluationLexic) {
        super(p,null);
        this.evaluationLexic = evaluationLexic;
    }

    @Override
    public void extractTerms() {
        XPathExpression eSpan;
        XPathExpression eTarget;
        XPathExpression eCorresp;
        XPathExpression eAna;

        try {
            eSpan = xpath.compile(SPAN);
            eTarget = xpath.compile(TARGET);
            eCorresp = xpath.compile(CORRESP);
            eAna = xpath.compile(ANA);

            NodeList nodes = (NodeList) eSpan.evaluate(doc, XPathConstants.NODESET);

            for (int i = 0; i < nodes.getLength(); i++) {
                String ana = eAna.evaluate(nodes.item(i));
                if (!ana.isEmpty()){
                    target.add(eTarget.evaluate(nodes.item(i)));
                    corresp.add(eCorresp.evaluate(nodes.item(i)));
                    lexAna.add(eAna.evaluate(nodes.item(i)));
                }
            }
        } catch (XPathExpressionException e) {
            LOGGER.error("error during the parsing of document",e);
        }
    }

    @Override
    protected void mapToMultiset(Node span, String c, String l){
        try {
            XPathExpression eLemma = xpath.compile(".//tei:f[@name = 'lemma']/tei:string/text()");
            XPathExpression ePos = xpath.compile(".//tei:f[@name = 'pos']/tei:symbol/@value");

            Node lemmaNode = (Node) eLemma.evaluate(span, XPathConstants.NODE);
            Node posNode = (Node) ePos.evaluate(span, XPathConstants.NODE);
            String lemmaValue = lemmaNode.getNodeValue().trim();
            String posValue = posNode.getNodeValue().trim();
            String key = normalizeKey(c,l);
            if (evaluationLexic.containsKey(key)){
                evaluationLexic.get(key).addOccurence(lemmaValue + " " + posValue);
            }
            else{
                Multiset<String> multiset = HashMultiset.create();
                multiset.add(lemmaValue + " " + posValue);
                evaluationLexic.put(key,new EvaluationProfile(multiset));

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
