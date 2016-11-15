package org.atilf.models.disambiguation;

import com.google.common.collect.ConcurrentHashMultiset;
import com.google.common.collect.Multiset;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;

/**
 * @author Simon Meoni
 *         Created on 21/10/16.
 */
public class LexiconProfile implements Iterable<String>{
    private Multiset<String> _lexicalTable;
    private Map<String,Float> _specCoefficientMap = new HashMap<>();

    public LexiconProfile(Multiset<String> lexicalTable) {
        _lexicalTable = lexicalTable;
    }

    public LexiconProfile() {
        _lexicalTable = ConcurrentHashMultiset.create();
    }

    public Multiset<String> getLexicalTable() {
        return _lexicalTable;
    }

    int lexicalSize(){
        return _lexicalTable.size();
    }

    int countOccurrence(String word){
        if (_lexicalTable.contains(word)){
            return _lexicalTable.count(word);
        }
        else
            return -1;
    }

    public Map<String, Float> getSpecCoefficientMap() {
        return _specCoefficientMap;
    }

    public float getSpecCoefficient(String entry){
        if (_specCoefficientMap.containsKey(entry)) {
            return _specCoefficientMap.get(entry);
        }
        else
            return 0;
    }

    public void addCoefficientSpec(String entry, Float coefficient){
        if (_lexicalTable.contains(entry)) {
            _specCoefficientMap.put(entry,coefficient);
        }
        else{

            throw new NullPointerException("Multiset Object not contains this entry " + entry);
        }
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
