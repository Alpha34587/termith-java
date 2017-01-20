package org.atilf.module.disambiguation;

import org.atilf.models.disambiguation.ScoreTerm;
import org.atilf.models.termith.TermithIndex;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Map;

/**
 * @author Simon Meoni Created on 03/01/17.
 */
public class ExportScoreToCsv extends ExportScoreToJson {

    protected ExportScoreToCsv(Map<String, ScoreTerm> scoreTerms) {
        super(scoreTerms,null);
    }

    public ExportScoreToCsv(TermithIndex termithIndex) {
        super(termithIndex);
    }
    @Override
    public void execute() {
        _logger.info("CSV exportation started");
        try {
            Locale.setDefault(new Locale("en", "US"));
            DecimalFormat df = new DecimalFormat("#.####");
            df.setRoundingMode(RoundingMode.CEILING);

            FileWriter fileWriter = new FileWriter(TermithIndex.getOutputPath() + "/termith-score.csv");
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(
                    "\"FL CT\",\"Lg\",\"POS\",\"Man On\",\"Man Off\",\"Nb occ\"," +
                            "\"Accord\",\"Désaccord\",\"Sans réponse\",\"Tendance terminologique\"," +
                            "\"Taux d'ambiguité\",\"Précision\",\"Rappel\",\"F-mesure\"\n"
            );

            _scoreTerms.forEach(
                    (key, value) -> {
                        try {
                        /*
                        write flexions words
                         */
                            bufferedWriter.write("\"" + value.getFlexionsWords() + "\",");
                        /*
                        write language
                         */
                            bufferedWriter.write("\"\",");
                        /*
                        write pos
                         */
                            bufferedWriter.write("\"" + retrievePos(value) + "\",");
                        /*
                        write number of term annotation
                         */
                            bufferedWriter.write("\"" + (int) value.getValidatedOccurrence() + "\",");
                        /*
                        write number of non term annotation
                         */
                            bufferedWriter.write("\""
                                    + (int) (value.getTotalOccurrences()
                                    - value.getValidatedOccurrence())
                                    + "\",");
                        /*
                        write the number of total occurrence
                         */
                            bufferedWriter.write("\"" + value.getTotalOccurrences() + "\",");
                        /*
                        write the number of correct annotation
                         */
                            bufferedWriter.write("\"" + value.getCorrectOccurrence() + "\",");
                        /*
                        write the number of incorrect annotation
                         */
                            bufferedWriter.write("\"" +
                                    (value.getTotalOccurrences() - value.getMissingOccurrence() - value.getCorrectOccurrence())
                                    + "\",");
                        /*
                        write the number of missing occurrence
                         */
                            bufferedWriter.write("\"" + value.getMissingOccurrence() + "\",");
                        /*
                        write the terminology trend
                         */
                            bufferedWriter.write("\"" + df.format(value.getTerminologyTrend()) + "\",");
                        /*
                        write the ambiguity rate
                         */
                            bufferedWriter.write("\"" + df.format(value.getAmbiguityRate()) + "\",");
                        /*
                        write the precision
                         */
                            bufferedWriter.write("\"" + df.format(value.getPrecision()) + "\",");
                        /*
                        write the recall
                         */
                            bufferedWriter.write("\"" + df.format(value.getRecall()) + "\",");
                        /*

                        write the f1-score
                         */
                            bufferedWriter.write("\"" + df.format(value.getF1Score()) + "\"\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            );
            bufferedWriter.flush();
            bufferedWriter.close();
        }
        catch (IOException e) {
            _logger.error("cannot export to CSV : ",e);
        }
        _logger.info("CSV exportation is finished");
    }
}
