package org.atilf.module.disambiguisation;

import java.util.Map;

/**
 * @author Simon Meoni
 *         Created on 24/10/16.
 */
public class EvaluationInjector {
    private final String term;
    private final Map<String,Map<String, EvaluationProfile>> evaluationLexic;
    private final Map<String, LexicalProfile> termSubLexic;

    public EvaluationInjector(String term,
                              Map<String,Map<String, EvaluationProfile>> evaluationLexic,
                              Map<String, LexicalProfile> termSubLexic) {
        this.term = term;
        this.evaluationLexic = evaluationLexic;
        this.termSubLexic = termSubLexic;
    }

    public void execute() {

    }
}
