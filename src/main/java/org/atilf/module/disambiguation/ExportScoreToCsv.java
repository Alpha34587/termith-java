package org.atilf.module.disambiguation;

import org.atilf.models.disambiguation.ScoreTerm;
import org.atilf.models.disambiguation.TotalTermScore;

import java.io.IOException;
import java.util.Map;

/**
 * @author Simon Meoni Created on 03/01/17.
 */
public class ExportScoreToCsv extends ExportScoreToJson {

    public ExportScoreToCsv(Map<String, ScoreTerm> scoreTerms, TotalTermScore totalTermScore) {
        super(scoreTerms,totalTermScore);
    }

    @Override
    public void run() {
        try {
            execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
