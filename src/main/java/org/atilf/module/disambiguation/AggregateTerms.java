package org.atilf.module.disambiguation;

import org.atilf.models.disambiguation.EvaluationProfile;

import java.util.Map;

/**
 * @author Simon Meoni Created on 15/12/16.
 */
public class AggregateTerms implements Runnable {
    private final String _file;
    private final Map<String, EvaluationProfile> _terms;

    public AggregateTerms(String file, Map<String, EvaluationProfile> terms) {
        _file = file;
        _terms = terms;
    }

    public void execute(){

    }

    @Override
    public void run() {

    }
}
