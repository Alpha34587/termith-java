package org.atilf.module.disambiguisation;

import java.util.Map;

/**
 * @author Simon Meoni
 *         Created on 24/10/16.
 */
public class EvaluationExtractor {
    private final String file;
    private final Map<String, EvaluationProfile> evaluationLexic;

    public EvaluationExtractor(String file, Map<String, EvaluationProfile> evaluationLexic) {

        this.file = file;
        this.evaluationLexic = evaluationLexic;
    }

    public void execute() {

    }
}
