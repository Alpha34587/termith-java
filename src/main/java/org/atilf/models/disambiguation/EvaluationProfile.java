package org.atilf.models.disambiguation;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import java.util.Iterator;
import java.util.function.Consumer;

/**
 * @author Simon Meoni
 *         Created on 24/10/16.
 */
public class EvaluationProfile implements Iterable<String> {
    private Multiset<String> _lexicalTable;
    private String _disambiguationId = "noDa";

    public EvaluationProfile(Multiset<String> lexicalTable) {
        _lexicalTable = lexicalTable;
    }

    public EvaluationProfile() {
        _lexicalTable = HashMultiset.create();
    }

    public Multiset<String> getLexicalTable() {
        return _lexicalTable;
    }

    public int countOccurrence(String word){
        if (_lexicalTable.contains(word)){
            return _lexicalTable.count(word);
        }
        else
            return -1;
    }

    public String getDisambiguationId() {
        return _disambiguationId;
    }

    public void setDisambiguationId(String _disambiguationId) {
        this._disambiguationId = _disambiguationId;
    }


    public void addOccurrence(String occ){
        _lexicalTable.add(occ);
    }

    @Override
    public Iterator<String> iterator() {
        return null;
    }

    @Override
    public void forEach(Consumer<? super String> consumer) {
        _lexicalTable.elementSet().forEach(consumer);
    }
}
