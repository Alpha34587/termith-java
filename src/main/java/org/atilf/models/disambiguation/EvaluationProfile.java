package org.atilf.models.disambiguation;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import java.util.Iterator;
import java.util.function.Consumer;

import static org.atilf.models.disambiguation.AnnotationResources.NO_DA;

/**
 * the evaluation contains the context of a terms to evaluate and a the value of disambiguation.
 * the default value of the disambiguation is not noDa and can take three value : noDa, DaOn or DaOff
 * @author Simon Meoni
 *         Created on 24/10/16.
 */
public class EvaluationProfile implements Iterable<String> {
    private Multiset<String> _lexicalTable;
    private AnnotationResources _disambiguationId = NO_DA;

    /**
     * a constructor for evaluationProfile
     * @param lexicalTable an already initialized multiset
     */
    public EvaluationProfile(Multiset<String> lexicalTable) {
        _lexicalTable = lexicalTable;
    }

    /**
     * empty constructor of this class
     */
    public EvaluationProfile() {
        _lexicalTable = HashMultiset.create();
    }

    /**
     * get _lexicalTable of this class
     * @return a multiset
     */
    public Multiset<String> getLexicalTable() {
        return _lexicalTable;
    }

    /**
     * count the number of occurrences of a word
     * @param word a word to count in the corpus
     * @return return the number of occurrence of word
     */
    public int count(String word){
        if (_lexicalTable.contains(word)){
            return _lexicalTable.count(word);
        }
        else
            return -1;
    }

    /**
     * get the disambiguation id of this term
     * @return the disambiguation of this term
     */
    public String getDisambiguationId() {
        return _disambiguationId.getValue();
    }

    /**
     * setter of disambiguationId
     * @param _disambiguationId AnnotationResource
     */
    public void setDisambiguationId(AnnotationResources _disambiguationId) {
        this._disambiguationId = _disambiguationId;
    }

    /**
     * add a word to _lexicalTable
     * @param occ a word
     */
    public void addOccurrence(String occ){
        _lexicalTable.add(occ);
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
