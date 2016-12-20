package org.atilf.module.disambiguation;

import org.atilf.models.disambiguation.ScoreTerm;
import org.atilf.models.disambiguation.TotalTermScore;

import java.util.Map;

/**
 * @author Simon Meoni Created on 15/12/16.
 */
public class ComputeTotalTermsScore implements Runnable {

    private Map<String, ScoreTerm> _scoreTerms;
    private int _totalOccurrences = 0;
    private int _totalMissingOccurrences = 0;
    private int _totalCorrectOccurrences = 0;
    private TotalTermScore _totalTermScore;

    public ComputeTotalTermsScore(Map<String, ScoreTerm> scoreTerms, TotalTermScore totalTermScore) {
        _scoreTerms = scoreTerms;
        _totalTermScore = totalTermScore;
    }

    public void execute(){
        sum();
        computeRecall();
        computePrecision();
        computeF1score();
    }

    public void sum() {
        _scoreTerms.forEach(
                (key,value) -> {
                    _totalOccurrences += value.getTotalOccurrences();
                    _totalMissingOccurrences += value.getMissingOccurrence();
                    _totalCorrectOccurrences += value.getCorrectOccurrence();
                }
        );
    }

    protected void computeRecall(){
        _totalTermScore.setRecall(1 - ((float) _totalMissingOccurrences / (float) _totalOccurrences));
    }

    protected void computePrecision(){
        _totalTermScore.setPrecision((float) _totalCorrectOccurrences / (float) _totalOccurrences);
    }

    protected void computeF1score(){
        float precisionFloat = _totalTermScore.getPrecision();
        float recallFloat = _totalTermScore.getRecall();
        _totalTermScore.setF1score((precisionFloat * recallFloat) / (precisionFloat + recallFloat));
    }
    @Override
    public void run() {

    }
}
