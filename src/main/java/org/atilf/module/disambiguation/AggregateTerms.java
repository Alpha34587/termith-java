package org.atilf.module.disambiguation;

import org.atilf.models.disambiguation.EvaluationProfile;
import org.atilf.models.disambiguation.ScoreTerm;

import java.util.Map;

/**
 * @author Simon Meoni Created on 15/12/16.
 */
public class AggregateTerms implements Runnable {
    private final String _file;
    private final Map<String, EvaluationProfile> _terms;
    private Map<String,ScoreTerm> _scoreTerm;

    public AggregateTerms(String file, Map<String, EvaluationProfile> terms, Map<String, ScoreTerm> scoreTerms) {
        _file = file;
        _terms = terms;
        _scoreTerm = scoreTerms;
    }

    public void execute(){

    }

    @Override
    public void run() {

    }
}
