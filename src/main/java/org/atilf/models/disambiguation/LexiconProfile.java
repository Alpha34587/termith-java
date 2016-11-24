package org.atilf.models.disambiguation;

import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.Multiset;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

/**
 * The lexicon profile corresponds to a term associated to his context. Each words of the context
 * has a specificity coefficient computed by the SpecificityCoefficientInjector module.
 * @see org.atilf.module.disambiguation.SpecCoefficientInjector
 * @author Simon Meoni
 *         Created on 21/10/16.
 */
public class LexiconProfile implements Iterable<String>{
    private Multiset<String> _lexicalTable;
    private Map<String,Float> _specCoefficientMap = new HashMap<>();

    /**
     * a constructor for evaluationProfile
     * @param lexicalTable an already initialized multiset
     */
    public LexiconProfile(Multiset<String> lexicalTable) {
        _lexicalTable = lexicalTable;
    }

    /**
     * empty constructor of this class
     */
    public LexiconProfile() {
        _lexicalTable = ConcurrentHashMultiset.create();
    }

    /**
     * get _lexicalTable of this class
     * @return a multiset
     */
    public Multiset<String> getLexicalTable() {
        return _lexicalTable;
    }

    /**
     * get the size of _lexicalTable
     * @return return the size of the lexicalTable
     */
    int lexicalSize(){
        return _lexicalTable.size();
    }

    /**
     * count the number of occurrences of a word
     * @param word a word to count in the corpus
     * @return return the number of occurrence of word
     */
    int count(String word){
        if (_lexicalTable.contains(word)){
            return _lexicalTable.count(word);
        }
        else
            return -1;
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
     * @param word
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

    /**
     * add a word to _lexicalTable
     * @param word the word
     */
    public void addOccurrence(String word){
        _lexicalTable.add(word);
    }

    /**
     * override iterator
     * @return return String iterator
     */
    @Override
    public Iterator<String> iterator() {
        return null;
    }

    /**
     * this foreach lambda method is associated to the multiset of the corpus
     * @param consumer the current string consumer
     */
    @Override
    public void forEach(Consumer<? super String> consumer) {
        _lexicalTable.elementSet().forEach(consumer);
    }
}
