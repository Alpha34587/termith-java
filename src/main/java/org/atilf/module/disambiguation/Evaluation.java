package org.atilf.module.disambiguation;

import java.util.Map;

/**
 * @author Simon Meoni
 *         Created on 24/10/16.
 */
public class Evaluation implements Runnable{

    private final Map<String, EvaluationProfile> _evaluationProfile;
    private final Map<String, LexicalProfile> _termSubLexicon;
    private float _factorOn = 0f;
    private float _factorOff = 0f;

    public Evaluation(Map<String, EvaluationProfile> evaluationProfile, Map<String, LexicalProfile> termSubLexicon) {

        _evaluationProfile = evaluationProfile;
        _termSubLexicon = termSubLexicon;
    }

    public void execute() {
        _evaluationProfile.forEach(
                (key,value) ->
                {

                    String lexEntryOn = formatEntry(key,"On");
                    String lexEntryOff = formatEntry(key, "Off");

                    if (!_termSubLexicon.containsKey(lexEntryOn)){
                        value.setDisambiguationId("DaOff");
                    }

                    else if (!_termSubLexicon.containsKey(lexEntryOff)){
                        value.setDisambiguationId("DaOn");
                    }

                    else {
                        computeFactor(value, _termSubLexicon.get(lexEntryOn), _termSubLexicon.get(lexEntryOff));
                        compareFactor(_factorOn, _factorOff, value);
                    }
                }
        );

    }

    private void compareFactor(float lexEntryOn, float lexEntryOff, EvaluationProfile entry) {
        if (lexEntryOff >= lexEntryOn){
            entry.setDisambiguationId("DaOff");
        }
        else {
            entry.setDisambiguationId("DaOn");
        }
    }

    private void computeFactor(EvaluationProfile entry,
                                LexicalProfile lexOn,
                                LexicalProfile lexOff) {
        entry.forEach(
                el -> {
                    _factorOn += occurrenceScore(lexOn.getSpecCoefficient(el),entry.countOccurrence(el));
                    _factorOff += occurrenceScore(lexOff.getSpecCoefficient(el),entry.countOccurrence(el));
                }
        );
    }

    private float occurrenceScore(float specCoefficient, int nbOcc) {
        return (float) nbOcc * specCoefficient;
    }

    private String formatEntry(String key, String token) {
        return key.split("_")[0] + "_lex" + token;
    }

    @Override
    public void run() {
        this.execute();
    }
}
