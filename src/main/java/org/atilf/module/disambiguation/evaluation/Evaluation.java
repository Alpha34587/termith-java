package org.atilf.module.disambiguation.evaluation;

import org.atilf.models.TermithIndex;
import org.atilf.models.disambiguation.AnnotationResources;
import org.atilf.models.disambiguation.EvaluationProfile;
import org.atilf.models.disambiguation.LexiconProfile;
import org.atilf.module.Module;

import java.util.Map;

/**
 * the evaluation module decide if a term candidate in a file is a terminology or not. For each term candidate
 * (it is a term candidate of a specific file) in _evaluationProfile
 * the associated context is compared with contexts in _contextLexicon
 * (the lexOn and lexOff contexts of the term candidate) of this term candidate.
 * For each words of _evaluationProfile presents in a lexicalProfile (_lexOn or _lexOff), the factor associated to this
 * lexicalProfile (_factorOn is the factor of _lexOn context and factorOff is the factor of _lexOff) is increased by
 * the specificity coefficient of this word in lexicalProfile multiply by this number of occurrence in the context of
 * the evaluationProfile.
 * Finally, if _factorOn is superior to _factorOff then the term candidate of the file is a terminology otherwise
 * the term candidate is not a terminology.
 * The result of the process is retained on the _evaluationProfile field
 * @author Simon Meoni
 *         Created on 24/10/16.
 */
public class Evaluation extends Module{

    private final Map<String, EvaluationProfile> _evaluationProfile;
    private final Map<String, LexiconProfile> _contextLexicon;
    private final String _p;
    float _factorOn = 0f;
    float _factorOff = 0f;

    /**
     * constructor of the Evaluation method
     * @param evaluationProfile the evaluationProfile of a termithIndex
     * @param contextLexicon the contextLexicon of a termithIndex
     */
    Evaluation(String p, Map<String, EvaluationProfile> evaluationProfile,
               Map<String, LexiconProfile> contextLexicon) {

        _evaluationProfile = evaluationProfile;
        _contextLexicon = contextLexicon;
        _p = p;
    }

    public Evaluation(String p, TermithIndex termithIndex) {
        super(termithIndex);
        _p = p;
        _evaluationProfile = _termithIndex.getEvaluationLexicon().get(_p);
        _contextLexicon = _termithIndex.getContextLexicon();
    }

    /**
     * this execute method browses the _evaluationProfile field and call computeFactor & compareFactor method if the
     * term candidate have a lexOn and a lexOff context in the _contextLexicon field
     */
    public void execute() {
        _logger.info("evaluate terms candidate from: " + _p);
        /*
        browse evaluation profile ("for each terms of a file")
         */
        _evaluationProfile.forEach(
                (key,value) ->
                {
                    _logger.debug("evaluation for term candidate: " + key + "for file: " + _p);
                    /*
                    convert evaluationProfile key to contextLexicon keys (suffix key with lexOn and lexOff)
                     */
                    String lexEntryOn = formatEntry(key,"On");
                    String lexEntryOff = formatEntry(key, "Off");

                    /*
                    check if _contextLexicon not contains the terminology context of the term. In this case,
                     the term is not a terminology
                     */
                    if (!_contextLexicon.containsKey(lexEntryOn)){
                        value.setAutomaticAnnotation(AnnotationResources.DA_OFF);
                        _logger.debug("term candidate: " + key + "for file: " + _p + " is not a terminology");

                    }

                    /*
                    check if _contextLexicon contains the non-terminology context of the term. In this case,
                     the term is terminology
                     */
                    else if (!_contextLexicon.containsKey(lexEntryOff)){
                        value.setAutomaticAnnotation(AnnotationResources.DA_ON);
                        _logger.debug("term candidate: " + key + "for file: " + _p + " is a terminology");
                    }

                    /*
                    otherwise the context comparing is performed
                     */
                    else {
                        /*
                        compute the _factorOn and _factorOff
                         */
                        _logger.debug("comparing factor for term candidate: " + key + "in file: " + _p);
                        computeFactor(value, _contextLexicon.get(lexEntryOn), _contextLexicon.get(lexEntryOff));
                        /*
                        compare factors
                         */
                        compareFactor(value);
                    }
                }
        );
        _logger.info("evaluation is finished for: " + _p);
    }

    /**
     * compare _factorOff and _factorOn to determine if the term candidate is a terminology or not.
     * If _factorOff is superior or equal to _factorOn the term candidate is not a terminology otherwise it is
     * a terminology.
     * @param entry an EvaluationProfile of a term candidate
     */
    private void compareFactor(EvaluationProfile entry) {
        if (_factorOff >= _factorOn){
            entry.setAutomaticAnnotation(AnnotationResources.DA_OFF);
        }
        else {
            entry.setAutomaticAnnotation(AnnotationResources.DA_ON);
        }
    }

    /**
     * compute the _factorOn and the _factorOff of a term candidate. for each word of a EvaluationProfile context,
     * the _factorOn is increased by the specificity coefficient of the current word in lexOn multiply
     * by the number of occurrence in the evaluationProfile. the _factorOff is compute in the way.
     * @param entry the evaluation profile of the current term candidate
     * @param lexOn the terminology lexicalProfile of the current term candidate
     * @param lexOff the non-terminology lexicalProfile of the current term candidate
     */
    void computeFactor(EvaluationProfile entry,
                       LexiconProfile lexOn,
                       LexiconProfile lexOff) {
        entry.forEach(
                el -> {
                    _factorOn += occurrenceScore(lexOn.getSpecCoefficient(el),entry.count(el));
                    _factorOff += occurrenceScore(lexOff.getSpecCoefficient(el),entry.count(el));
                }
        );
    }

    /**
     * compute the score for one word, this method is called by the computeFactor method
     * @param specCoefficient the specificity coefficient of a word
     * @param nbOcc the number of occurrence in the evaluation profile
     * @return the score of this word
     */
    private float occurrenceScore(float specCoefficient, int nbOcc) {
        return (float) nbOcc * specCoefficient;
    }

    /**
     * suffix key of evaluation profile by _lex + token string
     * @param key the key of evaluation profile
     * @param token the token (in this case the token can have two value "On" or "Off")
     * @return return the key suffix by _lexOn or _lexOff
     */
    private String formatEntry(String key, String token) {
        return key.split("_")[0] + "_lex" + token;
    }
}
