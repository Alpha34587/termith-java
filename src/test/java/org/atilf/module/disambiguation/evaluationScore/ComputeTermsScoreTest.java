package org.atilf.module.disambiguation.evaluationScore;

import org.atilf.models.disambiguation.ScoreTerm;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Simon Meoni Created on 19/12/16.
 */
public class ComputeTermsScoreTest {
    private static ScoreTerm _observedScore = new ScoreTerm();
    private static ScoreTerm _expectedScore = new ScoreTerm();
    private static ComputeTermsScore _computeTermsScore = new ComputeTermsScore("test",_observedScore);
    @BeforeClass
    public static void setUp() throws Exception {
        _observedScore.setMissingOccurrence(1);
        _observedScore.setTotalOccurrences(4);
        _observedScore.setCorrectOccurrence(3);
        _observedScore.setAmbiguityRate(1);
        _expectedScore.setRecall(0.75f);
        _expectedScore.setPrecision(0.75f);
        _expectedScore.setF1Score(0.75f);
        _expectedScore.setTerminologyTrend(0.75f);
        _expectedScore.setAmbiguityRate(0.25f);
    }

    @Test
    public void computeRecall() throws Exception {
        _computeTermsScore.computeRecall();
        Assert.assertEquals("the recall score must be equal to : " + _expectedScore.getRecall(),
                _expectedScore.getRecall(),
                _observedScore.getRecall(),
                0f
                );
    }

    @Test
    public void computePrecision() throws Exception {
        _computeTermsScore.computePrecision();
        Assert.assertEquals("the precision score must be equal to : " + _expectedScore.getPrecision(),
                _expectedScore.getPrecision(),
                _observedScore.getPrecision(),
                0f
        );
    }

    @Test
    public void computeF1score() throws Exception {
        _observedScore.setPrecision(0.75f);
        _observedScore.setRecall(0.75f);
        _computeTermsScore.computeF1score();
        Assert.assertEquals("the f1 score must be equal to : " + _expectedScore.getF1Score(),
                _expectedScore.getF1Score(),
                _observedScore.getF1Score(),
                0f
        );
    }

    @Test
    public void computeAmbiguityRate() throws Exception {
        _observedScore.setTerminologyTrend(0.75f);
        _computeTermsScore.computeAmbiguityRate();
        Assert.assertEquals("the ambiguity rate must be equal to : " + _expectedScore.getAmbiguityRate(),
                _expectedScore.getAmbiguityRate(),
                _observedScore.getAmbiguityRate(),
                0f
        );
    }

    @Test
    public void computeTerminologyTrend() throws Exception {
        _observedScore.setValidatedOccurrence(3);
        _computeTermsScore.computeTerminologyTrend();
        Assert.assertEquals("the ambiguity rate must be equal to : " + _expectedScore.getTerminologyTrend(),
                _expectedScore.getTerminologyTrend(),
                _observedScore.getTerminologyTrend(),
                0f
        );
    }

}