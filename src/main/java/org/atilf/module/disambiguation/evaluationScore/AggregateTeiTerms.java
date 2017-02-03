package org.atilf.module.disambiguation.evaluationScore;

import org.atilf.models.disambiguation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;

/**
 * @author Simon Meoni Created on 15/12/16.
 */
public class AggregateTeiTerms extends DefaultHandler implements Runnable {
    private boolean _inStandOff = false;
    private boolean _inW = false;
    private Map<String, ScoreTerm> _scoreTerm;
    private ContextWord _currentW;
    private Deque<ContextTerm> _terms = new ArrayDeque<>();
    private List<Entry<ContextTerm,Set<ContextWord>>> _termTemp = new LinkedList<>();
    private CountDownLatch _aggregateCounter;
    private final String _xml;
    private final Map<String, EvaluationProfile> _evaluationProfile;
    private static final Logger LOGGER = LoggerFactory.getLogger(AggregateTeiTerms.class.getName());

    AggregateTeiTerms(String xml, Map<String, EvaluationProfile> evaluationProfile,
                      Map<String, ScoreTerm> scoreTerms) {
        _xml = xml;
        _evaluationProfile = evaluationProfile;
        _scoreTerm = scoreTerms;
    }

    public AggregateTeiTerms(String xml, Map<String, EvaluationProfile> evaluationProfile,
                             Map<String, ScoreTerm> scoreTerms, CountDownLatch aggregateCounter) {
        this(xml, evaluationProfile, scoreTerms);
        _aggregateCounter = aggregateCounter;
    }

    public void execute() {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(_xml, this);
        } catch (Exception e) {
            LOGGER.error("cannot parse this xml file :" + _xml, e);
        }
    }

    @Override
    public void run() {
        LOGGER.info("aggregate terms is started for file : " + _xml);
        execute();
        _aggregateCounter.countDown();
        LOGGER.info("aggregate terms is finished for file : " + _xml);
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
        AnnotationResources ana = parseAna(attributes.getValue("ana"));
        String corresp = attributes.getValue("corresp").substring(1);
        String key =  corresp + "_" + ana;
        String target = attributes.getValue("target");
        assert ana != null;
        if (!ana.equals(AnnotationResources.NO_DM)) {
            _scoreTerm.putIfAbsent(corresp, new ScoreTerm());
            ScoreTerm scoreTerm = _scoreTerm.get(corresp);
            scoreTerm.setFlexionsWords(attributes.getValue("text"));
            _terms.add(new ContextTerm(corresp, target));

            if ((term = _evaluationProfile.get(key)) != null) {
                verifyAnnotation(scoreTerm, ana, term.getDisambiguationId());
            } else {
                scoreTerm.incMissingOccurrence();
            }
            if (ana.equals(AnnotationResources.DM4)) {
                scoreTerm.incValidatedOccurrence();
            }
            scoreTerm.incTotalOccurrence();
        }
    }

    private AnnotationResources parseAna(String ana) {
        for (String id : ana.split(" ")) {
            AnnotationResources annotation = AnnotationResources.getAnnotations(id);
            if(annotation != null)
                if (annotation.getAutoAnnotation() != null)
                    return annotation;
        }
        return null;
    }

    private void verifyAnnotation(ScoreTerm scoreTerm, AnnotationResources ana, String disambiguationId) {
        if (disambiguationId.equals(ana.getAutoAnnotation().getValue())){
            scoreTerm.incCorrectOccurrence();
        }
    }

    @Override
    public void endElement(String uri,
                           String localName, String qName) throws SAXException {
        if(_inW){
            addPosLemmaToTerm();
        }

        switch (qName) {
            case "ns:standOff":
                _inStandOff = false;
                LOGGER.info("term extraction finished");
                break;
            case "text":
                LOGGER.info("context extraction finished");
                break;
            case "w":
                _inW = false;
                break;
        }
    }

    private void addPosLemmaToTerm() {
        ContextTerm term;
        while (!_terms.isEmpty() && (term = _terms.peek()).getBeginTag() <= _currentW.getTarget()) {
            if (term.getBeginTag() == term.getEndTag()) {
                _scoreTerm.get(term.getCorresp()).addTermWords(Collections.singletonList(_currentW));
            } else {
                _termTemp.add(new AbstractMap.SimpleEntry(term, new LinkedHashSet<>()));
            }
            _terms.poll();
        }
        Iterator<Entry<ContextTerm, Set<ContextWord>>> iter = _termTemp.iterator();
        while (iter.hasNext()) {
            Entry<ContextTerm, Set<ContextWord>> el = iter.next();
            el.getValue().add(_currentW);
            if (!_termTemp.isEmpty() && el.getKey().getEndTag() == _currentW.getTarget()){
                _scoreTerm.get(el.getKey().getCorresp()).addTermWords(new ArrayList<>(el.getValue()));
                iter.remove();
            }
        }
    }

    @Override
    public void characters(char ch[],
                           int start, int length) throws SAXException {
        if (_inW){
            String posLemma = new String(ch,start,length);
            _currentW.setPosLemma(posLemma);
        }
    }
}