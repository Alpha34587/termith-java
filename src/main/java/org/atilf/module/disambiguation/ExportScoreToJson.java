package org.atilf.module.disambiguation;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.atilf.models.disambiguation.ContextWord;
import org.atilf.models.disambiguation.ScoreTerm;
import org.atilf.models.disambiguation.TotalTermScore;
import org.atilf.models.termith.TermithIndex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * @author Simon Meoni Created on 03/01/17.
 */
public class ExportScoreToJson implements Runnable {
    final Map<String, ScoreTerm> _scoreTerms;
    private final TotalTermScore _totalTermScore;
    private static final Logger LOGGER = LoggerFactory.getLogger(ExportScoreToCsv.class.getName());

    public ExportScoreToJson(Map<String, ScoreTerm> scoreTerms, TotalTermScore totalTermScore) {
        _scoreTerms = scoreTerms;
        _totalTermScore = totalTermScore;
    }

    @Override
    public void run() {
        LOGGER.info("exportation to json is started");
        try {
            execute();
        } catch (IOException e) {
            LOGGER.error("cannot export in JSON",e);
        }
        LOGGER.info("exportation to json is finished");
    }

    protected void execute() throws IOException {
        JsonFactory f = new JsonFactory();
        JsonGenerator g = f.createGenerator(new FileWriter(TermithIndex.getOutputPath() + "/termith-score.json"));
        g.useDefaultPrettyPrinter();
        g.writeStartObject();
        g.writeFieldName("total_score");
        g.writeStartObject();
        g.writeNumberField("recall",_totalTermScore.getRecall());
        g.writeNumberField("precision",_totalTermScore.getPrecision());
        g.writeNumberField("f1_score",_totalTermScore.getF1score());
        g.writeEndObject();
        g.writeArrayFieldStart("terms");
        _scoreTerms.forEach(
                (key, scoreTerm) -> {
                    try {
                        g.writeStartObject();
                        g.writeStringField("id",key);
                        g.writeStringField("text",scoreTerm.getFlexionsWords());
                        g.writeStringField("pos",retrievePos(scoreTerm));
                        g.writeNumberField("recall",scoreTerm.getRecall());
                        g.writeNumberField("precision",scoreTerm.getPrecision());
                        g.writeNumberField("f1_score",scoreTerm.getF1Score());
                        g.writeNumberField("terminology_trend",scoreTerm.getTerminologyTrend());
                        g.writeNumberField("ambiguity_rate",scoreTerm.getAmbiguityRate());
                        g.writeEndObject();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );
        g.writeEndArray();
        g.writeEndObject();
        g.close();
    }

    String retrievePos(ScoreTerm scoreTerm) {
        String pos = "";
        LOGGER.info("extract term : " + scoreTerm.getFlexionsWords());
        for (ContextWord el : scoreTerm.getTermWords().get(0)) {
            pos += el.getPosLemma().split(" ")[1] + " ";
        }
        return pos.substring(0 ,pos.length() - 1);
    }
}
