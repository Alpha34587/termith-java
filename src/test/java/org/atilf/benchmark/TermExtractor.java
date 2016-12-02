package org.atilf.benchmark;

import org.atilf.models.disambiguation.CorpusLexicon;
import org.atilf.models.disambiguation.LexiconProfile;
import org.atilf.module.disambiguation.ContextExtractor;

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
}
