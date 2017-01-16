package org.atilf.module.disambiguation;

import org.atilf.models.disambiguation.ScoreTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CountDownLatch;

/**
 * @author Simon Meoni Created on 15/12/16.
 */
public class ComputeTermsScore implements Runnable {
    private final String _term;
    private final ScoreTerm _scoreTerm;
    private CountDownLatch _scoreCounter;
    private static final Logger LOGGER = LoggerFactory.getLogger(ComputeTermsScore.class.getName());

    public ComputeTermsScore(String term, ScoreTerm scoreTerm) {
        _term = term;
        _scoreTerm = scoreTerm;
    }

    public ComputeTermsScore(String p, ScoreTerm value, CountDownLatch scoreCounter) {
        this(p,value);
        _scoreCounter = scoreCounter;

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
        LOGGER.info("compute score is started for : " + _term );
        execute();
        _scoreCounter.countDown();
        LOGGER.info("compute score is finished for : " + _term );
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
