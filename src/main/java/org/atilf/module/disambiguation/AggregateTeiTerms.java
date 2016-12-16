package org.atilf.module.disambiguation;

import org.atilf.models.disambiguation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.*;

/**
 * @author Simon Meoni Created on 15/12/16.
 */
public class AggregateTeiTerms extends DefaultHandler implements Runnable {
    private ContextWord _currentW;
    private List<ContextWord> _wElements = new ArrayList<>();
    private Deque<ContextTerm> _terms = new ArrayDeque<>();
    private final String _xml;
    private final Map<String, EvaluationProfile> _evaluationProfile;
    private Map<String, ScoreTerm> _scoreTerm;
    private boolean _inStandOff = false;
    private boolean _inW = false;
    private boolean _inText = false;
    private static final Logger LOGGER = LoggerFactory.getLogger(AggregateTeiTerms.class.getName());

    public AggregateTeiTerms(String xml, Map<String, EvaluationProfile> evaluationProfile, Map<String, ScoreTerm> scoreTerms) {
        _xml = xml;
        _evaluationProfile = evaluationProfile;
        _scoreTerm = scoreTerms;
    }

    public void execute() {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(_xml, this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        switch (qName) {
            case "ns:standOff":
                _inStandOff = true;
                LOGGER.info("term extraction started");
                break;
            case "text":
                _inText = true;
                LOGGER.info("context extraction started");
                break;
            case "w":
                _inW = true;
                break;
        }
        if (_inStandOff && qName.equals("span")) {
            addTerms(attributes);
        }

        if (_inW){
            _currentW = new ContextWord(attributes.getValue("xml:id"));
        }
    }

    private void addTerms(Attributes attributes) {
        EvaluationProfile term;
        String ana = attributes.getValue("ana").substring(1);
        String corresp = attributes.getValue("corresp").substring(1);
        String key =  corresp + "_" + ana;
        String target = attributes.getValue("target");
        _scoreTerm.putIfAbsent(corresp,new ScoreTerm());
        ScoreTerm scoreTerm = _scoreTerm.get(corresp);
        _terms.add(new ContextTerm(corresp,target));
        if ( (term = _evaluationProfile.get(key)) != null){
            verifyAnnotation(scoreTerm,ana,term.getDisambiguationId());
        }
        else {
            scoreTerm.incMissingOccurence();
        }
        scoreTerm.incTotalOccurence();
    }

    private void verifyAnnotation(ScoreTerm scoreTerm, String ana, String disambiguationId) {
        if (AnnotationResources.valueOf(ana) == AnnotationResources.valueOf(disambiguationId).getAutoAnnotation()){
            scoreTerm.incCorrectOccurence();
        }
    }

    @Override
    public void endElement(String uri,
                           String localName, String qName) throws SAXException {
        if(_inW){
            _wElements.add(_currentW);
        }

        switch (qName) {
            case "ns:standOff":
                _inStandOff = false;
                LOGGER.info("term extraction finished");
                break;
            case "text":
                _inText = false;
                LOGGER.info("context extraction finished");
                break;
            case "w":
                _inW = false;
                break;
        }
    }

    @Override
    public void characters(char ch[],
                           int start, int length) throws SAXException {
        if (_inW){
            String posLemma = new String(ch,start,length);
            _currentW.setPosLemma(posLemma);
            LOGGER.debug("add pos lemma pair: "+ posLemma +" to corpus");
        }
    }
}
