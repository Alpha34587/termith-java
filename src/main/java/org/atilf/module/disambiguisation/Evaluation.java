package org.atilf.module.disambiguisation;

import java.util.Map;

/**
 * @author Simon Meoni
 *         Created on 24/10/16.
 */
public class Evaluation {

    private final Map<String, EvaluationProfile> _evaluationProfile;
    private final Map<String, LexicalProfile> _termSubLexic;
    float _factorOn = 0f;
    float _factorOff = 0f;

    public Evaluation(Map<String, EvaluationProfile> evaluationProfile, Map<String, LexicalProfile> termSubLexic) {

        _evaluationProfile = evaluationProfile;
        _termSubLexic = termSubLexic;
    }

    public void execute() {
        _evaluationProfile.forEach(
                (key,value) ->
                {

                    String lexEntryOn = formatEntry(key,"On");
                    String lexEntryOff = formatEntry(key, "Off");

                    if (!_termSubLexic.containsKey(lexEntryOn)){
                        value.set_disambId("DaOff");
                    }

                    else if (!_termSubLexic.containsKey(lexEntryOff)){
                        value.set_disambId("DaOn");
                    }

                    else {
                        computeFactor(value, _termSubLexic.get(lexEntryOn), _termSubLexic.get(lexEntryOff));
                        compareFactor(_factorOn, _factorOff, value);
                    }
                }
        );

    }

    private void compareFactor(float lexEntryOn, float lexEntryOff, EvaluationProfile entry) {
        if (lexEntryOff >= lexEntryOn){
            entry.set_disambId("DaOff");
        }
        else {
            entry.set_disambId("DaOn");
        }
    }

    private void computeFactor(EvaluationProfile entry,
                                LexicalProfile lexOn,
                                LexicalProfile lexOff) {
        entry.forEach(
                el -> {
                    _factorOn += occurrenceScore(lexOn.getSpecCoefficient(el),entry.countOccurence(el));
                    _factorOff += occurrenceScore(lexOff.getSpecCoefficient(el),entry.countOccurence(el));
                }
        );
    }

    private float occurrenceScore(float specCoefficient, int nbOcc) {
        return (float) nbOcc * specCoefficient;
    }

    private String formatEntry(String key, String token) {
        return key.split("_")[0] + "_lex" + token;
    }
}
