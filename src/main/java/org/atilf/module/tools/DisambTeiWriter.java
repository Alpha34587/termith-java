package org.atilf.module.tools;

import org.atilf.module.disambiguisation.EvaluationProfile;

import java.nio.file.Path;
import java.util.Map;

/**
 * @author Simon Meoni
 *         Created on 25/10/16.
 */
public class DisambTeiWriter {
    private final Path p;
    private final Map<String,Map<String, EvaluationProfile>> evaluationLexic;

    public DisambTeiWriter(Path p, Map<String,Map<String, EvaluationProfile>> evaluationLexic) {
        this.p = p;
        this.evaluationLexic = evaluationLexic;
    }

    public void execute() {

    }
}
