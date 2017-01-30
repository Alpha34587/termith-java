package org.atilf.benchmark;

import org.atilf.models.disambiguation.CorpusLexicon;
import org.atilf.models.disambiguation.LexiconProfile;
import org.atilf.module.disambiguation.contextLexicon.ContextExtractor;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import java.util.HashMap;

/**
 * @author Simon Meoni Created on 28/11/16.
 */
class TermExtractor extends ContextExtractor{

    /**
     * constructor of contextExtractor
     *
     * @param p
     *         the path of the xml file
     * @see LexiconProfile
     */
    TermExtractor(String p) {
        super(
                p,
                new HashMap<>(),
                new CorpusLexicon(new HashMap<>(), new HashMap<>())
        );
    }

    int countTerms() {
        execute();
        return _terms.size();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        switch (qName) {
            case "ns:standOff":
                _inStandOff = true;
                break;
        }
        if (_inStandOff && qName.equals("span")){
            extractTerms(attributes);
        }
    }

    @Override
    public void endElement(String uri,
                           String localName, String qName) throws SAXException {
        switch (qName) {
            case "ns:standOff":
                _inStandOff = false;
                break;
        }
    }

    /**
     * the character event is used to extract the Pos/Lemma pair of a w element
     */
    @Override
    public void characters(char ch[],
                           int start, int length) throws SAXException {
    }
}
