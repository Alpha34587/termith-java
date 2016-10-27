package org.atilf.module.disambiguisation;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Simon Meoni
 *         Created on 21/10/16.
 */
public class LexicalProfile implements Iterable<String>{
    Multiset<String> _lexicalTable;
    Map<String,Float> _specCoefficientMap = new HashMap<>();

    LexicalProfile(Multiset<String> lexicalTable) {
        _lexicalTable = lexicalTable;
    }

    LexicalProfile() {
        _lexicalTable = HashMultiset.create();
    }

    Multiset<String> get_lexicalTable() {
        return _lexicalTable;
    }

    public int lexicalSize(){
        return _lexicalTable.size();
    }

    public int countOccurrence(String word){
        if (_lexicalTable.contains(word)){
            return _lexicalTable.count(word);
        }
        else
            return -1;
    }

    Map<String, Float> get_specCoefficientMap() {
        return _specCoefficientMap;
    }

    float getSpecCoefficient(String entry){
        if (_specCoefficientMap.containsKey(entry)) {
            return _specCoefficientMap.get(entry);
        }
        else
            return 0;
    }

    void addCoefficientSpec(String entry, Float coefficient){
        if (_lexicalTable.contains(entry)) {
            _specCoefficientMap.put(entry,coefficient);
        }
        else{

            throw new NullPointerException("Multiset Object not contains this entry " + entry);
        }
    }



    void addOccurrence(String occ){
        _lexicalTable.add(occ);
    }

    @Override
    public Iterator iterator() {
        return null;
    }

    @Override
    public void forEach(Consumer<? super String> consumer) {
        _lexicalTable.elementSet().forEach(consumer);
    }
}
