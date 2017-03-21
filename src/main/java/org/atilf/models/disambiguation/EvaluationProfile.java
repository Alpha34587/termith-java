package org.atilf.models.disambiguation;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import static org.atilf.models.disambiguation.AnnotationResources.NO_DA;

/**
 * the evaluation contains the _context of a terms to evaluate and a the value of module.disambiguation.
 * the default value of the module.disambiguation is not noDa and can take three value : noDa, DaOn or DaOff
 * @author Simon Meoni
 *         Created on 24/10/16.
 */
public class EvaluationProfile extends Lexicon {
    private AnnotationResources _automaticAnnotation = NO_DA;

    /**
     * a constructor for evaluationProfile
     * @param lexicalTable an already initialized multiset
     */
    public EvaluationProfile(Multiset<String> lexicalTable) {
        super(lexicalTable);
    }

    /**
     * empty constructor of this class
     */
    public EvaluationProfile() {
        super(HashMultiset.create());
    }

    /**
     * get the module.disambiguation id of this term
     * @return the module.disambiguation of this term
     */
    public String getAutomaticAnnotation() {
        return _automaticAnnotation.getValue();
    }

    /**
     * setter of disambiguationId
     * @param _disambiguationId AnnotationResource
     */
    public void setAutomaticAnnotation(AnnotationResources _disambiguationId) {
        this._automaticAnnotation = _disambiguationId;
    }
}
