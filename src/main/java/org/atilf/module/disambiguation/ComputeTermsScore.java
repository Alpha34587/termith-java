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
        computeRecall();
        computePrecision();
        computeF1score();
        computeTerminologyTrend();
        computeAmbiguityRate();
    }

    @Override
    public void run() {
    }

    protected void computeRecall(){
         _scoreTerm.setRecall(1- ((float) _scoreTerm.getMissingOccurrence() / (float) _scoreTerm.getTotalOccurrences()));
    }

    protected void computePrecision(){
        _scoreTerm.setPrecision((float) _scoreTerm.getCorrectOccurrence() / (float) _scoreTerm.getTotalOccurrences());
    }

    protected void computeF1score(){
         float precisionFloat = _scoreTerm.getPrecision();
         float recallFloat = _scoreTerm.getRecall();
        _scoreTerm.setF1Score(2*((precisionFloat * recallFloat) / (precisionFloat + recallFloat)));
    }

    protected void computeAmbiguityRate(){
        if (_scoreTerm.getTerminologyTrend() <= 0.5){
            _scoreTerm.setAmbiguityRate(_scoreTerm.getTerminologyTrend());
        }
        _scoreTerm.setAmbiguityRate((1f - _scoreTerm.getTerminologyTrend()));
    }

    protected void computeTerminologyTrend(){
        _scoreTerm.setTerminologyTrend(_scoreTerm.getValidatedOccurrence() / _scoreTerm.getTotalOccurrences());
    }
}
