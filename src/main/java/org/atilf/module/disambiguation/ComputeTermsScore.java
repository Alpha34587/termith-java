package org.atilf.module.disambiguation;

import org.atilf.models.disambiguation.ScoreTerm;

/**
 * @author Simon Meoni Created on 15/12/16.
 */
public class ComputeTermsScore implements Runnable {
    private final String _term;
    private final ScoreTerm _scoreTerm;

    public ComputeTermsScore(String term, ScoreTerm scoreTerm) {
        _term = term;
        _scoreTerm = scoreTerm;
    }

    public void execute(){

    }

    @Override
    public void run() {

    }
}
