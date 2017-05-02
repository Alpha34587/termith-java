package org.atilf.module.disambiguation.evaluationScore;

import org.atilf.models.TermithIndex;
import org.atilf.models.disambiguation.ScoreTerm;
import org.atilf.module.Module;

/**
 * @author Simon Meoni Created on 15/12/16.
 */
public class ComputeTermsScore extends Module {
    private final String _term;
    private final ScoreTerm _scoreTerm;

    public ComputeTermsScore(String term, ScoreTerm scoreTerm) {
        _term = term;
        _scoreTerm = scoreTerm;
    }

    public ComputeTermsScore(String p, TermithIndex termithIndex) {
        super(termithIndex);
        _term = p;
        _scoreTerm = _termithIndex.getScoreTerms().get(p);

    }

    @Override
    public void execute(){
        _logger.info("compute score is started for : %s",_term );
        computeRecall();
        computePrecision();
        computeF1score();
        computeTerminologyTrend();
        computeAmbiguityRate();
        _logger.info("compute score is finished for : %s",_term );
    }

    void computeRecall(){
        _scoreTerm.setRecall(1- ((float) _scoreTerm.getMissingOccurrence() / (float) _scoreTerm.getTotalOccurrences()));
    }

    void computePrecision(){
        _scoreTerm.setPrecision((float) _scoreTerm.getCorrectOccurrence() / (float) _scoreTerm.getTotalOccurrences());
    }

    void computeF1score(){
        float precisionFloat = _scoreTerm.getPrecision();
        float recallFloat = _scoreTerm.getRecall();
        float f1Score = 2*((precisionFloat * recallFloat) / (precisionFloat + recallFloat));

        if (Float.isNaN(f1Score)) {
            _scoreTerm.setF1Score(0);
        }
        else {
            _scoreTerm.setF1Score(f1Score);
        }
    }

    void computeAmbiguityRate(){
        if (_scoreTerm.getTerminologyTrend() <= 0.5f){
            _scoreTerm.setAmbiguityRate(_scoreTerm.getTerminologyTrend());
        }
        else {
            _scoreTerm.setAmbiguityRate((1f - _scoreTerm.getTerminologyTrend()));
        }
    }

    void computeTerminologyTrend(){
        _scoreTerm.setTerminologyTrend(_scoreTerm.getValidatedOccurrence() / _scoreTerm.getTotalOccurrences());
    }
}
