package org.atilf.models.disambiguation;

import com.google.common.collect.Multiset;
import org.atilf.module.disambiguation.lexiconprofile.SpecCoefficientInjector;

import java.util.HashMap;
import java.util.Map;

/**
 * The lexicon profile corresponds to a term associated to his _context. Each words of the _context
 * has a specificity coefficient computed by the SpecificityCoefficientInjector module.
 * @see SpecCoefficientInjector
 * @author Simon Meoni
 *         Created on 21/10/16.
 */
public class LexiconProfile extends Lexicon{
    private Map<String,Float> _specCoefficientMap = new HashMap<>();

    /**
     * a constructor for evaluationProfile
     * @param lexicalTable an already initialized multiset
     */
    public LexiconProfile(Multiset<String> lexicalTable) {
        super(lexicalTable);
    }

    /**
     * empty constructor of this class
     */
    public LexiconProfile() {
        super();
    }

    /**
     * get the map with specificities coefficients of each words
     * @return return the map
     */
    public Map<String, Float> getSpecCoefficientMap() {
        return _specCoefficientMap;
    }

    /**
     * get a particularly coefficient of a word
     * @param word add a specificity coefficient to this word
     * @return the value of a coefficient
     */
    public float getSpecCoefficient(String word){
        if (_specCoefficientMap.containsKey(word)) {
            return _specCoefficientMap.get(word);
        }
        else
            return 0;
    }

    /**
     * add specificity coefficient to a word
     * @param word the word
     * @param coefficient he value of the coefficient
     */
    public void addCoefficientSpec(String word, Float coefficient){
        if (_lexicalTable.contains(word)) {
            _specCoefficientMap.put(word,coefficient);
        }
        else{

            throw new NullPointerException("Multiset Object not contains this entry " + word);
        }
    }
}
