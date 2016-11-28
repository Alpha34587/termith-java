package org.atilf.benchmark;

import org.atilf.models.disambiguation.LexiconProfile;
import org.atilf.module.disambiguation.ContextExtractor;

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
        super(p, null);
    }

    int countTerms() {
        extractTerms();
        return _target.size();
    }
}
