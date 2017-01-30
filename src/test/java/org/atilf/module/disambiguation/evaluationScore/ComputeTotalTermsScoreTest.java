package org.atilf.module.disambiguation.evaluationScore;

import org.atilf.models.disambiguation.ScoreTerm;
import org.atilf.models.disambiguation.TotalTermScore;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Simon Meoni Created on 20/12/16.
 */
public class ComputeTotalTermsScoreTest {

    private static Map<String,ScoreTerm> _scoreTermMap = new HashMap<>();
    private static TotalTermScore _totalTermScore = new TotalTermScore();
    private static ComputeTotalTermsScore _computeTotalTermsScore = new ComputeTotalTermsScore(_scoreTermMap,_totalTermScore);

    @BeforeClass
    public static void setUp() throws Exception {
        ScoreTerm _scoreTerm1 = new ScoreTerm();
        _scoreTerm1.setMissingOccurrence(2);
        _scoreTerm1.setCorrectOccurrence(2);
        _scoreTerm1.setTotalOccurrences(4);
        _scoreTermMap.put("term1",_scoreTerm1);
        _scoreTermMap.put("term2",_scoreTerm1);
        _computeTotalTermsScore.sum();
    }

    @Test
    public void computeRecall() throws Exception {
        _computeTotalTermsScore.computeRecall();
        Assert.assertEquals("the recall must be equals to : ",
                0.5f,
                _totalTermScore.getRecall(),
                0f);
    }

    @Test
    public void computePrecision() throws Exception {
        _computeTotalTermsScore.computePrecision();
        Assert.assertEquals("the precision must be equals to : ",
                0.5f,
                _totalTermScore.getPrecision(),
                0f);
    }

    @Test
    public void computeF1score() throws Exception {
        _totalTermScore.setRecall(0.5f);
        _totalTermScore.setPrecision(0.5f);
        _computeTotalTermsScore.computeF1score();
        Assert.assertEquals("the f1 score must be equals to : ",
                0.5f,
                _totalTermScore.getF1score(),
                0f);
    }

}