package org.atilf.module.disambiguation;

import org.atilf.models.disambiguation.ScoreTerm;
import org.atilf.models.disambiguation.TotalTermScore;
import org.atilf.models.termith.TermithIndex;
import org.atilf.module.Module;

import java.util.Map;

/**
 * @author Simon Meoni Created on 15/12/16.
 */
public class ComputeTotalTermsScore extends Module {

    private Map<String, ScoreTerm> _scoreTerms;
    private int _totalOccurrences = 0;
    private int _totalMissingOccurrences = 0;
    private int _totalCorrectOccurrences = 0;
    private TotalTermScore _totalTermScore;

    ComputeTotalTermsScore(Map<String, ScoreTerm> scoreTerms, TotalTermScore totalTermScore) {
        _scoreTerms = scoreTerms;
        _totalTermScore = totalTermScore;
    }

    public ComputeTotalTermsScore(TermithIndex termithIndex) {
        super(termithIndex);
        _scoreTerms = termithIndex.getScoreTerms();
        _totalTermScore = termithIndex.getTotalTermScore();
    }

    public void execute() {
        _logger.info("ComputeTotalTermScore is started");
        sum();
        computeRecall();
        computePrecision();
        computeF1score();
        _logger.info("ComputeTotalTermScore is finished");
    }

    void sum() {
        _scoreTerms.forEach(
                (key, value) -> {
                    _totalOccurrences += value.getTotalOccurrences();
                    _totalMissingOccurrences += value.getMissingOccurrence();
                    _totalCorrectOccurrences += value.getCorrectOccurrence();
                }
        );
    }

    void computeRecall() {
        _totalTermScore.setRecall(1 - ((float) _totalMissingOccurrences / (float) _totalOccurrences));
    }

    void computePrecision() {
        _totalTermScore.setPrecision((float) _totalCorrectOccurrences / (float) _totalOccurrences);
    }

    void computeF1score() {
        float precisionFloat = _totalTermScore.getPrecision();
        float recallFloat = _totalTermScore.getRecall();
        _totalTermScore.setF1score(2 * ((precisionFloat * recallFloat) / (precisionFloat + recallFloat)));
    }
}