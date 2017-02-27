package org.atilf.module.disambiguation.evaluationScore;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import org.atilf.models.TermithIndex;
import org.atilf.models.disambiguation.ContextWord;
import org.atilf.models.disambiguation.ScoreTerm;
import org.atilf.models.disambiguation.TotalTermScore;
import org.atilf.module.Module;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * @author Simon Meoni Created on 03/01/17.
 */
public class ExportScoreToJson extends Module {
    Map<String, ScoreTerm> _scoreTerms;
    private TotalTermScore _totalTermScore;
    Path _scoreFolder;
    private boolean _exportJs;

    ExportScoreToJson(Map<String, ScoreTerm> scoreTerms, TotalTermScore totalTermScore, Path scoreFolder,
                      boolean exportJs) {
        _scoreTerms = scoreTerms;
        _totalTermScore = totalTermScore;
        _scoreFolder = scoreFolder;
        _exportJs = exportJs;
    }

    public ExportScoreToJson(TermithIndex termithIndex, Path scoreFolder, boolean exportJs) {
        super(termithIndex);
        _scoreTerms = termithIndex.getScoreTerms();
        _totalTermScore = termithIndex.getTotalTermScore();
        _scoreFolder = scoreFolder;
        _exportJs = exportJs;
    }

    protected void execute(){
        try{
            _logger.info("exportation to json is started");
            JsonFactory f=new JsonFactory();
            JsonGenerator g=f.createGenerator(
                    new FileWriter(_scoreFolder + "/termith-score.json")
            );
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
                    (key,scoreTerm)->{
                        try{
                            g.writeStartObject();
                            g.writeStringField("id",key);
                            g.writeStringField("text",scoreTerm.getFlexingWords());
                            g.writeStringField("pos",retrievePos(scoreTerm));
                            g.writeNumberField("recall",scoreTerm.getRecall());
                            g.writeNumberField("precision",scoreTerm.getPrecision());
                            g.writeNumberField("f1_score",scoreTerm.getF1Score());
                            g.writeNumberField("terminology_trend",scoreTerm.getTerminologyTrend());
                            g.writeNumberField("ambiguity_rate",scoreTerm.getAmbiguityRate());
                            g.writeEndObject();
                        }catch(IOException e){
                            e.printStackTrace();
                        }
                    }
            );
            g.writeEndArray();
            g.writeEndObject();
            g.close();
            _logger.info("exportation to json is finished");
        }
        catch(IOException e){
            _logger.error("cannot export in JSON",e);

        }
        if (_exportJs){
            exportJs();
        }
    }

    private void exportJs() {
        try {
            Files.copy(
                    Paths.get("src/main/resources/score/Chart.js"),
                    Paths.get(_scoreFolder + "/Chart.js"),
                    REPLACE_EXISTING
            );

            Files.copy(
                    Paths.get("src/main/resources/score/graph.js"),
                    Paths.get(_scoreFolder + "/graph.js"),
                    REPLACE_EXISTING
            );


            Files.copy(
                    Paths.get("src/main/resources/score/graph.html"),
                    Paths.get(_scoreFolder + "/graph.html"),
                    REPLACE_EXISTING
            );
        }
        catch (IOException e) {
            _logger.error("cannot copy js/html file : ",e);
        }
    }

    String retrievePos(ScoreTerm scoreTerm) {
        String pos = "";
        _logger.info("extract term : " + scoreTerm.getFlexingWords());
        for (ContextWord el : scoreTerm.getTermWords().get(0)) {
            pos += el.getPosLemma().split(" ")[1] + " ";
        }
        return pos.substring(0 ,pos.length() - 1);
    }
}
