package org.atilf.module.disambiguation;

import org.atilf.models.disambiguation.ScoreTerm;
import org.atilf.models.disambiguation.TotalTermScore;

import java.util.Map;

/**
 * @author Simon Meoni Created on 03/01/17.
 */
public class ExportScoreToCsv implements Runnable {
    private final Map<String, ScoreTerm> _scoreTerms;
    private final TotalTermScore _totalTermScore;

    public ExportScoreToCsv(Map<String, ScoreTerm> scoreTerms, TotalTermScore totalTermScore) {
        _scoreTerms = scoreTerms;
        _totalTermScore = totalTermScore;
    }

    @Override
    public void run() {
        execute();
    }

    protected void execute() {

    }
}
