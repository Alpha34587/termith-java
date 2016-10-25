package org.atilf.module.disambiguisation;

import java.util.Map;

/**
 * @author Simon Meoni
 *         Created on 24/10/16.
 */
public class Evaluation {

    private final Map<String, EvaluationProfile> evaluationProfile;
    private final Map<String, LexicalProfile> termSubLexic;
    float factorOn = 0f;
    float factorOff = 0f;

    public Evaluation(Map<String, EvaluationProfile> evaluationProfile, Map<String, LexicalProfile> termSubLexic) {

        this.evaluationProfile = evaluationProfile;
        this.termSubLexic = termSubLexic;
    }

    public void execute() {
        evaluationProfile.forEach(
                (key,value) ->
                {

                    String lexEntryOn = formatEntry(key,"On");
                    String lexEntryOff = formatEntry(key, "Off");

                    if (!termSubLexic.containsKey(lexEntryOn)){
                        value.setDisambIdMap("DaOff");
                    }

                    else if (!termSubLexic.containsKey(lexEntryOff)){
                        value.setDisambIdMap("DaOn");
                    }

                    else {
                        computeFactor(value,
                                termSubLexic.get(lexEntryOn),
                                termSubLexic.get(lexEntryOff)
                        );
                        compareFactor(factorOn,factorOff, value);
                    }
                }
        );

    }

    private void compareFactor(float lexEntryOn, float lexEntryOff, EvaluationProfile entry) {
        if (lexEntryOff >= lexEntryOn){
            entry.setDisambIdMap("DaOff");
        }
        else {
            entry.setDisambIdMap("DaOn");
        }
    }

    private void computeFactor(EvaluationProfile entry,
                                LexicalProfile lexOn,
                                LexicalProfile lexOff) {
        entry.forEach(
                el -> {
                    factorOn += occurenceScore(lexOn.getSpecCoefficient(el),entry.countOccurence(el));
                    factorOff += occurenceScore(lexOff.getSpecCoefficient(el),entry.countOccurence(el));
                }
        );
    }

    private float occurenceScore(float specCoefficient, int nbOcc) {
        return (float) nbOcc * specCoefficient;
    }

    private String formatEntry(String key, String token) {
        String[] entrySplit = key.split("_");
        entrySplit[1] =  "lex" + token;

        return String.join("_", entrySplit);
    }
}
