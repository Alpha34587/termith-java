package org.atilf.module.disambiguisation;

import com.google.common.collect.Multiset;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Simon Meoni
 *         Created on 24/10/16.
 */
public class EvaluationProfile {
    Multiset lexicalTable;
    Map<String,String> disambIdMap;

    public EvaluationProfile(Multiset lexicalTable) {
        this.lexicalTable = lexicalTable;
        disambIdMap = new HashMap<>();
    }

    public Multiset getLexicalTable() {
        return lexicalTable;
    }

    public int countOccurence(String word){
        if (lexicalTable.contains(word)){
            return lexicalTable.count(word);
        }
        else
            return -1;
    }

    public Map<String, String> getDisambIdMap() {
        return disambIdMap;
    }

    public void addDisambId(String entry, String disambId){
        if (lexicalTable.contains(entry)) {
            disambIdMap.put(entry,disambId);
        }
        else{
            throw new NullPointerException("Multiset Object not contains this entry " + entry);
        }
    }
}
